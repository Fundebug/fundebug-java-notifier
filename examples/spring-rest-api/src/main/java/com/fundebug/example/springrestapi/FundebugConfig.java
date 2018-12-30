package com.fundebug.example.springrestapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fundebug.Fundebug;
import com.fundebug.SpringConfig;

@Configuration
@Import(SpringConfig.class)
public class FundebugConfig {
	@Bean
	public Fundebug getBean() {
		return new Fundebug("716b45610ebaee70faa193663a0aa03813d8fcbb5c1ee9201c0417e17ed5a728");
	}
}