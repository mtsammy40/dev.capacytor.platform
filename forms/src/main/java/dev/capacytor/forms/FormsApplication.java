package dev.capacytor.forms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class FormsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FormsApplication.class, args);
	}

}
