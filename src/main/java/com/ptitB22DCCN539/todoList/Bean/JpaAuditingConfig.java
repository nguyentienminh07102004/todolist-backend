package com.ptitB22DCCN539.todoList.Bean;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "Config")
public class JpaAuditingConfig implements AuditorAware<String> {

	@Override
	@NonNull
	public Optional<String> getCurrentAuditor() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return Optional.ofNullable(email);
	}

	@Bean
	JpaAuditingConfig Config() {
		return new JpaAuditingConfig();
	}

}
