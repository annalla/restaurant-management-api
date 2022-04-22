package com.restaurant.restaurantmanagementapi.controller;

import com.restaurant.restaurantmanagementapi.dto.MenuItemRequest;
import com.restaurant.restaurantmanagementapi.dto.MenuItemResponse;
import com.restaurant.restaurantmanagementapi.exception.DatabaseErrorException;
import com.restaurant.restaurantmanagementapi.exception.RestaurantException;
import com.restaurant.restaurantmanagementapi.exception.InvalidRequestException;
import com.restaurant.restaurantmanagementapi.service.MenuItemService;
import com.restaurant.restaurantmanagementapi.utils.Message;
import com.restaurant.restaurantmanagementapi.exception.NotFoundException;
import com.restaurant.restaurantmanagementapi.utils.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

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
     * @throws DatabaseErrorException when data access failed
     */
    @GetMapping(Path.ID)
    public MenuItemResponse getMenuItemById(@PathVariable("id") Long id) throws RestaurantException {
        return menuItemService.getById(id);
    }

    /**
     * Get information of all menu items. The result is List of MenuItemResponse with params size (maximum of each page), page (number of page)
     *
     * @param pageable params of pagination include size (maximum of each page), page (number of page)
     * @return List of MenuItemResponse
     */
    @GetMapping()
    public List<MenuItemResponse> getMenuItems(Pageable pageable) throws RestaurantException {
        return menuItemService.getAll(pageable);
    }

    /**
     * Add a menu item. The result is MenuItemResponse object if success or throws BadRequestException newMenuItem is not valid
     *
     * @param newMenuItem information of MenuItem (name, description, image, note, price)
     * @return MenuItemResponse if success
     * @throws InvalidRequestException newMenuItem is not valid
     */
    @PostMapping()
    public MenuItemResponse addMenuItem(@RequestBody MenuItemRequest newMenuItem) throws RestaurantException {

        Map.Entry<Boolean,String> result = isValidMenuItemRequest(newMenuItem);
        if (result.getKey()) {
            return menuItemService.add(newMenuItem);
        }
        throw new InvalidRequestException(result.getValue());
    }

    /**
     * Validate MenuItem request. The result is boolean and message
     * @param newMenuItem MenuItem
     * @return Map.Entry<Boolean,String>  true and Message.OK,
     * name is empty return false,Message.EMPTY_MENU_ITEM_NAME
     * price is negative return false,Message.NEGATIVE_PRICE
     */
    private Map.Entry<Boolean,String> isValidMenuItemRequest(MenuItemRequest newMenuItem) throws RestaurantException {
        if (newMenuItem.getName() == null || newMenuItem.getName().length() == 0) {
            return new AbstractMap.SimpleEntry<Boolean,String>(false, Message.EMPTY_MENU_ITEM_NAME);
        }
        if(newMenuItem.getPrice()<0){
            return new AbstractMap.SimpleEntry<Boolean,String>(false, Message.NEGATIVE_PRICE);
        }
        if(menuItemService.checkExistedName(newMenuItem.getName())){
            return new AbstractMap.SimpleEntry<Boolean,String>(false, Message.EXISTED_MENU_ITEM_NAME);
        }
        return new AbstractMap.SimpleEntry<Boolean,String>(true, Message.OK);
    }

    /**
     * Update existed menu item with id. The result is MenuItemResponse object if success,
     * throws BadRequestException newMenuItem is not valid
     * or NotFoundException when id not exist
     *
     * @param newMenuItem information of MenuItem (name, description, image, note, price)
     * @param id          id of MenuItem
     * @return MenuItemResponse if success
     * @throws InvalidRequestException newMenuItem is not valid
     * @throws NotFoundException   when id not exist
     */
    @PutMapping(Path.ID)
    public MenuItemResponse updateMenuItem(@RequestBody MenuItemRequest newMenuItem, @PathVariable Long id) throws RestaurantException {
        Map.Entry<Boolean,String> result = isValidMenuItemRequest(newMenuItem);
        if (result.getKey()) {
            if(menuItemService.checkExistedName(newMenuItem.getName(),id)){
                throw new InvalidRequestException(Message.EXISTED_MENU_ITEM_NAME);
            }
            return menuItemService.update(newMenuItem, id);
        }
        throw new InvalidRequestException(result.getValue());
    }

    /**
     * Delete an existed MenuItem. The result is true if success,
     * throws NotFoundException when id not exist
     * or BadRequestException when menu item exist in bill, MenuItem status is inactive
     *
     * @param id
     * @return message Success if delete successfully, else Message.CAN_NOT_DELETE_MENU_ITEM when menu item exist in bill
     * @throws NotFoundException   when id not exist
     */
    @DeleteMapping(Path.ID)
    public String deleteMenuItem(@PathVariable Long id) throws RestaurantException {
        if (menuItemService.delete(id))
            return Message.SUCCESS;
        return Message.CAN_NOT_DELETE_MENU_ITEM;
    }

    /**
     * Search menu items matching title, description. The result is list of MenuItemResponse limit by params of pagination
     *
     * @param keyword  string of keyword
     * @param pageable params of pagination include size (maximum of each page), page (number of page)
     * @return list of MenuItemResponse
     */

    @GetMapping(Path.SEARCH)
    public List<MenuItemResponse> searchMenuItems(@Param("keyword") String keyword,@Param("filter") String filter, Pageable pageable) throws RestaurantException {
        return menuItemService.search(keyword,filter, pageable);
    }
}
