# ela-invoice-app

This is test app for playing with camunda.

## Below are the steps to recreate this project from scratch
- Refer to https://www.youtube.com/watch?v=sHgf_EsQzfc&t=3s to get an idea about the sample project.
- Go to https://start.camunda.com/ and create an empty spring boot camunda project
  - I selected "On-Disk H2" as the database of choice for this sample
  - Provide a simple admin username/password.
  - Click Generate Project which download a zip file.
  - Extract and open the project in IDE of your choice.
  - Add a .gitignore file from a sample spring boot project. Add camunda-h2-database* to the gitignore file.
  - Make an initial commit.
  - Run the test WorkflowTest and make sure it runs green.
  - Run the main Spring boot class Application.java from the IDE and open localhost:8080 and play around with the sample process.bpmn.
- Stop the application and delete process.bpmn from resources folder.
- 