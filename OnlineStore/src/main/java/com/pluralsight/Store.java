package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {

    public static void main(String[] args) {
        // Initialize variables
        ArrayList<Product> inventory = new ArrayList<Product>();
        ArrayList<Product> cart = new ArrayList<Product>();
        double totalAmount = 0.0;

        // Load inventory from CSV file
        loadInventory("products.csv", inventory);

        // Create scanner to read user input
        Scanner scanner = new Scanner(System.in);
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
                case 1:
                    displayProducts(inventory, cart, scanner);
                    break;
                case 2:
                    displayCart(cart, scanner, totalAmount);
                    break;
                case 3:
                    System.out.println("Thank you for shopping with us!");
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
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

    public static void displayProducts(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) {
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

    public static void displayCart(ArrayList<Product> cart, Scanner scanner, double totalAmount) {
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
        System.out.println("Your cart items:");
        for (Product product : cart) {
            totalAmount += product.price();
            System.out.println("ID: " + product.id() + ", Name: " + product.name() + ", Price: $" + product.price());
        }

        // Display the total amount
        System.out.printf("\nTotal amount: $%.2f\n", totalAmount);

        // Prompt user for ID of the product to remove
        System.out.print("\nTo remove a product from your cart, enter the ID of the product you want to remove\nEnter 'done' to return to main menu: ");
        String input = scanner.nextLine().trim();

        while (!input.equalsIgnoreCase("done")) {
            try {
                boolean found = false;

                // Searching cart to remove indicated item, update the totalAmount for removed
                for (Product product : cart) {
                    if (product.id().equalsIgnoreCase(product.id())) {
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

            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a valid product ID or 'done'.");
            }

            // Display updated cart and total
            System.out.println("\nCurrent cart items:");
            for (Product product : cart) {
                System.out.println("ID: " + product.id() + ", Name: " + product.name() + ", Price: " + product.price());
            }
            System.out.printf("\nTotal amount: $%.2f", totalAmount);

            // Prompt for next input
            System.out.print("\nEnter the ID of the product you want to remove, or type 'done' to finish: ");  // Modify to check if cart is empty before prompt
            input = scanner.nextLine();
        }
    }

    public static void checkOut(ArrayList<Product> cart, double totalAmount, Scanner scanner) {
        // This method should calculate the total cost of all items in the cart,
        // and display a summary of the purchase to the user. The method should
        // prompt the user to confirm the purchase, and deduct the total cost
        // from their account if they confirm.
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Please add items to your cart before checking out.\n");
            return;
        }

        // Display a summary of the purchase
        totalAmount = 0.0;
        System.out.println("\nSummary of your purchase:");
        for (Product product : cart) {
            System.out.println("ID: " + product.id() + ", Name: " + product.name() + ", Price: $" + product.price());
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

            cart.clear();
        } else {
            System.out.println("Purchase canceled: Insufficient Funds");
        }
    }

    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // This method should search the inventory ArrayList for a product with
        // the specified ID, and return the corresponding com.pluralsight.Product object. If
        // no product with the specified ID is found, the method should return
        // null.
        return null;
    }
}