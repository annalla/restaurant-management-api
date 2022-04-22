package com.restaurant.restaurantmanagementapi.mapper;

import com.restaurant.restaurantmanagementapi.dto.BillResponse;
import com.restaurant.restaurantmanagementapi.dto.MenuItemResponse;
import com.restaurant.restaurantmanagementapi.model.Bill;
import com.restaurant.restaurantmanagementapi.model.MenuItem;

/**
 * The GenericMapper abstract class contains method transform from entityToDTO
 *
 */
public interface GenericMapper<T,T1,T2> {
    public T1 entityToDTO (T entity) ;

    public T dtoToEntity (T2 dto) ;
}
