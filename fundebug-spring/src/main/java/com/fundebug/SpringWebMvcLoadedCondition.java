package com.fundebug;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

class SpringWebMvcLoadedCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return context.getClassLoader() != null
				&& context.getClassLoader().getResource("org/springframework/web/servlet") != null;
	}
}