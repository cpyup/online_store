package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class InventoryManager {
    public static void loadProducts(String filePath, List<Product> targetInventory) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\|");
                if (values.length == 3) {
                    Product product = parseProductString(values);
                    if (product != null) {
                        targetInventory.add(product);
                    }
                } else {
                    System.out.println("Invalid product line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
    }

    public static void displayAllProducts(List<Product> currentInventory) {
        if (currentInventory.isEmpty()) {
            System.out.println("\nThe inventory is empty.");
            return;
        }

        System.out.println("\nAvailable Products:");
        currentInventory.forEach(System.out::println);
    }

    public static void searchInventoryForId(List<Product> targetInventory, String targetId) {
        List<Product> foundProducts = findProductsById(targetId, targetInventory);
        if (foundProducts.isEmpty()) {
            System.out.println("\nID Not Found");
        } else {
            foundProducts.forEach(product -> System.out.println("\n" + product));
        }
    }

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

    private static List<Product> findProductsById(String targetId, List<Product> targetInventory) {
        return targetInventory.stream()
                .filter(product -> product.id().contains(targetId.toUpperCase()))
                .toList();
    }
}
