package com.restaurant.restaurantmanagementapi.mapper;

import com.restaurant.restaurantmanagementapi.dto.MenuItemRequest;
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
public class MenuItemMapper implements GenericMapper<MenuItem,MenuItemResponse,MenuItemRequest> {
    /**
     * Convert from MenuItem to MenuItemResponse
     * @param entity MenuItem
     * @return MenuItemResponse
     */
    @Override
    public MenuItemResponse entityToDTO(MenuItem entity) {
        MenuStatus status = entity.getStatus() ? MenuStatus.ACTIVE: MenuStatus.INACTIVE;
        return new MenuItemResponse(entity.getId(), entity.getName(), entity.getDescription(), entity.getImage(), entity.getNote(), entity.getPrice(), status);
    }

    /**
     * Convert from MenuItemRequest to MenuItem
     * @param dto MenuItemRequest
     * @return MenuItem
     */
    @Override
    public MenuItem dtoToEntity(MenuItemRequest dto) {
        if(dto.getImage()==null){
            dto.setImage("");
        }
        if(dto.getDescription()==null){
            dto.setDescription("");
        }
        if(dto.getNote()==null){
            dto.setNote("");
        }
        return new MenuItem(dto.getName(), dto.getDescription(), dto.getImage(), dto.getNote(), dto.getPrice());
    }



}
