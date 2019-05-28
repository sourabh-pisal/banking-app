# Banking Application
Simple Java based Banking Application for sending money between accounts

* Please make sure Java 8 is installed by running following command into terminal  
``java -version``

* To Build and Run Project execute following command
``./gradlew build -x test && java -jar build/libs/banking-1.0.0.jar``

* The Application can also be deployed on Docker
``docker build -t banking-app .``
``docker run -p 8080:8080 banking-app``

* Please note some controller itegration test cases are failing and are marked with @ignore and will be fixed in later releases
* The project uses Spring framework for dependency injection, spring boot for auto-configuring spring, in-memory database(H2) for prototyping along with Hibernate and embedded tomcat.
* To minimize boiled-plate code for POJO Lombok plugin is used.
* The transaction is done by locking specific row in account table using Pessimistic locking at database side. To verify transaction locking seperate integration test is present in AccountServiceITest.

* To Create customer please run the below Post Rest
``curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Curtis","lastName":"Sheridan","dateOfBirth":"2000-08-15","active":true}' http://localhost:8080/customer/create``

* The above rest will return CustomerDto object consisting of Customer Id
* Minimum two customers are required for account transfer
* For creating second customer
``curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Katrina","lastName":"Faulkner","dateOfBirth":"1991-05-27","active":true}' http://localhost:8080/customer/create``

* For creating Account which is required for transfering run the following Post Rest
``curl -H "Content-Type: application/json" -X POST -d '{"customer":{"customerId":1},"currentBalance":"5000","active":true}' http://localhost:8080/account/create``
``curl -H "Content-Type: application/json" -X POST -d '{"customer":{"customerId":2},"currentBalance":"2000","active":true}' http://localhost:8080/account/create``

* Both rest will create two accounts one for customer 1 and other for customer 2
* To transfer money between two accounts please run below Put Rest
``curl -X PUT 'http://localhost:8080/account/transfer?from-account-id=1&to-account-id=2&balance=1000'``

* The above rest will transfer 1000 from customer 1 to customer 2
* To Get all the transactions for given account number below is the Get request
``curl http://localhost:8080/account/1/get-transaction-details?page-number=0``

* Future improments
    * Some Test cases are failing need to fix them
    * Add exception handling at controller layer
    * Introduce Authentication and authorization using oauth2 and JWT
    * Database mirgration using flyway/liquibase
    * Implementing CI/CD Pipeline using either CircleCi or TravisCI
