package com.restaurant.restaurantmanagementapi.dto;

import com.restaurant.restaurantmanagementapi.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The MenuItemResponse wraps fields including id (type : Long), name (type : String), description (type : String), image (type : String), note (type : String)
 * and price (type : Double) and quantity (type : Integer), status (type : Status)
 *
 * @see Status
 */
@Getter
@Setter
@AllArgsConstructor
public class MenuItemResponse {
    private Long id;
    private String name;
    private String description;
    private String image;
    private String note;
    private Double price;
    private Status status;
}
