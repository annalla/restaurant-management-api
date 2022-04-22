package com.restaurant.restaurantmanagementapi.utils;

/**
 * The Message class wraps all messages in the project
 */
public class Message {
    public static final String NOT_FOUND = "Id %d is not existed";

    public static final String NOT_EXISTED_BILL = "Bill %d is not existed";
    public static final String NOT_EXISTED_BILL_ITEM_IN_BILL = "Bill item %s is not existed in this bill";


    public static final String NOT_EXISTED_MENU_ITEM = "MenuItem %d is not existed";
    public static final String EXISTED_MENU_ITEM_NAME = "Name is existed";
    public static final String CAN_NOT_DELETE_MENU_ITEM = "Menu item can not delete due to existence in the bill, but status is set inactive";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED_DATA_ACCESS = "Data access failed";
}
