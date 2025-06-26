package com.coffeeshop.management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {
    // This class can be used for general application-wide configurations
    // For example, defining beans for mappers, or other utility classes
}