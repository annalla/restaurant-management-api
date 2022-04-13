package com.restaurant.restaurantmanagementapi.utils;

/**
 * The Message class wraps all messages in the project
 */
public class Message {
    public static final String NOT_FOUND = "Id %d is not existed";
    public static final String EXISTED_NAME = "Name is existed";
    public static final String MISSING_FIELD = "Bill items miss some fields";
    public static final String EMPTY_BILL_ITEM = "Bill item is empty";
    public static final String CAN_NOT_DELETE = "Menu item can not delete due to existence in the bill, but status is set inactive";
    public static final String EMPTY_NAME = "Menu item name is empty";
    public static final String NEGATIVE_QUANTITY = "Quantity is negative";
    public static final String NOT_EXISTED_MENU_ITEM = "Menu item with id %s not existed";
    public static final String OK = "Ok";
    public static final String NOT_EXISTED_BILL_ITEM = "Bill item with id %s not existed";
}
