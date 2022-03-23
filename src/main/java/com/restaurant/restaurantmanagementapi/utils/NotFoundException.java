package com.restaurant.restaurantmanagementapi.utils;

public class NotFoundException extends RuntimeException{
    public NotFoundException(Long id) {
        super("Could not find id = " + id);
    }
}
