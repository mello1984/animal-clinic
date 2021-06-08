package ru.butakov.animalclinic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "/private.properties", ignoreResourceNotFound = true)
public class AppConfig {
}
