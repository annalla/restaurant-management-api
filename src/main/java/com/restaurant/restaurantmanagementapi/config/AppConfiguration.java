package com.restaurant.restaurantmanagementapi.config;

import com.restaurant.restaurantmanagementapi.utils.Constant;
import com.restaurant.restaurantmanagementapi.utils.Path;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * The AppConfiguration class allows to define Bean in Spring IOC Container
 */
@Configuration
@EnableSwagger2
public class AppConfiguration {
    @Bean
    DatabaseConfiguration databaseConfiguration() {
        return new DatabaseConfiguration();
    }
    @Bean
    public Docket docketSwagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(Constant.MAIN_PACKAGE))
                .paths(regex(Path.ROOT))
                .build();
    }

}
