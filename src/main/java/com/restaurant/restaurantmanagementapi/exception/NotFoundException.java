package com.restaurant.restaurantmanagementapi.exception;

import com.restaurant.restaurantmanagementapi.utils.Message;

/**
 * The NotFoundException handle httpStatus 404: Not Found
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super(String.format(Message.NOT_FOUND, id));
    }
}