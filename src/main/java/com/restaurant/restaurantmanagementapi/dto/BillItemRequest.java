package com.restaurant.restaurantmanagementapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The BillItemRequest wraps fields including menuItemId (type : Long) and quantity (type : Integer)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillItemRequest {
    private Long menuItemId;
    private Integer quantity;
}
