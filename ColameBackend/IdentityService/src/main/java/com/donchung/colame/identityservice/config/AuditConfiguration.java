package com.donchung.colame.identityservice.config;

import com.donchung.colame.identityservice.POJO.EntityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
/*
In this article, you have learned about Spring Data JPA auditing and how to enable it in a Spring Boot and MySQL application.

In short, all you need to do is the following to enable the JPA auditing feature:

Define the audit fields using the @CreatedDate, @CreatedBy, @LastModifiedDate, and @LastModfiiedBy annotations. The best way to do so is by creating a generic abstract class and extending the entities that need the auditing functionality.
Implement the AuditorAware interface to let Spring Data JPA auditing infrastructure know about the currently logged-in user making the changes.
Add the @EnableJpaAuditing annotation to any configuration class to enable JPA auditing.
Expose a bean of type AuditorAware (only required if you need an auditing author).
 */

@Configuration
@EnableJpaAuditing
public class AuditConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new EntityAuditorAware();
    }
}
