package com.restaurant.restaurantmanagementapi.service;

import com.restaurant.restaurantmanagementapi.dto.BillItemDeleteRequest;
import com.restaurant.restaurantmanagementapi.dto.BillItemUpdateRequest;
import com.restaurant.restaurantmanagementapi.dto.BillResponse;
import com.restaurant.restaurantmanagementapi.dto.BillItemRequest;
import com.restaurant.restaurantmanagementapi.exception.DatabaseErrorException;
import com.restaurant.restaurantmanagementapi.exception.NotFoundException;
import com.restaurant.restaurantmanagementapi.exception.RestaurantException;
import com.restaurant.restaurantmanagementapi.mapper.BillMapper;
import com.restaurant.restaurantmanagementapi.model.Bill;
import com.restaurant.restaurantmanagementapi.model.BillItem;
import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.repository.BillRepository;
import com.restaurant.restaurantmanagementapi.repository.BillItemRepository;
import com.restaurant.restaurantmanagementapi.utils.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The BillService class handle business login related bill
 */
@Service
public class BillService {
    private static final Logger log = LoggerFactory.getLogger(BillService.class);

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillItemRepository billItemRepository;
    @Autowired
    private MenuItemService menuItemService;
    @Autowired
    private BillMapper billMapper;

    public BillService() {
    }
    public boolean isExistedMenuItemIdInBillRequest(Long id) throws RestaurantException {
        return menuItemService.findMenuItemById(id).isPresent();
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
//    public String checkBillItem(List<BillItemRequest> billItems) throws RestaurantException {
//
//        for (BillItemRequest billItem : billItems) {
//            if (billItem.getMenuItemId() == null || billItem.getQuantity() == null) {
//                return Message.EMPTY_BILL_ITEM;
//            }
//            if (billItem.getQuantity() < 0) {
//                return Message.EMPTY_BILL_ITEM;
//            }
//            if (billItem.getQuantity() > 0) {
//                Optional<MenuItem> menuItem = menuItemService.findMenuItemById(billItem.getMenuItemId());
//                if (menuItem.isEmpty()) {
//                    return String.format(Message.NOT_EXISTED_MENU_ITEM, billItem.getMenuItemId());
//                }
//            }
//        }
//        return Message.OK;
//    }

    /**
     * Check list of BillItemId is valid or not. The result is String Message.OK if valid, Message.NOT_EXISTED_MENU_ITEM if menu item id not exist,
     * Message.MISSING_FIELD when item miss menuItemId
     *
     * @param billItemIds list of BillItemDeleteRequest
     * @return String Message.OK if valid,
     * Message.NOT_EXISTED_MENU_ITEM if bill item id not exist,
     * Message.MISSING_FIELD when item miss billItemId,
     */
//    public String checkBillItemId(List<BillItemDeleteRequest> billItemIds) {
//        for (BillItemDeleteRequest billItem : billItemIds) {
//            if (billItem.getId() == null) {
//                return Message.EMPTY_BILL_ITEM;
//            }
//            Optional<BillItem> menuItem = billItemRepository.findById(billItem.getId());
//            if (menuItem.isEmpty()) {
//                return String.format(Message.NOT_EXISTED_BILL_ITEM, billItem.getId());
//            }
//        }
//        return Message.OK;
//    }
    /**
     * Check list of BillItemUpdateRequest is valid or not. The result is String Message.OK if valid, Message.NOT_EXISTED_MENU_ITEM if menu item id not exist,
     * Message.MISSING_FIELD when item miss id or quantity
     *
     * @param billItemIds list of BillItemDeleteRequest
     * @return String Message.OK if valid,
     * Message.NOT_EXISTED_MENU_ITEM if menu item id not exist,
     * Message.MISSING_FIELD when item miss billItemId or quantity field,
     * Message.NEGATIVE_QUANTITY when quantity is negative
     */
//    public String checkUpdateBillItemId(List<BillItemUpdateRequest> billItemIds) {
//        for (BillItemUpdateRequest billItem : billItemIds) {
//            if (billItem.getId() == null||billItem.getQuantity()==null) {
//                return Message.EMPTY_BILL_ITEM;
//            }
//            if (billItem.getQuantity() < 0) {
//                return Message.EMPTY_BILL_ITEM;
//            }
//            Optional<BillItem> menuItem = billItemRepository.findById(billItem.getId());
//            if (menuItem.isEmpty()) {
//                return String.format(Message.NOT_EXISTED_BILL_ITEM, billItem.getId());
//            }
//        }
//        return Message.OK;
//    }

    /**
     * Add a bill. The result is BillResponse object
     *
     * @param billItems list of BillItemRequest
     * @return BillResponse object
     */
    public BillResponse add(List<BillItemRequest> billItems) throws RestaurantException {
        Bill newBill = new Bill();
        newBill.setTotal(0.0);
        newBill.setOrderedTime(LocalDateTime.now());
        try {
            Long billId = billRepository.save(newBill).getId();
            addBillItemIntoBill(billItems, billId, newBill);
            double total=calculateTotalBill(newBill);
            newBill.setTotal(total);
            Bill response = billRepository.save(newBill);
            return billMapper.entityToDTO(response);
        }
        catch(DataAccessException e){
            log.error(String.valueOf(e));
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }
    }

    /**
     * Get information of bill by id. The result is  BillResponse object if id existed, null otherwise
     *
     * @param id id of bill
     * @return BillResponse object if id existed, null otherwise
     */
    public BillResponse getById(Long id) throws RestaurantException{
        try {
            return billRepository.findById(id).map(bill -> billMapper.entityToDTO(bill)).orElseThrow(()->new NotFoundException(String.format(Message.NOT_EXISTED_BILL,id)));
        } catch(DataAccessException e){
            log.error(String.valueOf(e));
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }

    }

    /**
     * Get informations of all bills, The result is list of BillResponse limit by pageable
     *
     * @param pageable params of pagination page, size
     * @return list of BillResponse
     */
    public List<BillResponse> getAll(Pageable pageable) throws RestaurantException{
        try {
            return billRepository.findAll(pageable).getContent().stream().map(bill -> billMapper.entityToDTO(bill)).collect(Collectors.toList());
        }
        catch(DataAccessException e){
            log.error(String.valueOf(e));
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }
    }

    /**
     * Update a bill. The result is BillResponse object if id exist, null otherwise
     *
     * @param billItems list of BillItemRequest
     * @param id        id of bill
     * @return BillResponse object if id exist, null otherwise
     */
    public BillResponse update(List<BillItemRequest> billItems, Long id) throws RestaurantException {
        try {
            Optional<Bill> bill=billRepository.findById(id);
            if(bill.isPresent()){
                Bill existedBill=bill.get();
                addBillItemIntoBill(billItems, id, existedBill);
                double total=calculateTotalBill(existedBill);
                existedBill.setTotal(total);
                Bill response = billRepository.save(existedBill);
                return billMapper.entityToDTO(response);
            }
        }
        catch(DataAccessException e){
            log.error(String.valueOf(e));
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }
        return add(billItems);
    }

    /**
     * add billItem into bill and calculate total
     *
     * @param billItems list of BillItemRequest
     * @param id        id of bill
     * @param bill
     */
    private void addBillItemIntoBill(List<BillItemRequest> billItems, Long id, Bill bill) throws RestaurantException {
        Set<BillItem> items=bill.getBillItems();
        for (BillItemRequest billItem : billItems) {
            Optional<MenuItem> menuItem = menuItemService.findMenuItemById(billItem.getMenuItemId());
            BillItem newBillItem = new BillItem(id, billItem.getMenuItemId(), menuItem.get(), billItem.getQuantity(), menuItem.get().getPrice());
            items=addBillItemInBill(newBillItem,bill,items);
        }
        bill.setBillItems(items);
    }

    /**
     * Delete a bill. The result is true if delete successfully, false if id not exist
     *
     * @param id id of Bill
     * @return true if delete successfully, false if id not exist
     */
    public boolean delete(Long id) throws RestaurantException {
        try {
            return billRepository.findById(id).map(bill -> {
                billRepository.delete(bill);
                return true;
            }).orElse(false);
        }
        catch(DataAccessException e){
            log.error(String.valueOf(e));
            throw new DatabaseErrorException(Message.FAILED_DATA_ACCESS);
        }

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
            Set<BillItem> items=bill.getBillItems();
            for (BillItemRequest billItem : billItemRequest) {
                Optional<MenuItem> menuItem = null;
                try {
                    menuItem = menuItemService.findMenuItemById(billItem.getMenuItemId());
                } catch (RestaurantException e) {
                    throw new RuntimeException(e);
                }
                BillItem newBillItem = new BillItem(billId, billItem.getMenuItemId(), menuItem.get(), billItem.getQuantity(), menuItem.get().getPrice());
                items=addBillItemInBill(newBillItem,bill,items);
            }
            bill.setBillItems(items);
            double total = calculateTotalBill(bill);
            bill.setTotal(Math.round(total * 100) / 100.0);
            return billMapper.entityToDTO(billRepository.save(bill));
        }).orElse(null);
    }
    /**
     * Update list of bill item into existed bill. The result is BillResponse if success, null if billId not exist
     *
     * @param billId          id of bill
     * @param  billItemUpdateRequest list of  billItemUpdateRequest
     * @return BillResponse if success, null if billId not exist
     */
    public BillResponse updateBillItem(Long billId, List<BillItemUpdateRequest> billItemUpdateRequest) {
        return billRepository.findById(billId).map(bill -> {
            Set<BillItem> items=bill.getBillItems();
            for (BillItemUpdateRequest billItem : billItemUpdateRequest) {
                Optional<BillItem> billItem1=billItemRepository.findById(billItem.getId());
                billItem1.get().setQuantity(billItem.getQuantity());
                items=updateBillItemInBill(billItem1.get(),bill,items);
            }
            bill.setBillItems(items);
            double total = calculateTotalBill(bill);
            bill.setTotal(Math.round(total * 100) / 100.0);
            return billMapper.entityToDTO(billRepository.save(bill));
        }).orElse(null);
    }

    /**
     * Calculate total of bill. The result is double object of total.
     *
     * @param bill id of bill
     * @return double object of total bill.
     */
    public double calculateTotalBill(Bill bill) {
        double total=0;
        for (BillItem billItem : bill.getBillItems()) {
            total+=(billItem.getPrice()*billItem.getQuantity());
        }
        return total;
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
            double total = calculateTotalBill(bill);
            bill.setTotal(Math.round(total * 100) / 100.0);
            return billMapper.entityToDTO(billRepository.save(bill));
        }).orElse(null);
    }

    /**
     * Find BillItem by menuItemId. The result is list of BillItem
     * @param id id of MenuItem
     * @return list of bill item
     */
    public List<BillItem> findBillItemByMenuItemId(Long id){
        return billItemRepository.findByMenuItemId(id);
    }
    /***
     * Add bill item into bill, if bill item existed, quantity will be oldQuantity + newQuantity
     *
     * @param billItem BillItem need to add
     * @see BillItem
     */
    private Set<BillItem>  addBillItemInBill(BillItem billItem, Bill bill, Set<BillItem> billItems ) {
        BillItem item = getExistedBillItemInBill(billItem,billItems);
        if (item == null) {
            billItem.setBill(bill);
            billItems.add(billItem);
        } else {
            item.setQuantity(billItem.getQuantity() + item.getQuantity());
        }
        return billItems;
    }
    public Set<BillItem> updateBillItemInBill(BillItem billItem, Bill bill, Set<BillItem> billItems ) {
        BillItem item = getExistedBillItemInBill(billItem,billItems);
        if (item == null) {
            billItem.setBill(bill);
            billItems.add(billItem);
        } else {
            item.setQuantity(billItem.getQuantity());
        }
        return billItems;
    }

    /**
     * Get existed BillItem in list of BillItem.
     *
     * @param billItem BillItem
     * @return BillItem if exist, null otherwise
     */
    private BillItem getExistedBillItemInBill(BillItem billItem, Set<BillItem> billItems ) {
        List<BillItem> items = billItems.stream().filter(billItem1 -> billItem1.getMenuItem().getId() == billItem.getMenuItem().getId()).collect(Collectors.toList());
        return items.size() == 0 ? null : items.get(0);
    }

}
