package com.restaurant.restaurantmanagementapi.controller;

import com.restaurant.restaurantmanagementapi.model.Bill;
import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.repository.BillRepository;
import com.restaurant.restaurantmanagementapi.repository.MenuItemRepository;
import com.restaurant.restaurantmanagementapi.repository.OrderedDetailRepository;
import com.restaurant.restaurantmanagementapi.req_res_model.MenuItemRequest;
import com.restaurant.restaurantmanagementapi.utils.BadRequestException;
import com.restaurant.restaurantmanagementapi.utils.Message;
import com.restaurant.restaurantmanagementapi.utils.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/bill")
public class BillController {
    @Autowired
    BillRepository billRepository;
    @Autowired
    OrderedDetailRepository orderedDetailRepository;
    @Autowired
    MenuItemRepository menuItemRepository;
//    @PostMapping("")
//    Bill addBill(@RequestBody List<MenuItemRequest> menuItemRequestList) {
//        System.out.println(menuItemRequestList);
//        return null;
//    }
    @PostMapping("")
    Bill addBill(@RequestBody Bill bill) {
        bill.setTotal(0.0);
        bill.setOrderedTime(LocalDateTime.now());
        System.out.println("?1");

        System.out.println("?2");
//        bill.getOrderedDetails().stream().map(orderedDetail -> {
//            System.out.println("?3");
////            orderedDetail.setBill(newBill);
//            orderedDetail.setPrice(orderedDetail.getMenuItem().getPrice());
//           orderedDetailRepository.save(orderedDetail);
//           return null;
//        });
        bill.setTotal(3.5);
        return billRepository.save(bill);
    }
}
