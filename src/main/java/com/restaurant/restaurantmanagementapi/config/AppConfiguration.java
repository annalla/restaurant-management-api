package com.restaurant.restaurantmanagementapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class AppConfiguration {
    @Bean
    DatabaseConfiguration initDatabaseConfiguration(){
        return new DatabaseConfiguration();
    }
}
