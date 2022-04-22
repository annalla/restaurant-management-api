package com.restaurant.restaurantmanagementapi.dto;

import com.restaurant.restaurantmanagementapi.enums.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The MenuItemRequest wraps fields including name (type : String), description (type : String), image (type : String), note (type : String)
 * and price (type : Double) and quantity (type : Integer)
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class MenuItemRequest {
    private String name;
    private String description;
    private String image;
    private String note;
    private Double price;
}
