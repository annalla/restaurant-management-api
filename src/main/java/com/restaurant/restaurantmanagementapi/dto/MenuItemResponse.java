package com.restaurant.restaurantmanagementapi.dto;

import com.restaurant.restaurantmanagementapi.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


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
