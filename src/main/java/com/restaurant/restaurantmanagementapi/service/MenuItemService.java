package com.restaurant.restaurantmanagementapi.service;

import com.restaurant.restaurantmanagementapi.dto.MenuItemRequest;
import com.restaurant.restaurantmanagementapi.dto.MenuItemResponse;
import com.restaurant.restaurantmanagementapi.enums.MenuStatus;
import com.restaurant.restaurantmanagementapi.exception.DatabaseErrorException;
import com.restaurant.restaurantmanagementapi.exception.NotFoundException;
import com.restaurant.restaurantmanagementapi.exception.RestaurantException;
import com.restaurant.restaurantmanagementapi.mapper.MenuItemMapper;
import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.repository.MenuItemRepository;
import com.restaurant.restaurantmanagementapi.utils.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The MenuItemService class handle business login related menu item
 */
@Component
public class MenuItemService {
    private static final Logger log = LoggerFactory.getLogger(MenuItemService.class);

    @Autowired
    MenuItemRepository menuItemRepository;
    @Autowired
    private MenuItemMapper menuItemMapper;

    /**
     * Check name of menu item is existed or not. The result is true if existed, false otherwise
     *
     * @param name name of menu item
     * @return true if exited, false otherwise
     */
    public boolean checkExistedName(String name) throws RestaurantException {
        try {
            return menuItemRepository.findByName(name).isPresent();
        } catch(DataAccessException e){
            log.error(String.valueOf(e));
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }

    }
    /**
     * Check new name of existed menu item is existed or not. The result is true if existed, false otherwise
     *
     * @param name name of menu item
     * @param id id of menu item
     * @return true if exited, false otherwise
     */
    public boolean checkExistedName(String name,Long id) throws RestaurantException{
        try {
            return menuItemRepository.findByName(name,id).isPresent();
        } catch(DataAccessException e){
            log.error(String.valueOf(e));
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }
    }

    /**
     * Get menu item by id. The result is  MenuItemResponse object if existed, null otherwise
     *
     * @param id id of menu item
     * @return MenuItemResponse object if existed, null otherwise
     */
    public MenuItemResponse getById(Long id) throws RestaurantException {
        try {
            return menuItemRepository.findById(id).map(menuItem -> {
                return menuItemMapper.entityToDTO(menuItem);
            }).orElseThrow(()->new NotFoundException(String.format(Message.NOT_EXISTED_MENU_ITEM,id)));
        }
        catch(DataAccessException e){
            log.error(String.valueOf(e));
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }
    }

    /**
     * add menu item. The result is MenuItemResponse object if success
     *
     * @param menuItem information of MenuItemRequest
     * @return MenuItemResponse object if success
     */
    public MenuItemResponse add(MenuItemRequest menuItem) throws RestaurantException  {
        MenuItem newMenuItem=  menuItemMapper.dtoToEntity(menuItem);
        newMenuItem.setStatus(true);
        try {
            return menuItemMapper.entityToDTO(menuItemRepository.save(newMenuItem));
        }
        catch(DataAccessException e){
            log.error(String.valueOf(e));
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }
    }

    /**
     * Update an existed MenuItem. The result is MenuItemResponse object if success, null if id not existed
     *
     * @param menuItem MenuItemRequest
     * @param id          id of menu item
     * @return MenuItemResponse object if success, null if id not existed
     */
    public MenuItemResponse update(MenuItemRequest menuItem, Long id) throws RestaurantException {
        MenuItem newMenuItem= menuItemMapper.dtoToEntity(menuItem);
        newMenuItem.setId(id);
        try {
            MenuItem updatedMenuItem = menuItemRepository.save(newMenuItem);
            return menuItemMapper.entityToDTO(updatedMenuItem);
        }
        catch(DataAccessException e){
            log.error(String.valueOf(e));
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }
    }
    /**
     * Get all menu items. The result is list of MenuItemResponse
     *
     * @param pageable params of pagination size, page
     * @return list of MenuItemResponse
     */
    public List<MenuItemResponse> getAll(Pageable pageable) throws RestaurantException  {
        try {
            return menuItemRepository.findAll(pageable).getContent().stream().map(menuItem ->  menuItemMapper.entityToDTO(menuItem)).collect(Collectors.toList());
        }
        catch(DataAccessException e){
            log.error(String.valueOf(e));
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }
    }

    /**
     * Search all menu items with title, description matching keyword. The result is list of MenuItemResponse
     *
     * @param keyword  String of keyword
     * @param pageable params of pagination size, page
     * @return list of MenuItemResponse
     */
    public List<MenuItemResponse> search(String keyword,String filter, Pageable pageable) throws RestaurantException  {
       Boolean status=null;
        if(filter!=null && filter.toLowerCase().equals(MenuStatus.ACTIVE.toString().toLowerCase())){
            status=true;
        }
        else  if(filter!=null && filter.toLowerCase().equals(MenuStatus.ACTIVE.toString().toLowerCase())){
            status=false;
        }
        try {
            return status==null?menuItemRepository.search(keyword, pageable).getContent().stream().map(menuItem ->
                    menuItemMapper.entityToDTO(menuItem)).collect(Collectors.toList()):menuItemRepository.searchWithFilter(keyword,status, pageable).getContent().stream().map(menuItem -> menuItemMapper.entityToDTO(menuItem)).collect(Collectors.toList());
        }
        catch(DataAccessException e){
            log.error(String.valueOf(e));
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }

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
    public boolean delete(Long id) throws RestaurantException {
        try {
            menuItemRepository.deleteById(id);
        }
        catch(EmptyResultDataAccessException e){
            throw new NotFoundException(String.format(Message.NOT_EXISTED_MENU_ITEM,id));
        }
        catch(DataIntegrityViolationException e){
            MenuItem existedMenuItem= menuItemRepository.findActiveItemById(id).get();
            existedMenuItem.setStatus(false);
            return false;
        }
        catch(DataAccessException e){
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }
        return true;
    }

    /**
     * Find MenuItem by id. The result is Optional<MenuItem>
     * @param id id of MenuItem
     * @return Optional<MenuItem>
     */
    public Optional<MenuItem> findMenuItemById(Long id) throws RestaurantException {
        try {
            return menuItemRepository.findActiveItemById(id);
        }
        catch(DataAccessException e){
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }
    }
}
