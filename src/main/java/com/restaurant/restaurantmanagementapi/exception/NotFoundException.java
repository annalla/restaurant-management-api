package com.restaurant.restaurantmanagementapi.exception;

import com.restaurant.restaurantmanagementapi.utils.Message;

/**
 * The NotFoundException handle httpStatus 404: Not Found
 */
public class NotFoundException extends RestaurantException {
    public NotFoundException(String messageStatus) {
        super(messageStatus);
    }
}
