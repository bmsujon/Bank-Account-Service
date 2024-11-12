# Bank-Account-Service

This is prepared as a demo project.

This project uses: 
1. Spring boot 3.3.5
2. Java 21
3. Spring data JPA
4. My SQL
5. Maven

Features:
1. User management
2. Bank account management

You can find the DB source in application.yaml
I have used DB name as bank_account_db
If you want to keep this name, you can create the DB with the following script in my sql workbench or any DB tool you are using:
CREATE DATABASE `bank_account_db`

You can also change the DB name and change the script as per that name.

How to run:
1. Clone the repo.
2. maven clean & build
3. run from your editor.

This project uses open-api for documentation and you can find it here after you run the project:
http://localhost:8083/account-service/api/v1/swagger-ui/index.html#