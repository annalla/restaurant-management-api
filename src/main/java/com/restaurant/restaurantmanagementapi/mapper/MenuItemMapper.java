package com.restaurant.restaurantmanagementapi.mapper;

import com.restaurant.restaurantmanagementapi.enums.Status;
import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.dto.MenuItemResponse;

/**
 * The MenuItemMapper class contains method transform from MenuItem to MenuItemResponse
 *
 * @see MenuItem
 * @see MenuItemResponse
 */
public class MenuItemMapper {
    public static MenuItemResponse toMenuItemResponse(MenuItem menuItem) {
        Status status = menuItem.getStatus() ? Status.Active : Status.Inactive;
        return new MenuItemResponse(menuItem.getId(), menuItem.getName(), menuItem.getDescription(), menuItem.getImage(), menuItem.getNote(), menuItem.getPrice(), status);
    }
}
