package com.restaurant.restaurantmanagementapi.mapper;

import com.restaurant.restaurantmanagementapi.dto.BillItemResponse;
import com.restaurant.restaurantmanagementapi.dto.BillResponse;
import com.restaurant.restaurantmanagementapi.model.Bill;
import com.restaurant.restaurantmanagementapi.repository.BillItemRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The BillMapper class contains method transform from Bill to BillResponse
 *
 * @see Bill
 * @see BillResponse
 */
public class BillMapper {
    public static BillResponse toBillResponse(Bill bill) {
        BillResponse billResponse = new BillResponse(bill.getId(), bill.getOrderedTime(), bill.getTotal());
        if (bill.getBillItems() != null) {
            List<BillItemResponse> billItemResponses = bill.getBillItems().stream().map(billItem -> {
                return new BillItemResponse(billItem.getId(), billItem.getMenuItem().getName(), billItem.getPrice(), billItem.getQuantity());
            }).collect(Collectors.toList());
            billResponse.setBillItems(billItemResponses);
        }
        return billResponse;
    }
}
