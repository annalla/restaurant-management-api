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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The BillController class handle routing (CRUD) related bill order
 */
@RestController
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
    public BillResponse addBill(@RequestBody List<BillItemRequest> billItemRequests) throws RestaurantException {
        Map.Entry<Boolean,String> result = isValidBillItemRequest(billItemRequests);
        if (result.getKey()) {
            return billService.add(billItemRequests);
        }
        throw new InvalidRequestException(result.getValue());
    }
    /**
     * Validate MenuItem request. The result is boolean and message
     * @param billItems List<BillItemRequest>
     * @return Map.Entry<Boolean,String>  true and Message.OK,
     * name is empty return false,Message.EMPTY_MENU_ITEM_NAME
     * price is negative return false,Message.NEGATIVE_PRICE
     */
    private Map.Entry<Boolean,String> isValidBillItemRequest( List<BillItemRequest> billItems) throws RestaurantException {
        for (BillItemRequest billItem : billItems) {
            if (billItem.getMenuItemId() == null && billItem.getQuantity() == null) {
                return new AbstractMap.SimpleEntry<Boolean,String>(false, Message.EMPTY_BILL_ITEM);
            }
            if( billItem.getMenuItemId() == null){
                return new AbstractMap.SimpleEntry<Boolean,String>(false, Message.MISSING_FIELD_BILL_ITEM_ID);
            }
            if(billItem.getQuantity() == null){
                return new AbstractMap.SimpleEntry<Boolean,String>(false, Message.MISSING_FIELD_BILL_ITEM_QUANTITY);
            }
            if (billItem.getQuantity() < 0) {
                return new AbstractMap.SimpleEntry<Boolean,String>(false, Message.NEGATIVE_BILL_ITEM_QUANTITY);
            }
            if(!billService.isExistedMenuItemIdInBillRequest(billItem.getMenuItemId())){
                return new AbstractMap.SimpleEntry<Boolean,String>(false, String.format( Message.NOT_EXISTED_MENU_ITEM,billItem.getMenuItemId()));
            }
        }
        return new AbstractMap.SimpleEntry<Boolean,String>(true, Message.OK);
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
    public BillResponse updateBill(@RequestBody List<BillItemRequest> billItemRequests, @PathVariable Long id) throws RestaurantException {
//        String message = billService.checkBillItem(billItemRequests);
//        if (!message.equals(Message.OK)) {
//            throw new InvalidRequestException(message);
//        }
        BillResponse updatedBill = billService.update(billItemRequests, id);
        if (updatedBill == null) {
            throw new NotFoundException("");
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
    public BillResponse addBillItem(@PathVariable Long id, @RequestBody List<BillItemRequest> billItemRequest) throws RestaurantException  {
//        String message = billService.checkBillItem(billItemRequest);
//        if (!message.equals(Message.OK)) {
//            throw new InvalidRequestException(message);
//        }
        BillResponse updatedBill = billService.addBillItem(id, billItemRequest);
        if (updatedBill == null) {
            throw new NotFoundException("");
        }
        return updatedBill;
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
    public BillResponse updateBillItem(@PathVariable Long id, @RequestBody List<BillItemUpdateRequest> billItemRequest) throws RestaurantException  {
//        String message = billService.checkUpdateBillItemId(billItemRequest);
//        if (!message.equals(Message.OK)) {
//            throw new InvalidRequestException(message);
//        }
        BillResponse updatedBill = billService.updateBillItem(id, billItemRequest);
        if (updatedBill == null) {
            throw new NotFoundException("");
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
     * @throws InvalidRequestException when billItemsRequest is not valid
     * @throws NotFoundException   when id not exist
     */
    @DeleteMapping(Path.BILL_ITEM)
    public BillResponse deleteBillItem(@PathVariable Long id, @RequestBody List<BillItemDeleteRequest> billItems) throws RestaurantException  {
//        String message = billService.checkBillItemId(billItems);
//        if (!message.equals(Message.OK)) {
//            throw new InvalidRequestException(message);
//        }
        BillResponse updatedBill = billService.deleteBillItem(id, billItems);
        if (updatedBill == null) {
            throw new NotFoundException("");
        }
        return updatedBill;
    }


}
