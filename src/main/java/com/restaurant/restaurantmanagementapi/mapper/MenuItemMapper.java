package com.restaurant.restaurantmanagementapi.mapper;

import com.restaurant.restaurantmanagementapi.enums.MenuStatus;
import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.dto.MenuItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * The MenuItemMapper class contains method transform from MenuItem to MenuItemResponse
 *
 * @see MenuItem
 * @see MenuItemResponse
 */
@Component
public class MenuItemMapper<T> extends GenericMapper {
    /**
     * Convert from MenuItem to MenuItemResponse
     * @param entity MenuItem
     * @return MenuItemResponse
     */
    @Override
    public Object entityToDTO(Object entity) {
        MenuItem menuItem=(MenuItem)entity;
        MenuStatus status = menuItem.getStatus() ? MenuStatus.ACTIVE: MenuStatus.INACTIVE;
        return new MenuItemResponse(menuItem.getId(), menuItem.getName(), menuItem.getDescription(), menuItem.getImage(), menuItem.getNote(), menuItem.getPrice(), status);
    }

}
