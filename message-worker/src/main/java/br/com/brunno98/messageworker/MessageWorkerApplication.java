package br.com.brunno98.messageworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MessageWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageWorkerApplication.class, args);
	}

}
