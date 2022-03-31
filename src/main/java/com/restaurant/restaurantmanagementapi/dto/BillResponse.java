package com.restaurant.restaurantmanagementapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The BillResponse class wraps fields including id (type : Long), orderedTime (type : LocaleDateTime), total (type: Double)
 * and billItems(type : List<BillItemResponse>).
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillResponse {
    private Long id;
    private LocalDateTime orderedTime;
    private Double total;
    private List<BillItemResponse> billItems;

    public BillResponse(Long id, LocalDateTime orderedTime, Double total) {
        this.id = id;
        this.orderedTime = orderedTime;
        this.total = total;
    }
}
