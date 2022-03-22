package com.restaurant.restaurantmanagementapi.controller;

import com.restaurant.restaurantmanagementapi.model.MenuItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping(path = "/menu-item")
public class MenuItemController {
    private MenuItem menuItem=new MenuItem(1,"name","des","img",3.5);
    @GetMapping("/{id}")
    public MenuItem getMenuItem(@PathVariable long id) {
        return menuItem;
    }
}
