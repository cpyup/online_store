package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class Store {

    private static final String PRODUCTS_PATH = "products.csv";
    private static final ArrayList<Product> inventory = new ArrayList<>();
    private static final ArrayList<Product> cart = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static double totalAmount;
    public static void main(String[] args) {
        // Initialize variables


        totalAmount = 0.0;

        // Load inventory from CSV file
        loadInventory(PRODUCTS_PATH);

        // Create scanner to read user input

        int choice = -1;

        // Display menu and get user choice until they choose to exit
        while (choice != 3) {
            System.out.println("\nWelcome to the Online com.pluralsight.Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");

            choice = scanner.nextInt();
            scanner.nextLine();

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
                    inventory.add(new Product(id, name, price));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // This method should read a CSV file with product information and
        // populate the inventory ArrayList with com.pluralsight.Product objects. Each line
        // of the CSV file contains product information in the following format:
        //
        // id,name,price
        //
        // where id is a unique string identifier, name is the product name,
        // price is a double value representing the price of the product
    }

    public static void displayProducts() {
        // This method should display a list of products from the inventory,
        // and prompt the user to add items to their cart. The method should
        // prompt the user to enter the ID of the product they want to add to
        // their cart. The method should
        // add the selected product to the cart ArrayList.
        if (inventory.isEmpty()) {
            System.out.println("\nThe inventory is empty.");
            return;
        }

        System.out.println("\nAvailable Products:");
        for (Product product : inventory) {
            System.out.println(product);
        }

        System.out.print("\nOptions:\n\tEnter ID of item to add it to your cart\n\tEnter 'S' to search for an item\n\tEnter 'E' to exit to the main menu\n");
        String input = scanner.nextLine();

        while (!input.equalsIgnoreCase("E")) {
            if(input.equalsIgnoreCase("S")){
                System.out.println("\nEnter the ID to search for: ");
                String searchIn = scanner.nextLine().trim();
                try{
                    if(findProductById(searchIn)!=null){
                        System.out.println(findProductById(searchIn));
                    }else{
                        System.out.println("\nID Not Found");
                    }
                } catch (Exception e){
                    System.out.println("\nID Not Found");
                }
            }else{
                try {
                    String productId = input.trim();
                    Product selectedProduct = null;
                    for (Product product : inventory) {
                        if (product.id().equalsIgnoreCase(productId)) {
                            selectedProduct = product;
                            break;
                        }
                    }

                    if (selectedProduct != null) {
                        cart.add(selectedProduct);
                        System.out.println(selectedProduct.name() + " has been added to your cart.");
                    } else {
                        System.out.println("Product with ID '" + productId + "' not found.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input. Please enter a valid product ID,'S' to search, or 'E' to exit to main menu:");
                }
            }

            System.out.print("\nEnter another ID to add to your cart, 'S' to search, or 'E' to exit to main menu: ");
            input = scanner.nextLine();
        }

    }

    public static void displayCart() {
        // This method should display the items in the cart ArrayList, along
        // with the total cost of all items in the cart. The method should
        // prompt the user to remove items from their cart by entering the ID
        // of the product they want to remove. The method should update the cart ArrayList and totalAmount
        // variable accordingly.
        if (cart.isEmpty()) {
            System.out.println("\nYour cart is empty.");
            return;
        }

        // Display the items in the cart
        HashMap<Product,Integer> currentCounts = new HashMap<>();

        System.out.println("\nYour cart items:");
        for (Product product : cart) {
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

        // Prompt user for ID of the product to remove
        System.out.println("\nOptions:\n\tC - Checkout\n\tR - Remove From Cart\n\tE - Exit to the main menu");
        String input = scanner.nextLine().trim();

        while (!input.equalsIgnoreCase("E")) {  // Exit to main menu
            if(input.equalsIgnoreCase("C")){
                // Checkout cart from here
                checkOut();

            }else{
                try {
                    boolean found = false;

                    // Searching cart to remove indicated item, update the totalAmount for removed
                    for (Product product : cart) {
                        if (product.id().equalsIgnoreCase(input)) {
                            totalAmount -= product.price();
                            cart.remove(product);
                            found = true;
                            System.out.println("\nProduct removed from cart.");
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("\nNo product found with ID: " + input + " found in cart.");
                    }

                } catch (Exception e) {
                    System.out.println("\nInvalid input. Please enter a valid product ID or 'E' to exit.");
                }
            }

            // Display updated cart and total
            System.out.println("\nCurrent cart items:");
            for (Product product : cart) {
                System.out.println(product);
            }
            System.out.printf("\nTotal amount: $%.2f", totalAmount);

            // Prompt for next input
            System.out.print("\nEnter the ID of the product you want to remove, 'C' to checkout, or 'E' to exit: ");  // Modify to check if cart is empty before prompt
            input = scanner.nextLine();
        }
    }

    public static void checkOut() {
        // This method should calculate the total cost of all items in the cart,
        // and display a summary of the purchase to the user. The method should
        // prompt the user to confirm the purchase, and deduct the total cost
        // from their account if they confirm.
        if (cart.isEmpty()) {
            System.out.println("\nYour cart is empty. Please add items to your cart before checking out.\n");
            return;
        }

        totalAmount = 0.0;  // Reset the total before displaying

        // Display a summary of the purchase
        System.out.println("\nSummary of your purchase:");
        for (Product product : cart) {
            System.out.println(product);
            totalAmount += product.price();
        }

        // Display the total amount
        System.out.printf("Total amount due: $%.2f\n", totalAmount);

        // Prompt user for confirmation
        System.out.print("\nEnter amount of cash for purchase: ");

        double paymentAmount = scanner.nextDouble();
        scanner.nextLine();

        if (paymentAmount >= totalAmount) {
            // Successful payment, handle receipt here
            totalAmount = 0.0;
            cart.clear();

        } else {
            System.out.println("\nPurchase canceled: Insufficient Funds");
        }
    }

    public static Product findProductById(String id) {
        // This method should search the inventory ArrayList for a product with
        // the specified ID, and return the corresponding com.pluralsight.Product object. If
        // no product with the specified ID is found, the method should return
        // null.
        try {
            for (Product product : inventory) {
                if (product.id().equalsIgnoreCase(id)) {
                    return product;
                }
            }
        }catch (Exception e){
            System.out.println("\nError searching for ID: "+e);
        }
        return null;
    }
}