package com.restaurant.restaurantmanagementapi.service;

import com.restaurant.restaurantmanagementapi.dto.MenuItemResponse;
import com.restaurant.restaurantmanagementapi.mapper.MenuItemMapper;
import com.restaurant.restaurantmanagementapi.model.MenuItem;
import com.restaurant.restaurantmanagementapi.repository.MenuItemRepository;
import com.restaurant.restaurantmanagementapi.repository.BillItemRepository;
import com.restaurant.restaurantmanagementapi.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuItemService {
    @Autowired
    MenuItemRepository menuItemRepository;
    @Autowired
    BillItemRepository orderedDetailRepository;
    public boolean checkExistedName(String name){
        return menuItemRepository.findByName(name).isPresent();
    }
    public MenuItemResponse getById(Long id) {
        return menuItemRepository.findById(id).map(menuItem -> MenuItemMapper.toMenuItemResponse(menuItem)).orElse(null);
    }
    public MenuItemResponse add(MenuItem newMenuItem) {
        newMenuItem.setStatus(true);
        return MenuItemMapper.toMenuItemResponse(menuItemRepository.save(newMenuItem));
    }
    public MenuItemResponse update(MenuItem newMenuItem,Long id) {
        return menuItemRepository.findActiveItemById(id).map(existedMenuItem -> {
            if(newMenuItem.getName()!=null&&newMenuItem.getName().length()!=0){
                existedMenuItem.setName(newMenuItem.getName());
            }
            if(newMenuItem.getDescription()!=null&&newMenuItem.getDescription().length()!=0){
                existedMenuItem.setDescription(newMenuItem.getDescription());
            }
            if(newMenuItem.getImage()!=null && newMenuItem.getImage().length()!=0){
                existedMenuItem.setImage(newMenuItem.getImage());
            }
            if(newMenuItem.getPrice()>0){
                existedMenuItem.setPrice(newMenuItem.getPrice());
            }
            return MenuItemMapper.toMenuItemResponse(menuItemRepository.save(existedMenuItem));
        }).orElse(null);
    }

    public String check(MenuItem newMenuItem){
        if(newMenuItem.getName()== null || newMenuItem.getName().length()==0){
            return Message.EMPTY_NAME;
        }
        if (checkExistedName(newMenuItem.getName())){
            return Message.EXISTED_NAME;
        }
       return Message.OK;
    }
    public List<MenuItemResponse> getAll(Pageable pageable) {
        return menuItemRepository.findAll(pageable).getContent().stream().map(menuItem -> MenuItemMapper.toMenuItemResponse(menuItem)).collect(Collectors.toList());
    }
    public List<MenuItemResponse> search(String keyword, Pageable pageable){
        return menuItemRepository.search(keyword,pageable).getContent().stream().map(menuItem -> MenuItemMapper.toMenuItemResponse(menuItem)).collect(Collectors.toList());
    }
    public String delete(Long id) {
        return menuItemRepository.findActiveItemById(id).map(existedMenuItem -> {
            if (orderedDetailRepository.findByMenuItemId(id).size()!=0){
                existedMenuItem.setStatus(false);
                menuItemRepository.save(existedMenuItem);
                return Message.CAN_NOT_DELETE;
            }
           menuItemRepository.delete(existedMenuItem);
            return Message.OK;
        }).orElse(Message.NOT_FOUND);
    }
}
