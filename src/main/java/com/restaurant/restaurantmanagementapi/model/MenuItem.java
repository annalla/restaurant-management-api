package com.restaurant.restaurantmanagementapi.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
    private long id;
    private String name;
    private String description;
    private String image;
    private double price;

}
