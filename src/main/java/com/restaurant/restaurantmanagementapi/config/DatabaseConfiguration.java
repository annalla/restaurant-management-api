package com.restaurant.restaurantmanagementapi.config;

import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.repository.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Configuration
@Component
public class DatabaseConfiguration {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);
    @Autowired
    MenuItemRepository repository;

    @PostConstruct
    void initDatabase() {
        System.out.println("here");
        System.out.println(repository.findAll());
        System.out.println(repository.findAll().isEmpty());
        if (repository.findAll().isEmpty()) {
            log.info("Preloading " + repository.save(new MenuItem("Bread", "bread,egg", "bread.img", "", 2.1)));
            log.info("Preloading " + repository.save(new MenuItem("Rice", "rice,fish", "rice.img", "", 4.1)));
            log.info("Preloading " + repository.save(new MenuItem("Noodle", "noodle,beef", "noodle.img", "", 6.1)));
            log.info("Preloading " + repository.save(new MenuItem("Beer", "", "beer.img", "", 3.1)));
            log.info("Preloading " + repository.save(new MenuItem("Water", "", "water.img", "", 1.5)));
        }

    }
}
