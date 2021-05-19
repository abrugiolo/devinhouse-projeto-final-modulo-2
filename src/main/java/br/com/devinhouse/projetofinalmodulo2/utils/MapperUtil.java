package br.com.devinhouse.projetofinalmodulo2.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;

@Configuration
public class MapperUtil {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
