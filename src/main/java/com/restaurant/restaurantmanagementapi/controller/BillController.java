package com.restaurant.restaurantmanagementapi.controller;

import com.restaurant.restaurantmanagementapi.dto.*;
import com.restaurant.restaurantmanagementapi.exception.InvalidRequestException;
import com.restaurant.restaurantmanagementapi.exception.NotFoundException;
import com.restaurant.restaurantmanagementapi.exception.RestaurantException;
import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.service.BillService;
import com.restaurant.restaurantmanagementapi.utils.Message;
import com.restaurant.restaurantmanagementapi.utils.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The BillController class handle routing (CRUD) related bill order
 */
@RestController
@Validated
@RequestMapping(path = Path.BILL)
public class BillController {
    @Autowired
    private BillService billService;

    /**
     * Get information of bill by id provided. The result is BillResponse object, if id not exist throws exception
     *
     * @param id id of bill need to get
     * @return BillResponse object or throws NotFoundException when id not exist
     * @throws NotFoundException
     */
    @GetMapping(Path.ID)
    public BillResponse getBillById(@PathVariable Long id) throws RestaurantException {
       return billService.getById(id);
    }

    /**
     * Get information of all bills. The result is List of BillResponse with params size (maximum of each page), page (number of page)
     *
     * @param pageable params of pagination include size, page
     * @return List<BillResponse>
     */

    @GetMapping()
    public List<BillResponse> getAllBills(Pageable pageable) throws RestaurantException {
        return billService.getAll(pageable);
    }

    /**
     * Add new bill. The result is Bill added or throws exception when request is not valid
     *
     * @param billItemRequests List of BillItemRequest contains information of bill item (menuItemId, quantity)
     * @return Bill if success
     * @throws InvalidRequestException when billItemRequests is not valid
     */
    @PostMapping()
    public BillResponse addBill(@RequestBody @NotEmpty(message = "Bill item list cannot be empty.")List<@Valid BillItemRequest> billItemRequests) throws RestaurantException {
        validateMenuItemIdInBill(billItemRequests);
        return billService.add(billItemRequests);
    }
    /**
     * Validate MenuItem request. The result throws exception InvalidRequestException when menu item id not existed
     * @param billItems List<BillItemRequest>
     */
    private void validateMenuItemIdInBill( List<BillItemRequest> billItems) throws RestaurantException {
        for (BillItemRequest billItem : billItems) {
            if(!billService.isExistedMenuItemIdInBillRequest(billItem.getMenuItemId())){
                throw new InvalidRequestException(String.format(Message.NOT_EXISTED_MENU_ITEM,billItem.getMenuItemId()));
            }
        }
    }
    /**
     * Update a bill with id. The result is BillResponse if success, throws BadRequestException with message otherwise
     *
     * @param billItemRequests List of BillItemRequest need to update
     * @param id               the id of bill
     * @return BillResponse if success
     * @throws InvalidRequestException when billItemRequests is not valid
     */
    @PutMapping(Path.ID)
    public BillResponse updateBill(@RequestBody @NotEmpty(message = "Bill item list cannot be empty.")List<@Valid BillItemRequest> billItemRequests, @PathVariable Long id) throws RestaurantException {
        validateMenuItemIdInBill(billItemRequests);
        return billService.update(billItemRequests, id);
    }

    /**
     * Delete a bill with id. the result is true if success, or throws NotFoundException when id not exist
     *
     * @param id the id of bill
     * @return true if success.
     * @throws NotFoundException when id not exist
     */
    @DeleteMapping(Path.ID)
    public String deleteBill(@PathVariable Long id) throws RestaurantException {
        if (!billService.delete(id)) {
            throw new NotFoundException(String.format(Message.NOT_EXISTED_BILL,id));
        }
        return Message.SUCCESS;
    }

    /**
     * Add list of bill items in existed bill. The result is  BillResponse object if success,
     * otherwise throws BadRequestException when billItemsRequest is not valid or
     * NotFoundException when id not exist
     *
     * @param id              id of bill
     * @param billItemRequest List of BillItemRequest
     * @return BillResponse object if success
     * @throws InvalidRequestException when billItemsRequest is not valid
     * @throws NotFoundException   when id not exist
     */
    @PostMapping(Path.BILL_ITEM)
    public BillResponse addBillItem(@PathVariable Long id, @RequestBody @NotEmpty(message = "Bill item list cannot be empty.")List<@Valid BillItemRequest> billItemRequest) throws RestaurantException  {
        validateMenuItemIdInBill(billItemRequest);
        return  billService.addBillItem(id, billItemRequest);
    }
    /**
     * Update list of bill items in existed bill. The result is  BillResponse object if success,
     * otherwise throws BadRequestException when billItemsRequest is not valid or
     * NotFoundException when id not exist
     *
     * @param id              id of bill
     * @param billItemRequest List of BillItemUpdateRequest
     * @return BillResponse object if success
     * @throws InvalidRequestException when billItemsRequest is not valid
     * @throws NotFoundException   when id not exist
     */
    @PutMapping(Path.BILL_ITEM)
    public BillResponse updateBillItem(@PathVariable Long id, @RequestBody @NotEmpty(message = "Bill item list cannot be empty.") List<@Valid BillItemUpdateRequest> billItemRequest) throws RestaurantException  {
        validateBillItemIdInUpdateBill(billItemRequest,id);
        return billService.updateBillItem(id, billItemRequest);
    }

    /**
     * Validate bill item id in Bill Item list request
     * @param billItems list bill item update request
     * @param billId id of bill
     * @throws RestaurantException when bill items id not exist in bill
     */
    private void validateBillItemIdInUpdateBill( List<BillItemUpdateRequest> billItems,Long billId) throws RestaurantException {
        for (BillItemUpdateRequest billItem : billItems) {
            if(!billService.isExistedBillItemIdInBillRequest(billItem.getId(),billId)){
                throw new InvalidRequestException(String.format(Message.NOT_EXISTED_BILL_ITEM_IN_BILL,billItem.getId()));
            }
        }
    }

    /**
     * Delete list of bill items in existed bill.The result is  BillResponse object if success,
     * otherwise throws BadRequestException when billItemsRequest is not valid or
     * NotFoundException when id not exist
     *
     * @param id        id of bill
     * @param billItems List of BillItemRequest
     * @return BillResponse object if success
     * @throws InvalidRequestException when billItemsRequest is not valid
     * @throws NotFoundException   when id not exist
     */
    @DeleteMapping(Path.BILL_ITEM)
    public BillResponse deleteBillItem(@PathVariable Long id, @RequestBody @NotEmpty(message = "Bill item delete list cannot be empty.")List<@Valid BillItemDeleteRequest> billItems) throws RestaurantException  {
        validateBillItemIdInBill(billItems,id);
        BillResponse updatedBill = billService.deleteBillItem(id, billItems);
        if (updatedBill == null) {
            throw new NotFoundException("");
        }
        return updatedBill;
    }
    /**
     * Validate bill item id in Bill Item list request
     * @param billItems list bill item delete request
     * @param billId id of bill
     * @throws RestaurantException when bill items id not exist in bill
     */
    private void validateBillItemIdInBill( List<BillItemDeleteRequest> billItems,Long billId) throws RestaurantException {
        for (BillItemDeleteRequest billItem : billItems) {
            if(!billService.isExistedBillItemIdInBillRequest(billItem.getId(),billId)){
                throw new InvalidRequestException(String.format(Message.NOT_EXISTED_BILL_ITEM_IN_BILL,billItem.getId()));
            }
        }
    }

}
