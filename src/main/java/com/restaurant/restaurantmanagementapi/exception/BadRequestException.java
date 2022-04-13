package com.restaurant.restaurantmanagementapi.exception;

/**
 * The NotFoundException handle httpStatus 400: Bad Request
 */
public class BadRequestException extends RestaurantException{
    public BadRequestException(String message) {
        super(message);
    }
}
