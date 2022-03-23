package com.restaurant.restaurantmanagementapi.controller;

import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.repository.MenuItemRepository;
import com.restaurant.restaurantmanagementapi.utils.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/menu-item")
public class MenuItemController {
    @Autowired
    MenuItemRepository menuItemRepository;

    @GetMapping("/{id}")
    public MenuItem getMenuItemById(@PathVariable("id") Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }
    @GetMapping("")
    public List<MenuItem> getMenuItems() {
        return menuItemRepository.findAll();
    }
    @PostMapping("")
    MenuItem addMenuItem(@RequestBody MenuItem newMenuItem) {
        newMenuItem.setIsDeleted(false);

        return menuItemRepository.save(newMenuItem);
    }
    @PutMapping("/{id}")
    MenuItem updateMenuItem(@RequestBody MenuItem newMenuItem, @PathVariable Long id) {
        Optional<MenuItem> existedMenuItem=menuItemRepository.findById(id);
        existedMenuItem.get().setName(newMenuItem.getName());
        existedMenuItem.get().setDescription(newMenuItem.getDescription());
        existedMenuItem.get().setImage(newMenuItem.getImage());
        existedMenuItem.get().setPrice(newMenuItem.getPrice());
        return menuItemRepository.save(existedMenuItem.get());
    }
    @DeleteMapping("/{id}")
    void deleteMenuItem(@PathVariable Long id) {
        Optional<MenuItem> existedMenuItem=menuItemRepository.findById(id);
        existedMenuItem.get().setIsDeleted(true);
        menuItemRepository.save(existedMenuItem.get());
    }
}
