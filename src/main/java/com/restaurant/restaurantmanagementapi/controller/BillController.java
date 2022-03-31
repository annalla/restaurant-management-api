package com.restaurant.restaurantmanagementapi.controller;

import com.restaurant.restaurantmanagementapi.dto.BillItemDeleteRequest;
import com.restaurant.restaurantmanagementapi.dto.BillItemRequest;
import com.restaurant.restaurantmanagementapi.dto.MenuItemResponse;
import com.restaurant.restaurantmanagementapi.exception.BadRequestException;
import com.restaurant.restaurantmanagementapi.exception.NotFoundException;
import com.restaurant.restaurantmanagementapi.model.Bill;
import com.restaurant.restaurantmanagementapi.dto.BillResponse;
import com.restaurant.restaurantmanagementapi.service.BillService;
import com.restaurant.restaurantmanagementapi.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/bill")
public class BillController<todo> {
    @Autowired
    BillService billService;

    @GetMapping("/{id}")
    BillResponse getBillById(@PathVariable Long id) {
        BillResponse billResponse = billService.getById(id);
        if (billResponse == null) {
            throw new NotFoundException(id);
        }
        return billResponse;
    }

    @GetMapping("")
    List<BillResponse> getBillById(Pageable pageable) {
        return billService.getAll(pageable);
    }

    @PostMapping("")
    BillResponse addBill(@RequestBody List<BillItemRequest> billItemRequests) {
        String message = billService.checkBillItem(billItemRequests);
        if (!message.equals(Message.OK)) {
            throw new BadRequestException(message);
        }
        return billService.add(billItemRequests);
    }

    @PutMapping("/{id}")
    BillResponse addBill(@RequestBody List<BillItemRequest> billItemRequests, @PathVariable Long id) {
        String message = billService.checkBillItem(billItemRequests);
        if (!message.equals(Message.OK)) {
            throw new BadRequestException(message);
        }
        BillResponse updatedBill = billService.update(billItemRequests, id);
        if (updatedBill == null) {
            throw new NotFoundException(id);
        }
        ;
        return updatedBill;
    }

    @DeleteMapping("/{id}")
    public boolean deleteBill(@PathVariable Long id) {
        if (!billService.delete(id)) {
            throw new NotFoundException(id);
        }
        return true;
    }

    @PostMapping("/{id}/bill-item")
    public BillResponse addBillItem(@PathVariable Long id, @RequestBody List<BillItemRequest> billItemRequest) {
        String message = billService.checkBillItem(billItemRequest);
        if (!message.equals(Message.OK)) {
            throw new BadRequestException(message);
        }
        BillResponse updatedBill = billService.addBillItem(id, billItemRequest);
        if (updatedBill == null) {
            throw new NotFoundException(id);
        }
        return updatedBill;
    }
    @DeleteMapping("/{id}/bill-item")
    public BillResponse deleteBillItem(@PathVariable Long id, @RequestBody List<BillItemDeleteRequest> billItems) {
        String message = billService.checkBillItemId(billItems);
        if (!message.equals(Message.OK)) {
            throw new BadRequestException(message);
        }
        BillResponse updatedBill = billService.deleteBillItem(id, billItems);
        if (updatedBill == null) {
            throw new NotFoundException(id);
        }
        return updatedBill;
    }


}
