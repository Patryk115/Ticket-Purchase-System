Components:
* Camunda BPMN Process: A Camunda Platform 8 process that orchestrates the ticket purchase workflow, including user data input, discount calculation, payment processing, and ticket generation.
* Spring Boot Application: Backend application implementing workers for Camunda tasks and handling business logic.
* PostgreSQL Database: Stores ticket details generated during the process.

Requirements:
* Camunda Platform 8
* Java >= 17
* Maven

Run:
* Download/clone the code.
* Create a Camunda 8 SaaS cluster.
* Set API client connection details in the file `application.properties`.
* Run the application:

```
mvn package exec:java
```

