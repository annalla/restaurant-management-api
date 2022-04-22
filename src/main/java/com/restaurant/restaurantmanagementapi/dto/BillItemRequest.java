package com.restaurant.restaurantmanagementapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * The BillItemRequest wraps fields including menuItemId (type : Long) and quantity (type : Integer)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillItemRequest {
    @NotNull(message="Menu item id is mandatory")
    private Long menuItemId;
    @NotNull(message="Quantity of menu item is mandatory")
    @Positive(message = "Quantity of menu item is positive")
    private Integer quantity;
}
