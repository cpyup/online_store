package com.pluralsight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Cart {

    private final ArrayList<Product> ITEMS = new ArrayList<>();
    private final Scanner SCANNER;
    private double totalAmount;

    public Cart(Scanner SCANNER) {
        this.SCANNER = SCANNER;
    }

    public void addToCart(String productId, ArrayList<Product> inventory) {
        Product product = findProductById(productId, inventory);
        if (product != null) {
            ITEMS.add(product);
            System.out.println("\n" + product.name() + " has been added to your cart.");
        } else {
            System.out.println("\nProduct with ID '" + productId + "' not found.");
        }
    }

    public void displayCart() {
        if (ITEMS.isEmpty()) {
            System.out.println("\nYour cart is empty.");
            return;
        }

        System.out.println(printCartDetails());
        handleCartInput();
    }

    private void handleCartInput() {
        String input;
        do {
            System.out.println("\nOptions:\n\tC - Checkout\n\tR - Remove From Cart\n\tE - Exit to the main menu");
            input = SCANNER.nextLine().trim();
            if ("C".equalsIgnoreCase(input)) {
                checkOut();
                return;
            } else if ("R".equalsIgnoreCase(input)) {
                removeFromCart();
            }
        } while (!"E".equalsIgnoreCase(input));
    }

    private void removeFromCart() {
        System.out.print("\nEnter product ID to remove: ");
        String productId = SCANNER.nextLine().trim();
        boolean removed = ITEMS.removeIf(product -> product.id().equalsIgnoreCase(productId));
        if (removed) {
            System.out.println("\nProduct removed from cart.");
        } else {
            System.out.println("\nNo product found with ID: " + productId + " found in cart.");
        }
        System.out.println(printCartDetails());
    }

    public String printCartDetails() {
        HashMap<Product, Integer> currentCounts = new HashMap<>();
        totalAmount = 0.0;

        StringBuilder outString = new StringBuilder();
        outString.append("\nYour cart items:\n");

        for (Product product : ITEMS) {
            totalAmount += product.price();
            currentCounts.put(product, currentCounts.getOrDefault(product, 0) + 1);
        }

        currentCounts.forEach((key, value) -> outString.append("[x" + value + "] " + key.toString() + "\n"));  // For displaying multiple items as count

        outString.append(String.format("\nTotal amount: $%.2f", totalAmount));
        return outString.toString();
    }

    private void checkOut() {
        if (ITEMS.isEmpty()) {
            System.out.println("\nYour cart is empty. Please add items to your cart before checking out.\n");
            return;
        }

        System.out.println(printCartDetails());

        // Prompt user for confirmation
        System.out.print("\nEnter amount of cash for purchase: ");
        double paymentAmount = SCANNER.nextDouble();
        SCANNER.nextLine();

        if (paymentAmount >= totalAmount) {
            // Successful payment, handle receipt here
            totalAmount = 0.0;
            ITEMS.clear();
            System.out.println("\nPurchase successful!\nPress enter to return to main menu.");
            SCANNER.nextLine();
        } else {
            System.out.println("\nPurchase canceled: Insufficient Funds");
        }
    }

    private Product findProductById(String id, ArrayList<Product> inventory) {
        return inventory.stream()
                .filter(product -> product.id().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
}
