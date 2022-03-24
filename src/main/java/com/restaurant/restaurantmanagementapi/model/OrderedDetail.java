package com.restaurant.restaurantmanagementapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ordered_detail")
public class OrderedDetail {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="bill_id", nullable=false)
    private Bill bill;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="menu_item_id", nullable=false)
    private MenuItem menuItem;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="price")
    private Double price;
}
