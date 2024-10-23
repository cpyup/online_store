package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Inventory { // TODO: Refactor as an actual object, move loading methods to new class
    private final List<Product> CURRENT_INVENTORY;

    public Inventory(){
        CURRENT_INVENTORY = new ArrayList<>();
    }

    public List<Product> getCurrentInventory() {
        return CURRENT_INVENTORY;
    }

    /**
     * Creates products from a specified data file and adds them to the target inventory.
     *
     * <p>This method reads each line of the file, splits the line by the "|" character,
     * and parses the resulting values into {@link Product} objects. Products are added
     * to the specified inventory list if they contain exactly three values.</p>
     *
     * @param filePath        The path to the file containing product data. This file should
     *                        contain product entries in the format: ID|Name|Price.
     *                        cannot be null.
     * @throws NullPointerException if {@code targetInventory} is null.
     */
    public void loadProducts(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\|");
                if (values.length == 3) {
                    Product product = parseProductString(values);
                    if (product != null) {
                        CURRENT_INVENTORY.add(product);
                    }
                } else {
                    System.out.println("Invalid product line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
    }

    /**
     * Displays all products in the current inventory to the console.
     *
     * <p>If the inventory is empty, a message indicating that the inventory is empty
     * will be printed. Otherwise, all available products will be printed.</p>
     *
     * @throws NullPointerException if {@code currentInventory} is null.
     */
    public void displayAllProducts() {
        if (CURRENT_INVENTORY.isEmpty()) {
            System.out.println("\nThe inventory is empty.");
            return;
        }

        System.out.println("\nAvailable Products:");
        CURRENT_INVENTORY.forEach(System.out::println);
    }

    /**
     * Searches the inventory for products with IDs that contain the specified target ID
     * and displays the results to the console.
     *
     * <p>If no products are found, a message indicating that the ID was not found will be printed.
     * Otherwise, the matching products will be displayed.</p>
     *
     * @param targetId       The ID substring to search for in the product IDs.
     * @throws NullPointerException if {@code targetInventory} is null.
     */
    public void searchInventoryForId(String targetId) {
        List<Product> foundProducts = findProductsById(targetId);
        if (foundProducts.isEmpty()) {
            System.out.println("\nID Not Found");
        } else {
            foundProducts.forEach(product -> System.out.println("\n" + product));
        }
    }

    /**
     * Parses an array of strings into a {@link Product} object.
     *
     * <p>This method expects the array to contain three elements: ID, Name, and Price.
     * If the price cannot be parsed into a double, an error message will be printed,
     * and the method will return null.</p>
     *
     * @param values An array of strings representing product details.
     *               This array should contain three elements: ID, Name, and Price.
     * @return A {@link Product} object created from the provided values,
     *         or null if parsing fails.
     */
    private static Product parseProductString(String[] values) {
        try {
            String id = values[0].trim();
            String name = values[1].trim();
            double price = Double.parseDouble(values[2].trim());
            return new Product(id, name, price);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing price: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a list of products from the specified inventory whose IDs contain the given target ID.
     *
     * <p>This method filters the products in the provided inventory based on whether their
     * IDs contain the specified target ID, which is converted to uppercase for case-insensitive matching.</p>
     *
     * @param targetId      The ID substring to search for within the product IDs.
     *                      This value is converted to uppercase for comparison.
     * @return A list of {@link Product} objects from the inventory whose IDs contain the target ID.
     *         If no products match, an empty list is returned.
     * @throws NullPointerException if {@code targetInventory} is null.
     */
    private List<Product> findProductsById(String targetId) {
        return CURRENT_INVENTORY.stream()
                .filter(product -> product.id().contains(targetId.toUpperCase()))
                .toList();
    }
}
