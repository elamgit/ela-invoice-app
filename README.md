# ela-invoice-app

This is test app for playing with camunda.

## Below are the steps to recreate this project from scratch

- Go to https://start.camunda.com/ and create an empty spring boot camunda project
  - I selected "On-Disk H2" as the database of choice for this sample
  - Provide a simple admin username/password.
  - Click Generate Project which download a zip file.
  - Extract and open the project in IDE of your choice.
  - Add a .gitignore file from a sample spring boot project.
  - Run the test WorkflowTest and make sure it runs green.
  - Make an initial commit.
- Copy the sample invoice bpmn files from https://github.com/camunda/camunda-bpm-platform/tree/master/examples/invoice/src/main/resources to src/main/resources folder.
- 