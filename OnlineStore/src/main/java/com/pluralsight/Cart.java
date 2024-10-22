package com.pluralsight;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cart {

    private static final String FOLDER_PATH = "receipts\\";
    private final List<Product> items = new ArrayList<>();
    private double totalAmount;

    public Cart() {
        totalAmount = 0.0;
    }

    public void addProductToCart(String productId, List<Product> inventory, int productCount) {
        Product product = searchForProductInInventory(productId, inventory);

        if (product != null) {
            for (int i = 0; i < productCount; i++) {
                items.add(product);
            }
            System.out.printf("\n%s %s been added to your cart.%n", productCount > 1 ? (productCount + " " + product.name() + "s") : product.name(), productCount > 1 ? "have" : "has");
        } else {
            System.out.printf("\nProduct with ID '%s' not found.%n", productId);
        }
    }

    public void displayCartContents() {
        if (isEmpty()) {
            System.out.println("\nYour cart is empty.");
            return;
        }
        System.out.println("\nYour cart items:\n" + generateReceiptOutput());
    }

    public void removeProductFromCart(String productId, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        for (int i = 0; i < quantity; i++) {
            if (!items.removeIf(product -> product.id().equalsIgnoreCase(productId))) {
                System.out.printf("\nNo product found with ID: %s in cart.%n", productId);
                return;
            }
        }
        System.out.println("\nProduct removed from cart.");
        displayCartContents();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void checkOutCurrentCart(double paymentAmount) {
        if (isEmpty()) {
            System.out.println("\nYour cart is empty. Please add items to your cart before checking out.\n");
            return;
        }
        processPayment(paymentAmount);
    }

    @Override
    public String toString() {
        return generateReceiptOutput();
    }

    private String generateReceiptOutput() {
        StringBuilder outString = new StringBuilder();
        HashMap<Product, Integer> currentCounts = new HashMap<>();
        totalAmount = 0.0;

        for (Product product : items) {
            totalAmount += product.price();
            currentCounts.put(product, currentCounts.getOrDefault(product, 0) + 1);
        }

        currentCounts.forEach((product, count) -> outString.append(String.format("[x%d] %s%n", count, product.toString())));
        outString.append(String.format("\nTotal amount: $%.2f", totalAmount));
        return outString.toString();
    }

    private void processPayment(double paymentAmount) {
        if (paymentAmount >= totalAmount) {
            completePayment(paymentAmount);
        } else {
            System.out.println("\nPurchase canceled: Insufficient Funds");
        }
    }

    private void completePayment(double paymentAmount) {
        double change = paymentAmount - totalAmount;
        String receiptOut = generateReceiptOutput() + String.format("\nPayment Amount: $%.2f", paymentAmount) +
                (change == 0.0 ? "\n" : String.format("\nChange Due: $%.2f", change));

        System.out.println("\n" + receiptOut);
        saveNewReceipt(receiptOut);

        totalAmount = 0.0;
        items.clear();
        System.out.println("\nPurchase successful!");
    }

    private Product searchForProductInInventory(String id, List<Product> inventory) {
        return inventory.stream()
                .filter(product -> product.id().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    private void saveNewReceipt(String newReceiptOutput) {
        try {
            String targetPathFull = FOLDER_PATH + getCurrentDatestamp() + ".txt";
            createNewFile(targetPathFull);
            writeToFile(targetPathFull, "Transaction Receipt " + getCurrentDatestamp() + "\n\nPurchased Item(s):\n" + newReceiptOutput);
        } catch (Exception e) {
            System.out.println("\nError saving receipt:\n" + e.getMessage());
        }
    }

    private void createNewFile(String fileName) {
        try {
            File newFile = new File(fileName);
            System.out.println("\nFile '" + fileName + "' " + (newFile.createNewFile() ? "Successfully Created" : "Already Exists"));
        } catch (IOException ex) {
            System.out.println("Error Creating File " + fileName);
            throw new RuntimeException(ex);
        }
    }

    private void writeToFile(String targetFilePath, String dataToWrite) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetFilePath))) {
            bufferedWriter.write(dataToWrite);
            bufferedWriter.flush();
        } catch (Exception e) {
            System.out.println("\nError writing to file " + targetFilePath + " " + e.getMessage());
        }
    }

    private String getCurrentDatestamp() {
        DateTimeFormatter stampFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDateTime now = LocalDateTime.now();
        return now.format(stampFormat);
    }
}
