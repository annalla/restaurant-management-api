package com.restaurant.restaurantmanagementapi.utils;

/**
 * The Message class wraps all messages in the project
 */
public class Message {
    public static final String NOT_FOUND = "ID %s IS NOT EXISTED";
    public static final String EXISTED_NAME = "NAME IS EXISTED";
    public static final String MISSING_FIELD = "BILL ITEMS MISS SOME FIELDS";
    public static final String EMPTY_BILL_ITEM = "BILL ITEM IS EMPTY";
    public static final String CAN_NOT_DELETE = "MENU ITEM CAN NOT DELETE DUE TO EXISTENCE IN BILL, BUT STATUS IS SET INACTIVE";
    public static final String EMPTY_NAME = "MENU ITEM NAME IS EMPTY";
    public static final String NEGATIVE_QUANTITY = "QUANTITY IS NEGATIVE";
    public static final String NOT_EXISTED_MENU_ITEM = "MENU ITEM WITH ID %s NOT EXISTED";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public static final String OK = "OK";
    public static final String NOT_EXISTED_BILL_ITEM = "BILL ITEM WITH ID %s NOT EXISTED";
}
