# ela-invoice-app

This is a test spring boot camunda app.

## Below are the steps to recreate this project from scratch
- Refer to https://www.youtube.com/watch?v=sHgf_EsQzfc&t=3s to get an idea about the sample project.
- Go to https://start.camunda.com/ and create an empty spring boot camunda project
  - Select "On-Disk H2" as the database of choice for this sample
  - Provide a simple admin username/password.
  - Click "Generate Project" which downloads a zip file.
  - Extract and open the project in IDE of your choice.
  - Add a .gitignore file. Copy from a sample spring boot project(https://github.com/elamgit/spring-docker-ghactions/blob/master/.gitignore). Add camunda-h2-database* to the gitignore file.
  - Run the junit test "WorkflowTest.java" and make sure it runs green.
  - Run the main Spring boot class Application.java from the IDE and open localhost:8080 and play around with the sample process.bpmn.
  - Perform an "initial commit to git"
- Add spring profiles "prod" and "unittest".
  - spring profiles unittest can have in-memory h2 db.
  - add logback and logback-local.xml (for unittest)
  - Test. Normal run should create h2 db file and unit test should use in-memory and skip creating files.
  - Commit
- Add a UserConfiguration class to programmatically add users to the app.
  - Run and test the app in browser. You should be able to log in with test user that was added programmatically.
  - Commit
- Add sample invoice app
  - Stop the application and delete process.bpmn from resources folder.
  - Copy the bpmn files(ignore the v1 and copy only the v2), dmn file, invoice.pdf file and the META-INF folder with its processes.xml file from https://github.com/camunda/camunda-bpm-platform/tree/master/examples/invoice/src/main/resources to the resources folder.
  - Copy the forms from https://github.com/camunda/camunda-bpm-platform/tree/master/examples/invoice/src/main/webapp/forms to src/main/resources/static/forms
  - Copy the service classes from https://github.com/camunda/camunda-bpm-platform/tree/master/examples/invoice/src/main/java/org/camunda/bpm/example/invoice/service
  - Change references to org.camunda.bpm.example.invoice.service.ArchiveInvoiceService to this project's structure. 
  - Ignore the test in WorkflowTest case for now. It will fail since the bpmn files have changed.
  - Rename Application.java to InvoiceApplication.java
  - Add @EnableProcessApplication annotation to InvoiceApplication.java (forms will not work if this is not done. refer https://www.youtube.com/watch?v=ZFwS1WJUKCU&t=358s)
  - Start InvoiceApplication.java and test in browser.
  - Note - I was able to get the process going only after creating a group called accounting and adding the user "user1" to this group.
  - Commit
- Fix test case
  - try to copy from https://github.com/camunda/camunda-bpm-platform/blob/master/examples/invoice/src/test/java/org/camunda/bpm/example/invoice/InvoiceTestCase.java
  - testHappyPathV2()
  - Commit
  