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
import java.util.stream.Collectors;

/**
 * The bill class wraps information of bill including id, orderTime, total, list of BillItem
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;
    @Column(name = "total")
    private Double total;
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private final Set<BillItem> billItems = new HashSet<>();

    public Set<BillItem> getBillItems() {
        return Collections.unmodifiableSet(this.billItems);
    }

    /***
     * Add bill item into bill, if bill item existed, quantity will be oldQuantity + newQuantity
     *
     * @param billItem BillItem need to add
     * @see BillItem
     */
    public void addBillItem(BillItem billItem) {
        BillItem item = getExistedBillItem(billItem);
        if (item == null) {
            billItem.setBill(this);
            this.billItems.add(billItem);
        } else {
            item.setQuantity(billItem.getQuantity() + item.getQuantity());
        }
    }

    /**
     * Get existed BillItem in list of BillItem.
     *
     * @param billItem BillItem
     * @return BillItem if exist, null otherwise
     */
    private BillItem getExistedBillItem(BillItem billItem) {
        List<BillItem> items = billItems.stream().filter(billItem1 -> billItem1.getMenuItemId() == billItem.getMenuItemId()).collect(Collectors.toList());
        return items.size() == 0 ? null : items.get(0);
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
