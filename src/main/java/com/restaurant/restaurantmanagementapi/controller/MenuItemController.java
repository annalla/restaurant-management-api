package com.restaurant.restaurantmanagementapi.controller;

import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.repository.MenuItemRepository;
import com.restaurant.restaurantmanagementapi.utils.BadRequestException;
import com.restaurant.restaurantmanagementapi.utils.Message;
import com.restaurant.restaurantmanagementapi.utils.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<MenuItem> getMenuItems(Pageable pageable) {
        return menuItemRepository.findAll(pageable).getContent();
    }
    @PostMapping("")
    MenuItem addMenuItem(@RequestBody MenuItem newMenuItem) {
        if(checkExistedName(newMenuItem.getName())){
            throw new BadRequestException(Message.EXISTED_NAME);
        }
        newMenuItem.setIsDeleted(false);
        return menuItemRepository.save(newMenuItem);
    }
    @PutMapping("/{id}")
    MenuItem updateMenuItem(@RequestBody MenuItem newMenuItem, @PathVariable Long id) {
        menuItemRepository.findById(id).map(existedMenuItem -> {
            if(!checkExistedName(newMenuItem.getName())){
                existedMenuItem.setName(newMenuItem.getName());
            }
            existedMenuItem.setDescription(newMenuItem.getDescription());
            existedMenuItem.setImage(newMenuItem.getImage());
            existedMenuItem.setPrice(newMenuItem.getPrice());
            return menuItemRepository.save(existedMenuItem);
        }).orElseThrow(() -> new NotFoundException(id));
        return menuItemRepository.findById(id).get();
    }
    @DeleteMapping("/{id}")
    boolean deleteMenuItem(@PathVariable Long id) {
        menuItemRepository.findById(id).map(existedMenuItem -> {
            existedMenuItem.setIsDeleted(true);
            menuItemRepository.save(existedMenuItem);
            return true;
        }).orElseThrow(() -> new NotFoundException(id));
        return true;
    }
    private boolean checkExistedName(String name){
        System.out.println(menuItemRepository.findByName(name).isPresent());
        System.out.println(menuItemRepository.findByName(name));
        return menuItemRepository.findByName(name).isPresent();
    }
}
