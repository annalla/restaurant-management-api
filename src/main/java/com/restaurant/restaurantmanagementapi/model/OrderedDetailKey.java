package com.restaurant.restaurantmanagementapi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
@Getter
@Setter
@Embeddable
public class OrderedDetailKey implements Serializable {

    @Column(name = "bill_id")
    Long billId;

    @Column(name = "menu_item_id")
    Long menuItemId;
}
