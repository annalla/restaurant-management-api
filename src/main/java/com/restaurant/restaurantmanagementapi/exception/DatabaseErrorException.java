package com.restaurant.restaurantmanagementapi.exception;

import java.util.function.Supplier;

public class DatabaseErrorException extends RestaurantException {
    public DatabaseErrorException(String message) {
        super(message);
    }
}

