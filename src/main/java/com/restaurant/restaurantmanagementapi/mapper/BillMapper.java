package com.restaurant.restaurantmanagementapi.mapper;

import com.restaurant.restaurantmanagementapi.dto.BillItemResponse;
import com.restaurant.restaurantmanagementapi.dto.BillResponse;
import com.restaurant.restaurantmanagementapi.model.Bill;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The BillMapper class contains method transform from Bill to BillResponse
 *
 * @see Bill
 * @see BillResponse
 */
@Component
public class BillMapper implements GenericMapper<Bill,BillResponse,Object>{
    @Override
    public BillResponse entityToDTO(Bill entity) {
        BillResponse billResponse = new BillResponse(entity.getId(), entity.getOrderedTime(), entity.getTotal());
        if (entity.getBillItems() != null) {
            List<BillItemResponse> billItemResponses = entity.getBillItems().stream().map(billItem -> {
                return new BillItemResponse(billItem.getId(), billItem.getMenuItem().getName(), billItem.getPrice(), billItem.getQuantity());
            }).collect(Collectors.toList());
            billResponse.setBillItems(billItemResponses);
        }
        return billResponse;
    }

    @Override
    public Bill dtoToEntity(Object dto) {
        return null;
    }


//    public BillResponse entityToDTO(Bill bill) {
//        BillResponse billResponse = new BillResponse(bill.getId(), bill.getOrderedTime(), bill.getTotal());
//        if (bill.getBillItems() != null) {
//            List<BillItemResponse> billItemResponses = bill.getBillItems().stream().map(billItem -> {
//                return new BillItemResponse(billItem.getId(), billItem.getMenuItem().getName(), billItem.getPrice(), billItem.getQuantity());
//            }).collect(Collectors.toList());
//            billResponse.setBillItems(billItemResponses);
//        }
//        return billResponse;
//    }

}
