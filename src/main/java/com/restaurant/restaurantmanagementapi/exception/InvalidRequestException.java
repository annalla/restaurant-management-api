package com.restaurant.restaurantmanagementapi.exception;

/**
 * The NotFoundException handle httpStatus 400: Bad Request
 */
public class InvalidRequestException extends RestaurantException{
    public InvalidRequestException(String message) {
        super(message);
    }
}
