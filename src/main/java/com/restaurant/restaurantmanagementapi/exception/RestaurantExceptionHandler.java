package com.restaurant.restaurantmanagementapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The RestaurantExceptionHandler class is ControllerAdvice handle all exceptions in restaurant-api
 */
@ControllerAdvice
public class RestaurantExceptionHandler {
    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleBadRequestException(BadRequestException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handleNotFoundException(NotFoundException ex) {
        return ex.getMessage();
    }
}
