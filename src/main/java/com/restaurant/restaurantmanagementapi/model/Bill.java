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
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<BillItem> billItems = new HashSet<>();
}
