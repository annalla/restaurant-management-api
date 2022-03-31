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

/**
 * The MenuItemController class handle routing (CRUD, search) related menu item
 */
@RestController
@RequestMapping(path = Path.MENU_ITEM)
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    /**
     * Get information of a menu item with id. The result is MenuItemResponse object if id existed
     *
     * @param id id of menu item,
     * @return MenuItemResponse if id existed
     * @throws NotFoundException when id not exist
     */
    @GetMapping(Path.ID)
    public MenuItemResponse getMenuItemById(@PathVariable("id") Long id) {
        MenuItemResponse menuItemResponse = menuItemService.getById(id);
        if (menuItemResponse == null) {
            throw new NotFoundException(id);
        }
        return menuItemResponse;
    }

    /**
     * Get information of all menu items. The result is List of MenuItemResponse with params size (maximum of each page), page (number of page)
     *
     * @param pageable params of pagination include size (maximum of each page), page (number of page)
     * @return List of MenuItemResponse
     */
    @GetMapping()
    public List<MenuItemResponse> getMenuItems(Pageable pageable) {
        return menuItemService.getAll(pageable);
    }

    /**
     * Add a menu item. The result is MenuItemResponse object if success or throws BadRequestException newMenuItem is not valid
     *
     * @param newMenuItem information of MenuItem (name, description, image, note, price)
     * @return MenuItemResponse if success
     * @throws BadRequestException newMenuItem is not valid
     */
    @PostMapping()
    public MenuItemResponse addMenuItem(@RequestBody MenuItem newMenuItem) {
        String message = menuItemService.check(newMenuItem);
        if (!message.equals(Message.OK)) {
            throw new BadRequestException(message);
        }
        return menuItemService.add(newMenuItem);
    }

    /**
     * Update existed menu item with id. The result is MenuItemResponse object if success,
     * throws BadRequestException newMenuItem is not valid
     * or NotFoundException when id not exist
     *
     * @param newMenuItem information of MenuItem (name, description, image, note, price)
     * @param id          id of MenuItem
     * @return MenuItemResponse if success
     * @throws BadRequestException newMenuItem is not valid
     * @throws NotFoundException   when id not exist
     */
    @PutMapping(Path.ID)
    public MenuItemResponse updateMenuItem(@RequestBody MenuItem newMenuItem, @PathVariable Long id) {
        String message = menuItemService.check(newMenuItem);
        if (message.equals(Message.EXISTED_NAME)) {
            throw new BadRequestException(Message.EXISTED_NAME);
        }
        MenuItemResponse updatedMenuItem = menuItemService.update(newMenuItem, id);
        if (updatedMenuItem == null) {
            throw new NotFoundException(id);
        }
        ;
        return updatedMenuItem;
    }

    /**
     * Delete an existed MenuItem. The result is true if success,
     * throws NotFoundException when id not exist
     * or BadRequestException when menu item exist in bill, MenuItem status is inactive
     *
     * @param id
     * @return true if success
     * @throws NotFoundException   when id not exist
     * @throws BadRequestException when menu item exist in bill
     */
    @DeleteMapping(Path.ID)
    public boolean deleteMenuItem(@PathVariable Long id) {
        String message = menuItemService.delete(id);
        if (message.equals(Message.NOT_FOUND)) {
            throw new NotFoundException(id);
        }
        if (message.equals(Message.CAN_NOT_DELETE)) {
            throw new BadRequestException(message);
        }
        return true;
    }

    /**
     * Search menu items matching title, description. The result is list of MenuItemResponse limit by params of pagination
     *
     * @param keyword  string of keyword
     * @param pageable params of pagination include size (maximum of each page), page (number of page)
     * @return list of MenuItemResponse
     */

    @GetMapping(Path.SEARCH)
    public List<MenuItemResponse> searchMenuItems(@Param("keyword") String keyword, Pageable pageable) {
        return menuItemService.search(keyword, pageable);
    }
}
