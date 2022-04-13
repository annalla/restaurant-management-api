package com.restaurant.restaurantmanagementapi.mapper;

import com.restaurant.restaurantmanagementapi.dto.BillResponse;
import com.restaurant.restaurantmanagementapi.model.Bill;

/**
 * The GenericMapper abstract class contains method transform from entityToDTO
 *
 */
public abstract class GenericMapper<T> {
    public abstract T entityToDTO (T entity) ;
}
