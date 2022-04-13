package com.restaurant.restaurantmanagementapi.service;

import com.restaurant.restaurantmanagementapi.dto.MenuItemResponse;
import com.restaurant.restaurantmanagementapi.mapper.MenuItemMapper;
import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.repository.BillItemRepository;
import com.restaurant.restaurantmanagementapi.repository.MenuItemRepository;
import com.restaurant.restaurantmanagementapi.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The MenuItemService class handle business login related menu item
 */
@Component
public class MenuItemService {
    @Autowired
    MenuItemRepository menuItemRepository;
    @Autowired
    BillItemRepository billItemRepository;
    @Autowired
    private MenuItemMapper menuItemMapper;

    /**
     * Check name of menu item is exited or not. The result is true if exited, false otherwise
     *
     * @param name name of menu item
     * @return true if exited, false otherwise
     */
    public boolean checkExistedName(String name) {
        return menuItemRepository.findByName(name).isPresent();
    }

    /**
     * Get menu item by id. The result is  MenuItemResponse object if existed, null otherwise
     *
     * @param id id of menu item
     * @return MenuItemResponse object if existed, null otherwise
     */
    public MenuItemResponse getById(Long id) {
        return menuItemRepository.findById(id).map(menuItem -> {
            return (MenuItemResponse) menuItemMapper.entityToDTO(menuItem);
        }).orElse(null);
    }

    /**
     * add menu item. The result is MenuItemResponse object if success
     *
     * @param newMenuItem information of MenuItem
     * @return MenuItemResponse object if success
     */
    public MenuItemResponse add(MenuItem newMenuItem) {
        newMenuItem.setStatus(true);
        return (MenuItemResponse) menuItemMapper.entityToDTO(menuItemRepository.save(newMenuItem));
    }

    /**
     * Update an existed MenuItem. The result is MenuItemResponse object if success, null if id not existed
     *
     * @param newMenuItem MenuItem
     * @param id          id of menu item
     * @return MenuItemResponse object if success, null if id not existed
     */
    public MenuItemResponse update(MenuItem newMenuItem, Long id) {
        return menuItemRepository.findActiveItemById(id).map(existedMenuItem -> {
            if (newMenuItem.getName() != null && newMenuItem.getName().length() != 0) {
                existedMenuItem.setName(newMenuItem.getName());
            }
            if (newMenuItem.getDescription() != null && newMenuItem.getDescription().length() != 0) {
                existedMenuItem.setDescription(newMenuItem.getDescription());
            }
            if (newMenuItem.getImage() != null && newMenuItem.getImage().length() != 0) {
                existedMenuItem.setImage(newMenuItem.getImage());
            }
            if (newMenuItem.getPrice() > 0) {
                existedMenuItem.setPrice(newMenuItem.getPrice());
            }
            return (MenuItemResponse) menuItemMapper.entityToDTO(menuItemRepository.save(existedMenuItem));
        }).orElse(null);
    }

    /**
     * Check information of MenuItem is valid or not. The result is a String
     * Message.OK if valid, Message.EMPTY_NAME if name is empty, Message.EXISTED_NAME if existed name
     *
     * @param newMenuItem
     * @return String Message.OK if valid, Message.EMPTY_NAME if name is empty, Message.EXISTED_NAME if existed name
     */
    public String check(MenuItem newMenuItem) {
        if (newMenuItem.getName() == null || newMenuItem.getName().length() == 0) {
            return Message.EMPTY_NAME;
        }
        if (checkExistedName(newMenuItem.getName())) {
            return Message.EXISTED_NAME;
        }
        return Message.OK;
    }

    /**
     * Get all menu items. The result is list of MenuItemResponse
     *
     * @param pageable params of pagination size, page
     * @return list of MenuItemResponse
     */
    public List<MenuItemResponse> getAll(Pageable pageable) {
        return menuItemRepository.findAll(pageable).getContent().stream().map(menuItem -> (MenuItemResponse) menuItemMapper.entityToDTO(menuItem)).collect(Collectors.toList());
    }

    /**
     * Search all menu items with title, description matching keyword. The result is list of MenuItemResponse
     *
     * @param keyword  String of keyword
     * @param pageable params of pagination size, page
     * @return list of MenuItemResponse
     */
    public List<MenuItemResponse> search(String keyword, Pageable pageable) {
        return menuItemRepository.search(keyword, pageable).getContent().stream().map(menuItem -> (MenuItemResponse) menuItemMapper.entityToDTO(menuItem)).collect(Collectors.toList());
    }

    /**
     * Delete menu item with id. The result is
     * String Message.OK id success,
     * Message.NOT_FOUND if id not exist,
     * Message.CAN_NOT_DELETE if menu item existed in bill and status is set Inactive
     *
     * @param id id of menu item
     * @return String Message.OK id success, Message.NOT_FOUND if id not exist, Message.CAN_NOT_DELETE if menu item existed in bill and status is set Inactive
     */
    public String delete(Long id) {
        return menuItemRepository.findActiveItemById(id).map(existedMenuItem -> {
            if (billItemRepository.findByMenuItemId(id).size() != 0) {
                existedMenuItem.setStatus(false);
                menuItemRepository.save(existedMenuItem);
                return Message.CAN_NOT_DELETE;
            }
            menuItemRepository.delete(existedMenuItem);
            return Message.OK;
        }).orElse(Message.NOT_FOUND);
    }

    /**
     * Find MenuItem by id. The result is Optional<MenuItem>
     * @param id id of MenuItem
     * @return Optional<MenuItem>
     */
    public Optional<MenuItem> findMenuItemById(Long id){
        return menuItemRepository.findActiveItemById(id);
    }
}
