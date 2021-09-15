package com.digitalmaxim.workflow;

import camundajar.javax.activation.MimetypesFileTypeMap;
import junit.framework.AssertionFailedError;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.builder.FileValueBuilderImpl;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.value.builder.FileValueBuilder;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WorkflowTest extends AbstractProcessEngineRuleTest {

  @Autowired
  public ProcessEngine processEngine;

  @Autowired
  private RuntimeService runtimeService;

  @Autowired
  private TaskService taskService;

  @Autowired
  private ManagementService managementService;

  @Deployment(resources= {"invoice.v2.bpmn", "invoiceBusinessDecisions.dmn"})
  @Test
  public void testHappyPathV2() {
    InputStream invoiceInputStream = InvoiceApplication.class.getClassLoader().getResourceAsStream("invoice.pdf");
    VariableMap variables = Variables.createVariables()
            .putValue("creditor", "Great Pizza for Everyone Inc.")
            .putValue("amount", 300.0d)
            .putValue("invoiceCategory", "Travel Expenses")
            .putValue("invoiceNumber", "GPFE-23232323")
            .putValue("invoiceDocument", fileValue("invoice.pdf")
                    .file(invoiceInputStream)
                    .mimeType("application/pdf")
                    .create());

    ProcessInstance pi = runtimeService.startProcessInstanceByKey("invoice", variables);

    Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
    assertEquals("approveInvoice", task.getTaskDefinitionKey());

    List<IdentityLink> links = taskService.getIdentityLinksForTask(task.getId());
    Set<String> approverGroups = new HashSet<String>();
    for (IdentityLink link : links) {
      approverGroups.add(link.getGroupId());
    }
    assertEquals(2, approverGroups.size());
    assertTrue(approverGroups.contains("accounting"));
    assertTrue(approverGroups.contains("sales"));

    variables.clear();
    variables.put("approved", Boolean.TRUE);
    taskService.complete(task.getId(), variables);

    task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();

    assertEquals("prepareBankTransfer", task.getTaskDefinitionKey());
    taskService.complete(task.getId());

    Job archiveInvoiceJob = managementService.createJobQuery().singleResult();
    assertNotNull(archiveInvoiceJob);
    managementService.executeJob(archiveInvoiceJob.getId());

    assertProcessEnded(pi.getId());
  }

  public void assertProcessEnded(final String processInstanceId) {
    ProcessInstance processInstance = processEngine
            .getRuntimeService()
            .createProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult();

    if (processInstance!=null) {
      throw new AssertionFailedError("Expected finished process instance '"+processInstanceId+"' but it was still in the db");
    }
  }

  public static FileValueBuilder fileValue(String filename) {
    return fileValue(filename, false);
  }

  public static FileValueBuilder fileValue(String filename, boolean isTransient) {
    return new FileValueBuilderImpl(filename).setTransient(isTransient);
  }

  public static FileValue fileValue(File file){
    String contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file);
    return new FileValueBuilderImpl(file.getName()).file(file).mimeType(contentType).create();
  }

}
