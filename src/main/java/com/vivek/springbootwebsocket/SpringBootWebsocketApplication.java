package com.vivek.springbootwebsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class SpringBootWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebsocketApplication.class, args);

        System.out.println("This App is Running on 8080");
    }

}
