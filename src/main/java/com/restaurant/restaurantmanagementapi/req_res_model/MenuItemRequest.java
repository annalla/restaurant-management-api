package com.restaurant.restaurantmanagementapi.req_res_model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemRequest {
    private Long menuItemId;
    private Integer quantity;
}
