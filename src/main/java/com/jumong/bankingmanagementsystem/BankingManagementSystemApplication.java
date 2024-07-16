package com.jumong.bankingmanagementsystem;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Jumong Banking Management System",
                version = "1.0.0",
                description = "A simple banking management system API using Spring Boot",
                contact = @Contact(
                        name = "Lawal Toheeb",
                        email = "lawal.toheeb@gmail.com",
                        url = "https://github.com/Jumongweb/BankManagementSystem"
                ),
                license = @License(
                        name = "Jumong",
                        url = "https://github.com/Jumongweb/BankManagementSystem"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Jumong Bank App Documentation",
                url = "https://github.com/Jumongweb/BankManagementSystem"

        )
)
public class BankingManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingManagementSystemApplication.class, args);
    }

}
