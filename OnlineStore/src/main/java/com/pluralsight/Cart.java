package com.pluralsight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Cart {

    private final ArrayList<Product> items = new ArrayList<>();
    private double totalAmount;
    private final Scanner scanner;

    public Cart(Scanner scanner) {
        this.scanner = scanner;
    }

    public void addToCart(String productId, ArrayList<Product> inventory) {
        Product product = findProductById(productId, inventory);
        if (product != null) {
            items.add(product);
            System.out.println("\n" + product.name() + " has been added to your cart.");
        } else {
            System.out.println("\nProduct with ID '" + productId + "' not found.");
        }
    }

    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println("\nYour cart is empty.");
            return;
        }

        printCartDetails();
        handleCartInput();
    }

    private void handleCartInput() {
        String input;
        do {
            System.out.println("\nOptions:\n\tC - Checkout\n\tR - Remove From Cart\n\tE - Exit to the main menu");
            input = scanner.nextLine().trim();
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
        String productId = scanner.nextLine().trim();
        boolean removed = items.removeIf(product -> product.id().equalsIgnoreCase(productId));
        if (removed) {
            System.out.println("\nProduct removed from cart.");
        } else {
            System.out.println("\nNo product found with ID: " + productId + " found in cart.");
        }
        printCartDetails();
    }

    public void printCartDetails() {
        HashMap<Product, Integer> currentCounts = new HashMap<>();
        totalAmount = 0;  // Reset current total before adding from cart

        System.out.println("\nYour cart items:");
        for (Product product : items) {
            totalAmount += product.price();
            currentCounts.put(product, currentCounts.getOrDefault(product, 0) + 1);
        }

        currentCounts.forEach((key, value) -> System.out.println("[x" + value + "] " + key.toString()));  // For displaying multiple items as count

        // Display the total amount
        System.out.printf("\nTotal amount: $%.2f\n", totalAmount);
    }

    private void checkOut() {
        if (items.isEmpty()) {
            System.out.println("\nYour cart is empty. Please add items to your cart before checking out.\n");
            return;
        }

        printCartDetails();

        // Prompt user for confirmation
        System.out.print("\nEnter amount of cash for purchase: ");
        double paymentAmount = scanner.nextDouble();
        scanner.nextLine();

        if (paymentAmount >= totalAmount) {
            // Successful payment, handle receipt here
            totalAmount = 0.0;
            items.clear();
            System.out.println("\nPurchase successful!\nPress enter to return to main menu.");
            scanner.nextLine();
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
