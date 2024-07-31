package com.spring_modules;

import ch.qos.logback.core.net.server.Client;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@SpringBootApplication
@EnableIntegrationManagement
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

//    @Bean
    public CommandLineRunner clientHttpReq() {
        CommandLineRunner commandLineRunner = new CommandLineRunner() {
            @Override
            public void run(String... args) {
                String requestBody = "Hello, World!";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_PLAIN);

                RequestEntity<String> requestEntityObj = RequestEntity.post(URI.create("http://localhost:8080/receiveGateway"))
                        .accept(MediaType.TEXT_PLAIN)
                        .body(requestBody);

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntityObj, String.class);
                String responseBody = responseEntity.getBody();
                System.out.println("Response: " + responseBody);
            }
        };
        return commandLineRunner;
    }


}
