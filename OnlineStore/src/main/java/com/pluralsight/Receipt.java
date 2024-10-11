package com.pluralsight;

import java.util.ArrayList;

public class Receipt {
    private static final String DIRECTORY_OUT = "receipts\\";
    private static double totalCost;
    private static Cart cart;

    public Receipt(Cart cart){
        this.cart = cart;
    }

    public static void processReceipt(){
        StringBuilder output = new StringBuilder();
        output.append(cart.printCartDetails());
        displayReceipt(output.toString());
    }

    private static void displayReceipt(String receiptString){

    }

    private static void writeReceipt(){

    }
}
