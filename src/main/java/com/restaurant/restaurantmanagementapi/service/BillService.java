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

    public String checkBillItem(List<BillItemRequest> billItems) {

        for (BillItemRequest billItem : billItems) {
            if(billItem.getMenuItemId()==null||billItem.getQuantity()==null){
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

    public String checkBillItemId(List<BillItemDeleteRequest> billItems) {
        for (BillItemDeleteRequest billItem : billItems) {
            if(billItem.getId()==null){
                return Message.MISSING_FIELD;
            }
            Optional<BillItem> menuItem = billItemRepository.findById(billItem.getId());
            if (menuItem.isEmpty()) {
                return String.format(Message.NOT_EXISTED_BILL_ITEM, billItem.getId());
            }
        }
        return Message.OK;
    }

    public BillResponse add(List<BillItemRequest> billItems) {
        Bill newBill = new Bill();
        newBill.setTotal(0.0);
        newBill.setOrderedTime(LocalDateTime.now());
        Long billId = billRepository.save(newBill).getId();
        updateBill(billItems, billId, newBill);
        Bill response=billRepository.save(newBill);
        return BillMapper.toBillResponse(response);
    }

    public BillResponse getById(Long id) {
        return billRepository.findById(id).map(bill -> BillMapper.toBillResponse(bill)).orElse(null);
    }

    public List<BillResponse> getAll(Pageable pageable) {
        return billRepository.findAll(pageable).getContent().stream().map(bill -> BillMapper.toBillResponse(bill)).collect(Collectors.toList());
    }

    public BillResponse update(List<BillItemRequest> billItems, Long id) {
        return billRepository.findById(id).map(bill -> {
            deleteBillItemWithBill(bill.getId());
            bill.clearAllBillItem();
            updateBill(billItems, id, bill);
            return BillMapper.toBillResponse(billRepository.save(bill));
        }).orElse(null);
    }

    private void updateBill(List<BillItemRequest> billItems, Long id, Bill bill) {
        double total = 0.0;
        for (BillItemRequest billItem : billItems) {
            Optional<MenuItem> menuItem = menuItemRepository.findActiveItemById(billItem.getMenuItemId());
            BillItem newBillItem = new BillItem(id, billItem.getMenuItemId(),menuItem.get(), billItem.getQuantity(), menuItem.get().getPrice());
            bill.addBillItem(newBillItem);
            total += (newBillItem.getPrice() * newBillItem.getQuantity());
        }
        bill.setTotal(Math.round(total * 100) / 100.0);
    }

    private void deleteBillItemWithBill(Long id) {
        List<BillItem> orders = billItemRepository.findByBillId(id);
        for (BillItem order : orders) {
            billItemRepository.delete(order);
        }
    }

    public boolean delete(Long id) {
        return billRepository.findById(id).map(bill -> {
            billRepository.delete(bill);
            return true;
        }).orElse(false);
    }

    public BillResponse addBillItem(Long billId, List<BillItemRequest> billItemRequest) {
        return billRepository.findById(billId).map(bill -> {
            for (BillItemRequest billItem : billItemRequest) {
                Optional<MenuItem> menuItem = menuItemRepository.findActiveItemById(billItem.getMenuItemId());
                BillItem newBillItem = new BillItem(billId, billItem.getMenuItemId(),menuItem.get(), billItem.getQuantity(), menuItem.get().getPrice());
                bill.addBillItem(newBillItem);
            }
            double total = calculateTotalBill(billId);
            bill.setTotal(Math.round(total * 100) / 100.0);
            return BillMapper.toBillResponse(billRepository.save(bill));
        }).orElse(null);
    }

    public double calculateTotalBill(Long id) {
        return billItemRepository.findByBillId(id).stream().map(billItem -> billItem.getPrice() * billItem.getQuantity()).collect(Collectors.summingDouble(Double::doubleValue));
    }

    public BillResponse deleteBillItem(Long billId,  List<BillItemDeleteRequest>  billItems) {
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
