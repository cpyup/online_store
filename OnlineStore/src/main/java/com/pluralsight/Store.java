package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {

    private static final String PRODUCTS_PATH = "products.csv";
    private static final ArrayList<Product> INVENTORY = new ArrayList<>();
    private static final Scanner SCANNER = new Scanner(System.in);
    private static Cart cart;

    public static void main(String[] args) {
        loadInventory(PRODUCTS_PATH);
        cart = new Cart(SCANNER);

        int choice = -1;

        // Display menu and get user choice until they choose to exit
        while (choice != 3) {
            System.out.println("\nWelcome to the Online Store!\n1. Show Products\n2. Show Cart\n3. Exit");
            choice = SCANNER.nextInt();
            SCANNER.nextLine();

            // Call the appropriate method based on user choice
            switch (choice) {
                case 1 -> displayProducts();
                case 2 -> cart.displayCart();
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
                cart.addToCart(input, INVENTORY);
            }
        } while (!"E".equalsIgnoreCase(input));
    }

    private static void searchProduct() {
        System.out.println("\nEnter the ID to search for: ");
        String searchIn = SCANNER.nextLine().trim();
        Product foundProduct = findProductById(searchIn);
        System.out.println(foundProduct != null ? foundProduct : "\nID Not Found");
    }

    public static Product findProductById(String id) {
        return INVENTORY.stream()
                .filter(product -> product.id().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
}
