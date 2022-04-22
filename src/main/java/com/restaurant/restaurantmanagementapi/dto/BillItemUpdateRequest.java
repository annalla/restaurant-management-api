package com.restaurant.restaurantmanagementapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * The BillItemUpdateRequest wraps fields including billItemId (type : Long) and quantity (type : Integer)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillItemUpdateRequest {
    @NotNull(message="Menu item id is mandatory")
    private Long id;
    @NotNull(message="Quantity of menu item is mandatory")
    @Positive(message = "Quantity of menu item is positive")
    private Integer quantity;
}
