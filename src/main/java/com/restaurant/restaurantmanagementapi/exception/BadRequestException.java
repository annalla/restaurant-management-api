package com.restaurant.restaurantmanagementapi.exception;

/**
 * The NotFoundException handle httpStatus 400: Bad Request
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
