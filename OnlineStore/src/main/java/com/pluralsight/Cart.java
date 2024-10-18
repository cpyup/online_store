package com.pluralsight;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Cart {

    private static final String FOLDER_PATH = "receipts\\";
    private final ArrayList<Product> ITEMS = new ArrayList<>();
    private final Scanner SCANNER;
    private double totalAmount;

    public Cart(Scanner SCANNER) {
        this.SCANNER = SCANNER;
    }

    public void addToCart(String productId, ArrayList<Product> inventory) {
        Product product = findProductById(productId, inventory);
        if (product != null) {
            ITEMS.add(product);
            System.out.println("\n" + product.name() + " has been added to your cart.");
        } else {
            System.out.println("\nProduct with ID '" + productId + "' not found.");
        }
    }

    public void displayCart() {
        if (ITEMS.isEmpty()) {
            System.out.println("\nYour cart is empty.");
            return;
        }

        System.out.println(printCartDetails());
        handleCartInput();
    }

    private void handleCartInput() {
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

    private void removeFromCart() {
        System.out.print("\nEnter product ID to remove: ");
        String productId = SCANNER.nextLine().trim();
        boolean removed = ITEMS.removeIf(product -> product.id().equalsIgnoreCase(productId));
        if (removed) {
            System.out.println("\nProduct removed from cart.");
        } else {
            System.out.println("\nNo product found with ID: " + productId + " found in cart.");
        }
        System.out.println(printCartDetails());
    }

    public String printCartDetails() {
        HashMap<Product, Integer> currentCounts = new HashMap<>();
        totalAmount = 0.0;

        StringBuilder outString = new StringBuilder();
        outString.append("\nYour cart items:\n");

        for (Product product : ITEMS) {
            totalAmount += product.price();
            currentCounts.put(product, currentCounts.getOrDefault(product, 0) + 1);
        }

        currentCounts.forEach((key, value) -> outString.append("[x").append(value).append("] ").append(key.toString()).append("\n"));  // For displaying multiple items as count

        outString.append(String.format("\nTotal amount: $%.2f", totalAmount));
        return outString.toString();
    }

    private void checkOut() {
        if (ITEMS.isEmpty()) {
            System.out.println("\nYour cart is empty. Please add items to your cart before checking out.\n");
            return;
        }

        System.out.println(printCartDetails());

        // Prompt user for confirmation
        System.out.print("\nEnter amount of cash for purchase: ");
        double paymentAmount = SCANNER.nextDouble();
        SCANNER.nextLine();

        if (paymentAmount >= totalAmount) {
            // Successful payment, handle receipt here
            double change = paymentAmount - totalAmount;
            String receiptOut = printCartDetails() +
                    String.format("\nPayment Amount: $%.2f", paymentAmount) +
                    (change == 0 ? "\n" : String.format("\nChange Due: $%.2f", change));


            System.out.println(receiptOut);
            saveNewReceipt(receiptOut);


            totalAmount = 0.0;
            ITEMS.clear();
            System.out.println("\nPurchase successful!\nPress enter to return to main menu.");
            SCANNER.nextLine();
        } else {
            System.out.println("\nPurchase canceled: Insufficient Funds");
        }
    }

    private Product findProductById(String id, ArrayList<Product> inventory) {
        return inventory.stream()
                .filter(product -> product.id().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    private static void saveNewReceipt(String newReceiptOutput){
        try{
            String targetPathFull = FOLDER_PATH+getCurrentDatestamp()+".txt";
            createNewFile(targetPathFull);
            writeToFile(targetPathFull,newReceiptOutput);
        }catch(Exception e){
            System.out.println("Error saving receipt\n"+e);
        }
    }

    private static void createNewFile(String fileName){
        try {
            File newFile = new File(fileName);
            System.out.println("\nFile '" + fileName + "' " + ((newFile.createNewFile()) ?"Successfully Created":"Already Exists"));
        } catch (IOException ex) {
            System.out.println("Error Creating File " + fileName);
            throw new RuntimeException(ex);
        }
    }

    private static void writeToFile(String targetFilePath, String dataToWrite){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetFilePath))){
            bufferedWriter.write(dataToWrite);
            bufferedWriter.flush();
        }catch(Exception e){
            System.out.println("Error writing to file "+targetFilePath+e);
        }
    }

    private static String getCurrentDatestamp(){
        DateTimeFormatter stampFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDateTime now = LocalDateTime.now();
        return now.format(stampFormat);
    }
}
