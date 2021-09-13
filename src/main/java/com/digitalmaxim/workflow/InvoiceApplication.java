package com.digitalmaxim.workflow;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class InvoiceApplication {

  public static void main(String... args) {
    SpringApplication.run(InvoiceApplication.class, args);
  }

}