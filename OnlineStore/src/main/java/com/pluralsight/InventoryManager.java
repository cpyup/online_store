package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    public static void loadProductsFromFile(String filePath, ArrayList<Product> targetInventory) {
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

    public static void displayAllProducts(ArrayList<Product> currentInventory) {
        if (currentInventory.isEmpty()) {
            System.out.println("\nThe inventory is empty.");
            return;
        }

        System.out.println("\nAvailable Products:");
        currentInventory.forEach(System.out::println);
    }

    public static void searchInventoryForId(ArrayList<Product> targetInventory, String targetId) {
        StringBuilder output = new StringBuilder();
        findProductsById(targetId,targetInventory).forEach(product -> output.append("\n").append(product.toString()));
        System.out.println(output.isEmpty() ? "\nID Not Found" : output);
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

    private static List<Product> findProductsById(String targetId, ArrayList<Product> targetInventory) {
        return targetInventory.stream()
                .filter(product -> product.id().contains(targetId.toUpperCase())).
                toList();
    }
}
