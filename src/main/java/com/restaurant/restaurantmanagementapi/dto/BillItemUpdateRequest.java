package com.restaurant.restaurantmanagementapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The BillItemUpdateRequest wraps fields including billItemId (type : Long) and quantity (type : Integer)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillItemUpdateRequest {
    private Long id;
    private Integer quantity;
}
