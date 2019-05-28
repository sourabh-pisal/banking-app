# Banking Application
Simple Java based Banking Application for sending money between accounts

* Please make sure Java 8 is installed by running following command into terminal<br />
``java -version``<br />

* To Build and Run Project execute following command<br />
``./gradlew build && java -jar build/libs/banking-1.0.0.jar``<br />

* The Application can also be deployed on Docker<br />
``docker build -t banking-app .``<br />
``docker run -p 8080:8080 banking-app``<br />

* Please note some controller itegration test cases are failing and are marked with @ignore and will be fixed in later releases.<br />

* The project uses Spring framework for dependency injection, spring boot for auto-configuring spring, in-memory database(H2) for prototyping along with Hibernate and embedded tomcat.<br />

* To minimize boiled-plate code for POJO Lombok plugin is used.<br />

* The transaction is done by locking specific row in account table using Pessimistic locking at database side. To verify transaction locking seperate integration test is present in AccountServiceITest.<br />

* To Create customer please run the below Post Rest<br />
``curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Curtis","lastName":"Sheridan","dateOfBirth":"2000-08-15","active":true}' http://localhost:8080/customer/create``<br />

* The above rest will return CustomerDto object consisting of Customer Id<br />
* Minimum two customers are required for account transfer<br />
* For creating second customer<br />
``curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Katrina","lastName":"Faulkner","dateOfBirth":"1991-05-27","active":true}' http://localhost:8080/customer/create``<br />

* For creating Account which is required for transfering run the following Post Rest<br />
``curl -H "Content-Type: application/json" -X POST -d '{"customer":{"customerId":1},"currentBalance":"5000","active":true}' http://localhost:8080/account/create``<br />
``curl -H "Content-Type: application/json" -X POST -d '{"customer":{"customerId":2},"currentBalance":"2000","active":true}' http://localhost:8080/account/create``<br />

* Both rest will create two accounts one for customer 1 and other for customer 2<br />
* To transfer money between two accounts please run below Put Rest<br />
``curl -X PUT 'http://localhost:8080/account/transfer?from-account-id=1&to-account-id=2&balance=1000'``<br />

* The above rest will transfer 1000 from customer 1 to customer 2<br />
* To Get all the transactions for given account number below is the Get request<br />
``curl http://localhost:8080/account/1/get-transaction-details?page-number=0``<br />

* Future improments
    * Some Test cases are failing need to fix them
    * Add exception handling at controller layer
    * Introduce Authentication and authorization using oauth2 and JWT
    * Database mirgration using flyway/liquibase
    * Implementing CI/CD Pipeline using either CircleCi or TravisCI
