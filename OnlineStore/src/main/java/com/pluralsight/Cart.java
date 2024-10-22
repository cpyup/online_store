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
    private double totalAmount;

    public Cart() {
        totalAmount = 0.0;
    }

    public void addProductToCart(String productId, ArrayList<Product> inventory, int productCount) {
        Product product = searchCartForId(productId, inventory);

        if (product != null) {
            for(int i = 0; i < productCount; i++){  // For loop to allow adding multiple items at once
                ITEMS.add(product);
            }

            String itemsPurchased = (productCount > 1) ? productCount+" "+product.name()+"s have" : product.name()+" has";
            System.out.println("\n" + itemsPurchased + " been added to your cart.");
        } else {
            System.out.println("\nProduct with ID '" + productId + "' not found.");
        }
    }

    public void displayCartContents(Scanner scanner) {
        if (isEmpty()) {
            System.out.println("\nYour cart is empty.");
            return;
        }
        System.out.println("\nYour cart items:\n");
        System.out.println(this);
        InputManager.handleCartInput(scanner,this);
    }

    public void removeProductFromCart(String productId, Scanner scanner) {
        // Needs updated to handle removing x items, similar to adding
        boolean removed = ITEMS.removeIf(product -> product.id().equalsIgnoreCase(productId));
        System.out.println(removed ? "\nProduct removed from cart." : "\nNo product found with ID: " + productId + " found in cart.");
        displayCartContents(scanner);
    }

    @Override
    public String toString() {
        return generatedReceiptOutput().toString();
    }

    public boolean isEmpty(){
        return (ITEMS.isEmpty());
    }

    public void checkOutCurrentCart(Scanner scanner) {
        if (isEmpty()) {
            System.out.println("\nYour cart is empty. Please add items to your cart before checking out.\n");
            return;
        }

        if(InputManager.promptUserConfirmation(scanner,this)){
            tryProcessPayment(InputManager.getUserPayment(scanner));
        }
    }

    private StringBuilder generatedReceiptOutput(){
        StringBuilder outString = new StringBuilder();
        HashMap<Product, Integer> currentCounts = new HashMap<>();
        totalAmount = 0.0;

        ITEMS.forEach(product -> {
            totalAmount += product.price();
            currentCounts.put(product, currentCounts.getOrDefault(product, 0) + 1);
        });

        currentCounts.forEach((key, value) -> outString.append("[x").append(value).append("] ").append(key.toString()).append("\n"));
        outString.append(String.format("\nTotal amount: $%.2f", totalAmount));
        return outString;
    }

    private void tryProcessPayment(double paymentAmount){
        if (paymentAmount >= totalAmount) {
            successfulPaymentProcess((paymentAmount));
        } else {
            System.out.println("\nPurchase canceled: Insufficient Funds");
        }
    }

    private void successfulPaymentProcess(double paymentAmount){
        double change = paymentAmount - totalAmount;

        String receiptOut = this +
                String.format("\nPayment Amount: $%.2f", paymentAmount) +
                (change == 0.0 ? "\n" : String.format("\nChange Due: $%.2f", change));

        System.out.println("\n"+receiptOut);
        saveNewReceipt(receiptOut);

        totalAmount = 0.0;
        ITEMS.clear();

        System.out.println("\nPurchase successful!");
    }

    private Product searchCartForId(String id, ArrayList<Product> inventory) {
        return inventory.stream()
                .filter(product -> product.id().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    private static void saveNewReceipt(String newReceiptOutput){
        try{
            String targetPathFull = FOLDER_PATH+getCurrentDatestamp()+".txt";
            createNewFile(targetPathFull);
            writeToFile(targetPathFull,"Transaction Receipt "+getCurrentDatestamp()+"\n\nPurchased Item(s):\n"+newReceiptOutput);
        }catch(Exception e){
            System.out.println("\nError saving receipt:\n"+e);
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
            System.out.println("\nError writing to file "+targetFilePath+" "+e);
        }
    }

    private static String getCurrentDatestamp(){
        DateTimeFormatter stampFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDateTime now = LocalDateTime.now();
        return now.format(stampFormat);
    }
}
