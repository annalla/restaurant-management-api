package com.restaurant.restaurantmanagementapi.utils;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}