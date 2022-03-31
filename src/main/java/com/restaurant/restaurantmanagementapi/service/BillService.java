package com.restaurant.restaurantmanagementapi.service;

import com.restaurant.restaurantmanagementapi.dto.BillItemDeleteRequest;
import com.restaurant.restaurantmanagementapi.dto.BillResponse;
import com.restaurant.restaurantmanagementapi.dto.BillItemRequest;
import com.restaurant.restaurantmanagementapi.mapper.BillMapper;
import com.restaurant.restaurantmanagementapi.model.Bill;
import com.restaurant.restaurantmanagementapi.model.BillItem;
import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.repository.BillRepository;
import com.restaurant.restaurantmanagementapi.repository.MenuItemRepository;
import com.restaurant.restaurantmanagementapi.repository.BillItemRepository;
import com.restaurant.restaurantmanagementapi.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The BillService class handle business login related bill
 */
@Service
public class BillService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillItemRepository billItemRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;

    public BillService() {
    }

    /**
     * Check list of BillItem is valid or not. The result is String Message.OK if valid, Message.NOT_EXISTED_MENU_ITEM if menu item id not exist,
     * Message.MISSING_FIELD when item miss menuItemId or quantity field,Message.NEGATIVE_QUANTITY when quantity is negative
     *
     * @param billItems list of BillItemRequest
     * @return String Message.OK if valid,
     * Message.NOT_EXISTED_MENU_ITEM if menu item id not exist,
     * Message.MISSING_FIELD when item miss menuItemId or quantity field,
     * Message.NEGATIVE_QUANTITY when quantity is negative
     */
    public String checkBillItem(List<BillItemRequest> billItems) {

        for (BillItemRequest billItem : billItems) {
            if (billItem.getMenuItemId() == null || billItem.getQuantity() == null) {
                return Message.MISSING_FIELD;
            }
            if (billItem.getQuantity() < 0) {
                return Message.NEGATIVE_QUANTITY;
            }
            if (billItem.getQuantity() > 0) {
                Optional<MenuItem> menuItem = menuItemRepository.findActiveItemById(billItem.getMenuItemId());
                if (menuItem.isEmpty()) {
                    return String.format(Message.NOT_EXISTED_MENU_ITEM, billItem.getMenuItemId());
                }
            }
        }
        return Message.OK;
    }

    /**
     * Check list of BillItemId is valid or not. The result is String Message.OK if valid, Message.NOT_EXISTED_MENU_ITEM if menu item id not exist,
     * Message.MISSING_FIELD when item miss menuItemId
     *
     * @param billItemIds list of BillItemDeleteRequest
     * @return String Message.OK if valid,
     * Message.NOT_EXISTED_MENU_ITEM if menu item id not exist,
     * Message.MISSING_FIELD when item miss menuItemId or quantity field,
     */
    public String checkBillItemId(List<BillItemDeleteRequest> billItemIds) {
        for (BillItemDeleteRequest billItem : billItemIds) {
            if (billItem.getId() == null) {
                return Message.MISSING_FIELD;
            }
            Optional<BillItem> menuItem = billItemRepository.findById(billItem.getId());
            if (menuItem.isEmpty()) {
                return String.format(Message.NOT_EXISTED_BILL_ITEM, billItem.getId());
            }
        }
        return Message.OK;
    }

    /**
     * Add a bill. The result is BillResponse object
     *
     * @param billItems list of BillItemRequest
     * @return BillResponse object
     */
    public BillResponse add(List<BillItemRequest> billItems) {
        Bill newBill = new Bill();
        newBill.setTotal(0.0);
        newBill.setOrderedTime(LocalDateTime.now());
        Long billId = billRepository.save(newBill).getId();
        updateBill(billItems, billId, newBill);
        Bill response = billRepository.save(newBill);
        return BillMapper.toBillResponse(response);
    }

    /**
     * Get information of bill by id. The result is  BillResponse object if id existed, null otherwise
     *
     * @param id id of bill
     * @return BillResponse object if id existed, null otherwise
     */
    public BillResponse getById(Long id) {
        return billRepository.findById(id).map(bill -> BillMapper.toBillResponse(bill)).orElse(null);
    }

    /**
     * Get informations of all bills, The result is list of BillResponse limit by pageable
     *
     * @param pageable params of pagination page, size
     * @return list of BillResponse
     */
    public List<BillResponse> getAll(Pageable pageable) {
        return billRepository.findAll(pageable).getContent().stream().map(bill -> BillMapper.toBillResponse(bill)).collect(Collectors.toList());
    }

    /**
     * Update a bill. The result is BillResponse object if id exist, null otherwise
     *
     * @param billItems list of BillItemRequest
     * @param id        id of bill
     * @return BillResponse object if id exist, null otherwise
     */
    public BillResponse update(List<BillItemRequest> billItems, Long id) {
        return billRepository.findById(id).map(bill -> {
            deleteBillItemWithBill(bill.getId());
            bill.clearAllBillItem();
            updateBill(billItems, id, bill);
            return BillMapper.toBillResponse(billRepository.save(bill));
        }).orElse(null);
    }

    /**
     * add billItem into bill and calculate total
     *
     * @param billItems list of BillItemRequest
     * @param id        id of bill
     * @param bill
     */
    private void updateBill(List<BillItemRequest> billItems, Long id, Bill bill) {
        double total = 0.0;
        for (BillItemRequest billItem : billItems) {
            Optional<MenuItem> menuItem = menuItemRepository.findActiveItemById(billItem.getMenuItemId());
            BillItem newBillItem = new BillItem(id, billItem.getMenuItemId(), menuItem.get(), billItem.getQuantity(), menuItem.get().getPrice());
            bill.addBillItem(newBillItem);
            total += (newBillItem.getPrice() * newBillItem.getQuantity());
        }
        bill.setTotal(Math.round(total * 100) / 100.0);
    }

    /**
     * Delete BillItem in bill.
     *
     * @param id id of bill
     */
    private void deleteBillItemWithBill(Long id) {
        List<BillItem> orders = billItemRepository.findByBillId(id);
        for (BillItem order : orders) {
            billItemRepository.delete(order);
        }
    }

    /**
     * Delete a bill. The result is true if delete successfully, false if id not exist
     *
     * @param id id of Bill
     * @return true if delete successfully, false if id not exist
     */
    public boolean delete(Long id) {
        return billRepository.findById(id).map(bill -> {
            billRepository.delete(bill);
            return true;
        }).orElse(false);
    }

    /**
     * Add list of bill item into existed bill. The result is BillResponse if success, null if billId not exist
     *
     * @param billId          id of bill
     * @param billItemRequest list of BillItemRequest
     * @return BillResponse if success, null if billId not exist
     */
    public BillResponse addBillItem(Long billId, List<BillItemRequest> billItemRequest) {
        return billRepository.findById(billId).map(bill -> {
            for (BillItemRequest billItem : billItemRequest) {
                Optional<MenuItem> menuItem = menuItemRepository.findActiveItemById(billItem.getMenuItemId());
                BillItem newBillItem = new BillItem(billId, billItem.getMenuItemId(), menuItem.get(), billItem.getQuantity(), menuItem.get().getPrice());
                bill.addBillItem(newBillItem);
            }
            double total = calculateTotalBill(billId);
            bill.setTotal(Math.round(total * 100) / 100.0);
            return BillMapper.toBillResponse(billRepository.save(bill));
        }).orElse(null);
    }

    /**
     * Calculate total of bill. The result is double object of total.
     *
     * @param id id of bill
     * @return double object of total bill.
     */
    public double calculateTotalBill(Long id) {
        return billItemRepository.findByBillId(id).stream().map(billItem -> billItem.getPrice() * billItem.getQuantity()).collect(Collectors.summingDouble(Double::doubleValue));
    }

    /**
     * Delete list of bill item into existed bill. The result is BillResponse if success, null if billId not exist
     *
     * @param billId    id of bill
     * @param billItems list of BillItemDeleteRequest
     * @return BillResponse if success, null if billId not exist
     */
    public BillResponse deleteBillItem(Long billId, List<BillItemDeleteRequest> billItems) {
        return billRepository.findById(billId).map(bill -> {
            for (BillItemDeleteRequest billItem : billItems) {
                billItemRepository.delete(billItemRepository.findById(billItem.getId()).get());
            }
            double total = calculateTotalBill(billId);
            bill.setTotal(Math.round(total * 100) / 100.0);
            return BillMapper.toBillResponse(billRepository.save(bill));
        }).orElse(null);
    }
}
