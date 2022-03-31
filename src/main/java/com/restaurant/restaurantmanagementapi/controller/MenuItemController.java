package com.restaurant.restaurantmanagementapi.controller;

import com.restaurant.restaurantmanagementapi.dto.MenuItemResponse;
import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.repository.MenuItemRepository;
import com.restaurant.restaurantmanagementapi.exception.BadRequestException;
import com.restaurant.restaurantmanagementapi.service.MenuItemService;
import com.restaurant.restaurantmanagementapi.utils.Message;
import com.restaurant.restaurantmanagementapi.exception.NotFoundException;
import com.restaurant.restaurantmanagementapi.utils.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = Path.MENU_ITEM)
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;
    @GetMapping(Path.ID)
    public MenuItemResponse getMenuItemById(@PathVariable("id") Long id) {
        MenuItemResponse menuItemResponse=menuItemService.getById(id);
        if(menuItemResponse==null){
            throw new NotFoundException(id);
        }
        return menuItemResponse;
    }
    @GetMapping()
    public List<MenuItemResponse> getMenuItems(Pageable pageable) {
        return menuItemService.getAll(pageable);
    }
    @PostMapping()
    public MenuItemResponse addMenuItem(@RequestBody MenuItem newMenuItem) {
        String message=menuItemService.check(newMenuItem);
        if(!message.equals(Message.OK)){
            throw new BadRequestException(message);
        }
        return menuItemService.add(newMenuItem);
    }
    @PutMapping(Path.ID)
    public MenuItemResponse updateMenuItem(@RequestBody MenuItem newMenuItem, @PathVariable Long id) {
        String message=menuItemService.check(newMenuItem);
        if(message.equals(Message.EXISTED_NAME)){
            throw new BadRequestException(Message.EXISTED_NAME);
        }
        MenuItemResponse updatedMenuItem=menuItemService.update(newMenuItem,id);
        if (updatedMenuItem==null){
           throw new NotFoundException(id);
        };
        return updatedMenuItem;
    }
    @DeleteMapping(Path.ID)
    public boolean deleteMenuItem(@PathVariable Long id) {
        String message=menuItemService.delete(id);
        if(message.equals(Message.NOT_FOUND)){
            throw new NotFoundException(id);
        }
        if(message.equals(Message.CAN_NOT_DELETE)){
            throw new BadRequestException(message);
        }
        return true;
    }

    @GetMapping(Path.SEARCH)
    public List<MenuItemResponse> searchMenuItems(@Param("keyword") String keyword, Pageable pageable){
        return menuItemService.search(keyword,pageable);
    }
}
