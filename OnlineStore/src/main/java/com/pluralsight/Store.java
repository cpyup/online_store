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

        System.out.print("\nEnter the ID of the product you want to add to your cart (or 'E' to exit to main menu): ");
        String input = scanner.nextLine();

        while (!input.equalsIgnoreCase("E")) {
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
                    System.out.println("Product with ID " + productId + " not found.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a valid product ID (or 'E' to exit to main menu)");
            }

            System.out.print("\nEnter another ID to add to your cart (or 'E' to exit to main menu): ");
            input = scanner.nextLine();
        }

    }

    public static void displayCart(ArrayList<Product> cart, Scanner scanner, double totalAmount) {
        // This method should display the items in the cart ArrayList, along
        // with the total cost of all items in the cart. The method should
        // prompt the user to remove items from their cart by entering the ID
        // of the product they want to remove. The method should update the cart ArrayList and totalAmount
        // variable accordingly.
    }

    public static void checkOut(ArrayList<Product> cart, double totalAmount) {
        // This method should calculate the total cost of all items in the cart,
        // and display a summary of the purchase to the user. The method should
        // prompt the user to confirm the purchase, and deduct the total cost
        // from their account if they confirm.
    }

    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // This method should search the inventory ArrayList for a product with
        // the specified ID, and return the corresponding com.pluralsight.Product object. If
        // no product with the specified ID is found, the method should return
        // null.
        return null;
    }
}