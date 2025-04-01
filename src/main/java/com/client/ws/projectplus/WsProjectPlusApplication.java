package com.client.ws.projectplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class WsProjectPlusApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsProjectPlusApplication.class, args);
	}

}
