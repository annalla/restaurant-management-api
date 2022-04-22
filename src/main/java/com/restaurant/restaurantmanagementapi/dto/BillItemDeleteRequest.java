package com.restaurant.restaurantmanagementapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * The BillItemDeleteRequest wraps field id (type : Long)
 */
@Getter
@Setter
public class BillItemDeleteRequest {
    @NotNull(message="Bill item id is mandatory")
    private Long id;
}
