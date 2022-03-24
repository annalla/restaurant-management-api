package com.restaurant.restaurantmanagementapi;

import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.repository.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(MenuItemRepository repository) {
        return args -> {
            if (repository.findAll().isEmpty()){
                log.info("Preloading " + repository.save(new MenuItem("Bread","bread,egg","bread.img","",2.1)));
                log.info("Preloading " + repository.save(new MenuItem("Rice","rice,fish","rice.img","",4.1)));
                log.info("Preloading " + repository.save(new MenuItem("Noodle","noodle,beef","noodle.img","",6.1)));
                log.info("Preloading " + repository.save(new MenuItem("Beer","","beer.img","",3.1)));
                log.info("Preloading " + repository.save(new MenuItem("Water","","water.img","",1.5)));
            }
        };
    }
}