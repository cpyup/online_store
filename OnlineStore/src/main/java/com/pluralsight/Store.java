package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class Store {

    private static final String PRODUCTS_PATH = "products.csv";
    private static final ArrayList<Product> INVENTORY = new ArrayList<>();
    private static final ArrayList<Product> CART = new ArrayList<>();
    private static final Scanner SCANNER = new Scanner(System.in);
    private static double totalAmount;
    public static void main(String[] args) {
        loadInventory(PRODUCTS_PATH);
        totalAmount = 0.0;

        int choice = -1;

        // Display menu and get user choice until they choose to exit
        while (choice != 3) {
            System.out.println("\nWelcome to the Online com.pluralsight.Store!\n1. Show Products\n2. Show Cart\n3. Exit");
            choice = SCANNER.nextInt();
            SCANNER.nextLine();

            // Call the appropriate method based on user choice
            switch (choice) {
                case 1 -> displayProducts();
                case 2 -> displayCart();
                case 3 -> System.out.println("\nThank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    public static void loadInventory(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\|");
                if (values.length == 3) {
                    String id = values[0].trim();
                    String name = values[1].trim();
                    double price = Double.parseDouble(values[2].trim());
                    INVENTORY.add(new Product(id, name, price));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayProducts() {
        if (INVENTORY.isEmpty()) {
            System.out.println("\nThe inventory is empty.");
            return;
        }

        System.out.println("\nAvailable Products:");
        INVENTORY.forEach(System.out::println);
        handleProductInput();
    }

    private static void handleProductInput() {
        String input;
        do {
            System.out.print("\nEnter product ID to add to your cart, 'S' to search, or 'E' to exit: ");
            input = SCANNER.nextLine().trim();

            if ("S".equalsIgnoreCase(input)) {
                searchProduct();
            } else if (!"E".equalsIgnoreCase(input)) {
                addToCart(input);
            }
        } while (!"E".equalsIgnoreCase(input));
    }

    private static void searchProduct() {
        System.out.println("\nEnter the ID to search for: ");
        String searchIn = SCANNER.nextLine().trim();
        Product foundProduct = findProductById(searchIn);
        System.out.println(foundProduct != null ? foundProduct : "\nID Not Found");
    }

    private static void addToCart(String productId) {
        Product product = findProductById(productId);
        if (product != null) {
            CART.add(product);
            System.out.println("\n" + product.name() + " has been added to your cart.");
        } else {
            System.out.println("\nProduct with ID '" + productId + "' not found.");
        }
    }

    public static void displayCart() {
        if (CART.isEmpty()) {
            System.out.println("\nYour cart is empty.");
            return;
        }

        printCart();
        handleCartInput();
    }

    private static void handleCartInput() {
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

    private static void removeFromCart() {
        System.out.print("\nEnter product ID to remove: ");
        String productId = SCANNER.nextLine().trim();
        boolean removed = CART.removeIf(product -> product.id().equalsIgnoreCase(productId));
        if (removed) {
            System.out.println("\nProduct removed from cart.");
        } else {
            System.out.println("\nNo product found with ID: " + productId + " found in cart.");
        }
        printCart();
    }

    public static void printCart(){
        HashMap<Product,Integer> currentCounts = new HashMap<>();
        totalAmount = 0;  // Reset current total before adding from cart

        System.out.println("\nYour cart items:");
        for (Product product : CART) {
            totalAmount += product.price();
            if(currentCounts.containsKey(product)){
                currentCounts.put(product,currentCounts.get(product)+1);
            }else{
                currentCounts.put(product,1);
            }
        }

        currentCounts.forEach((key,value) -> System.out.println("[x"+value+"] "+key.toString()));  // For displaying multiple items as count

        // Display the total amount
        System.out.printf("\nTotal amount: $%.2f\n", totalAmount);
    }

    public static void checkOut() {
        if (CART.isEmpty()) {
            System.out.println("\nYour cart is empty. Please add items to your cart before checking out.\n");
            return;
        }

        printCart();

        // Prompt user for confirmation
        System.out.print("\nEnter amount of cash for purchase: ");

        double paymentAmount = SCANNER.nextDouble();
        SCANNER.nextLine();

        if (paymentAmount >= totalAmount) {
            // Successful payment, handle receipt here
            totalAmount = 0.0;
            CART.clear();
            System.out.println("\nPurchase successful!\nPress enter to return to main menu.");
            SCANNER.nextLine();
        } else {
            System.out.println("\nPurchase canceled: Insufficient Funds");
        }
    }

    public static Product findProductById(String id) {
        return INVENTORY.stream()
                .filter(product -> product.id().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
}