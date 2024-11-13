package com.bank.account;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(info = @Info(
		title = "Account service API",
		version = "v1",
		description = """
			# Account service related api documentation
			## Steps to create a bank account and get bank account details, user details etc.:
			1. Create an user with user/register api. You will get user object in response
			2. Login with userName with password of your user. You will get a token
			3. For all other api endPoints you will have to use Authorization header and put the token you have got in step 2
				
			*** Note: If you want to test the endpoints with postman, then you can collect the curl by requesting the endpoint here once, and then use that curl in your postman for testing.
				"""))
@SpringBootApplication
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

}
