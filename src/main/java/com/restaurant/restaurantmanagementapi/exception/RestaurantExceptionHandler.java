package com.restaurant.restaurantmanagementapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * The RestaurantExceptionHandler class is ControllerAdvice handle all exceptions in restaurant-api
 */
@ControllerAdvice
public class RestaurantExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public String ConstraintViolationException(
            ConstraintViolationException ex) {
        String[] errors=ex.getMessage().split(":");
        return errors[errors.length-1];
    }
    @ResponseBody
    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleInvalidRequestException(InvalidRequestException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handleNotFoundException(NotFoundException ex) {
        return ex.getMessage();
    }
    @ResponseBody
    @ExceptionHandler(DatabaseErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String handleDatabaseErrorException(DatabaseErrorException ex) {
        return ex.getMessage();
    }
}
