package com.restaurant.restaurantmanagementapi.dto;

import com.restaurant.restaurantmanagementapi.enums.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * The MenuItemRequest wraps fields including name (type : String), description (type : String), image (type : String), note (type : String)
 * and price (type : Double) and quantity (type : Integer)
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class MenuItemRequest {
    @NotEmpty(message = "Name menu item is mandatory")
    private String name;
    private String description;
    private String image;
    private String note;
    @NotEmpty(message ="Price is mandatory")
    @Positive(message="Price is positive")
    private Double price;
}
