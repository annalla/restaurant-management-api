package com.restaurant.restaurantmanagementapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="ordered_time")
    private LocalDateTime orderedTime;
    @Column(name="total")
    private Double total;
    @OneToMany(mappedBy="bill",cascade=CascadeType.ALL)
    private final Set<BillItem> billItems=new HashSet<>();

    // No setter, only a getter which returns an immutable collection
    public Set<BillItem> getBillItems() {
        return Collections.unmodifiableSet(this.billItems);
    }
    public void addBillItem(BillItem billItem) {
        billItem.setBill(this);
        this.billItems.add(billItem);
    }
    public void removeBillItem(BillItem billItem) {
        this.billItems.remove(billItem);
    }
    public void clearAllBillItem() {
        this.billItems.clear();
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", orderedTime=" + orderedTime +
                ", total=" + total +
                ", billItems=" + billItems +
                '}';
    }
}
