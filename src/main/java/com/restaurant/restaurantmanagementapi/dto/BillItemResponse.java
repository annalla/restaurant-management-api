package com.restaurant.restaurantmanagementapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The BillItemResponse wraps fields including id (type : Long), name (type : String), price (type : Double) and quantity (type : Integer)
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillItemResponse {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
}
