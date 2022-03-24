package com.restaurant.restaurantmanagementapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ordered_detail")
public class OrderedDetail {
    @EmbeddedId
    OrderedDetailKey id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @MapsId("billId")
    @JoinColumn(name="bill_id", nullable=false)
    private Bill bill;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("menuItemId")
    @JoinColumn(name="menu_item_id", nullable=false)
    private MenuItem menuItem;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="price")
    private Double price;
}
