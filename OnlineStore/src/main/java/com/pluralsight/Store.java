package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Store {

    private static final String PRODUCTS_PATH = "products.csv";
    private static final ArrayList<Product> INVENTORY = new ArrayList<>();
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Cart CART = new Cart(SCANNER);

    public static void main(String[] args) {
        loadProductsFromFile(PRODUCTS_PATH,INVENTORY);

        int choice = -1;

        // Display menu and get user choice until they choose to exit
        while (choice != 3) {
            System.out.println("\nWelcome to the Online Store!\n\t1. Show Products\n\t2. Show Cart\n\t3. Exit");
            choice = SCANNER.nextInt();
            SCANNER.nextLine();

            // Call the appropriate method based on user choice
            switch (choice) {
                case 1 -> displayProducts(INVENTORY);
                case 2 -> CART.displayCart();
                case 3 -> System.out.println("\nThank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void loadProductsFromFile(String filePath, ArrayList<Product> targetInventory) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\|");
                if (values.length == 3) {
                    targetInventory.add(parseProductString(values));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Product parseProductString(String[] values){
        try{
            String id = values[0].trim();
            String name = values[1].trim();
            double price = Double.parseDouble(values[2].trim());
            return new Product(id, name, price);
        }catch(Exception e){
            System.out.println("Error parsing line "+e);
            return null;
        }
    }

    private static void displayProducts(ArrayList<Product> targetInventory) {
        if (targetInventory.isEmpty()) {
            System.out.println("\nThe inventory is empty.");
            return;
        }

        System.out.println("\nAvailable Products:");
        targetInventory.forEach(System.out::println);
        handleProductInput();
    }

    private static void handleProductInput() {
        String input;
        do {
            System.out.print("\nEnter product ID to add to your cart, 'S' to search, or 'E' to exit: ");
            input = SCANNER.nextLine().trim();

            if ("S".equalsIgnoreCase(input)) {
                searchProductId(INVENTORY);
            } else if (!"E".equalsIgnoreCase(input)) {
                parseAddingInput(input);
            }
        } while (!"E".equalsIgnoreCase(input));
    }

    // Parsing input string to check for optional argument (item count)
    private static void parseAddingInput(String input){
        String[] args;
        try{
            // If there is an amount arg, call to add item with the amount. If not, default to 1
            args = input.split(" ");
            int itemCount = args.length > 1 ? Integer.parseInt(args[1]) : 1;
            CART.addToCart(args[0], INVENTORY, Math.max(itemCount, 1));
        }catch (NumberFormatException e) {
            System.out.println("Error Parsing Item Count\nExpected Input: item id [String] item amount [int]");
        }
    }

    private static void searchProductId(ArrayList<Product> targetInventory) {
        System.out.println("\nEnter the ID to search for: ");  // Input needs moved
        String searchIn = SCANNER.nextLine().trim();
        StringBuilder output = new StringBuilder();
        findProductById(searchIn,targetInventory).forEach(product -> output.append("\n").append(product.toString()));
        System.out.println(output.isEmpty() ? "\nID Not Found" : output);
    }

    private static List<Product> findProductById(String targetId, ArrayList<Product> targetInventory) {
        return targetInventory.stream()
               .filter(product -> product.id().contains(targetId.toUpperCase())).
               toList();
    }
}
