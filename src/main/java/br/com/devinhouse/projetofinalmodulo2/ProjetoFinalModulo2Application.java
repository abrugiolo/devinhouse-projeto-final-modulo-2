package br.com.devinhouse.projetofinalmodulo2;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProjetoFinalModulo2Application {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoFinalModulo2Application.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
