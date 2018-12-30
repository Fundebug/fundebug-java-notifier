package com.fundebug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(SpringWebMvcLoadedCondition.class)
public class SpringConfig {

	@Autowired
	private Fundebug fundebug;

	@Bean
	FundebugExceptionHandler fundebugExceptionHandler() {
		return new FundebugExceptionHandler(fundebug);
	}
}