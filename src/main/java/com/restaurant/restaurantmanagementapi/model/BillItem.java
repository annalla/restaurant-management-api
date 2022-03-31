package com.restaurant.restaurantmanagementapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * The BillItem class wraps information of bill item including id, billId, bill, menuItemId, menuItem
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bill_item")
public class BillItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bill_id")
    private Long billId;
    @Column(name = "menu_item_id")
    private Long menuItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private Bill bill;

    @ManyToOne()
    @JoinColumn(name = "menu_item_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private MenuItem menuItem;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

    public BillItem(Long billId, Long menuItemId, MenuItem menuItem, Integer quantity, Double price) {
        this.billId = billId;
        this.menuItemId = menuItemId;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.price = price;
    }


    @Override
    public String toString() {
        return "BillItem{" +
                "id=" + id +
                ", billId=" + billId +
                ", menuItemId=" + menuItemId +
                ", menuItem=" + menuItem +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
