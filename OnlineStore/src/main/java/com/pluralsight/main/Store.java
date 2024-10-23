package com.pluralsight.main;

import com.pluralsight.model.Cart;
import com.pluralsight.model.Inventory;
import com.pluralsight.ui.InputManager;

import java.util.Scanner;


public class Store {
    public static final String PRODUCTS_PATH = "products.csv";
    private static final Inventory INVENTORY = new Inventory();
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Cart CART = new Cart();

    public static void main(String[] args) {
        INVENTORY.loadInventoryProducts(PRODUCTS_PATH);
        String choice = "";


        // Display menu and get user choice until they choose to exit
        while (!choice.equals("3")) {

            System.out.println("\nWelcome to the Online Store!\n\t1. Show Products\n\t2. Show Cart\n\t3. Exit");
            choice = SCANNER.nextLine();

            // Call the appropriate method based on user choice
            switch (choice) {
                case "1" -> InputManager.productMenuDisplay(INVENTORY,SCANNER,CART);
                case "2" -> InputManager.handleCartInput(SCANNER,CART);
                case "3" -> System.out.println("\nThank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
