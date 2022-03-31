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
import com.restaurant.restaurantmanagementapi.utils.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The BillController class handle routing (CRUD) related bill order
 */
@RestController
@RequestMapping(path = Path.BILL)
public class BillController {
    @Autowired
    BillService billService;

    /**
     * Get information of bill by id provided. The result is BillResponse object, if id not exist throws exception
     *
     * @param id id of bill need to get
     * @return BillResponse object or throws NotFoundException when id not exist
     * @throws NotFoundException
     */
    @GetMapping(Path.ID)
    BillResponse getBillById(@PathVariable Long id) {
        BillResponse billResponse = billService.getById(id);
        if (billResponse == null) {
            throw new NotFoundException(id);
        }
        return billResponse;
    }

    /**
     * Get information of all bills. The result is List of BillResponse with params size (maximum of each page), page (number of page)
     *
     * @param pageable params of pagination include size, page
     * @return List<BillResponse>
     */

    @GetMapping()
    List<BillResponse> getAllBills(Pageable pageable) {
        return billService.getAll(pageable);
    }

    /**
     * Add new bill. The result is Bill added or throws exception when request is not valid
     *
     * @param billItemRequests List of BillItemRequest contains information of bill item (menuItemId, quantity)
     * @return Bill if success
     * @throws BadRequestException when billItemRequests is not valid
     */
    @PostMapping()
    BillResponse addBill(@RequestBody List<BillItemRequest> billItemRequests) {
        String message = billService.checkBillItem(billItemRequests);
        if (!message.equals(Message.OK)) {
            throw new BadRequestException(message);
        }
        return billService.add(billItemRequests);
    }

    /**
     * Update a bill with id. The result is BillResponse if success, throws BadRequestException with message otherwise
     *
     * @param billItemRequests List of BillItemRequest need to update
     * @param id               the id of bill
     * @return BillResponse if success
     * @throws BadRequestException when billItemRequests is not valid
     */
    @PutMapping(Path.ID)
    BillResponse updateBill(@RequestBody List<BillItemRequest> billItemRequests, @PathVariable Long id) {
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

    /**
     * Delete a bill with id. the result is true if success, or throws NotFoundException when id not exist
     *
     * @param id the id of bill
     * @return true if success.
     * @throws NotFoundException when id not exist
     */
    @DeleteMapping(Path.ID)
    public boolean deleteBill(@PathVariable Long id) {
        if (!billService.delete(id)) {
            throw new NotFoundException(id);
        }
        return true;
    }

    /**
     * Add list of bill items in existed bill. The result is  BillResponse object if success,
     * otherwise throws BadRequestException when billItemsRequest is not valid or
     * NotFoundException when id not exist
     *
     * @param id              id of bill
     * @param billItemRequest List of BillItemRequest
     * @return BillResponse object if success
     * @throws BadRequestException when billItemsRequest is not valid
     * @throws NotFoundException   when id not exist
     */
    @PostMapping(Path.BILL_ITEM)
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

    /**
     * Delete list of bill items in existed bill.The result is  BillResponse object if success,
     * otherwise throws BadRequestException when billItemsRequest is not valid or
     * NotFoundException when id not exist
     *
     * @param id        id of bill
     * @param billItems List of BillItemRequest
     * @return BillResponse object if success
     * @throws BadRequestException when billItemsRequest is not valid
     * @throws NotFoundException   when id not exist
     */
    @DeleteMapping(Path.BILL_ITEM)
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
