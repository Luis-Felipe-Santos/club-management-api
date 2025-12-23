package dev.clube_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class CadastroClubeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CadastroClubeApplication.class, args);
	}

}
