package com.pluralsight.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class Receipt {
    private static final String FOLDER_PATH = "receipts\\";
    private double totalCost;
    private final double paymentAmount;
    private final List<Product> cartItems;

    public Receipt(double totalCost, double paymentAmount, List<Product> cartItems){
        this.totalCost = totalCost;
        this.paymentAmount = paymentAmount;
        this.cartItems = cartItems;
    }

    public Receipt(double totalCost, List<Product> cartItems){
        this.totalCost = totalCost;
        this.paymentAmount = 0;
        this.cartItems = cartItems;
    }

    public double getChange(){
        return calculateChange();
    }

    public void saveNewReceipt(Receipt receipt) {
        try {
            String targetPathFull = FOLDER_PATH + getCurrentDatestamp() + ".txt";
            createNewFile(targetPathFull);
            writeToFile(targetPathFull, "Transaction Receipt " + getCurrentDatestamp() + "\n\nPurchased Item(s):\n" + receipt);
        } catch (Exception e) {
            System.out.println("\nError saving receipt:\n" + e.getMessage());
        }
    }

    /**
     * Returns a string representation of the shopping cart's contents.
     *
     * <p>This method returns a string formatted for display as a receipt.</p>
     *
     * @return A string representing the current contents of the cart.
     */
    @Override
    public String toString(){
        return generateReceiptOutput();
    }

    private double calculateChange(){
        return paymentAmount - totalCost;
    }

    /**
     * Creates a new file with the specified name.
     *
     * <p>If the file already exists, a message indicating this will be printed.</p>
     *
     * @param fileName The name of the file to create.
     * @throws RuntimeException if an I/O error occurs while creating the file.
     */
    private void createNewFile(String fileName) {
        try {
            File newFile = new File(fileName);
            System.out.println("\nFile '" + fileName + "' " + (newFile.createNewFile() ? "Successfully Created" : "Already Exists"));
        } catch (IOException ex) {
            System.out.println("Error Creating File " + fileName);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Writes the specified data to a file at the given path.
     *
     * <p>This method uses a buffered writer to write data to the file.</p>
     *
     * @param targetFilePath The path of the file where data will be written.
     * @param dataToWrite    The data to write to the file.
     */
    private void writeToFile(String targetFilePath, String dataToWrite) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetFilePath))) {
            bufferedWriter.write(dataToWrite);
            bufferedWriter.flush();
        } catch (Exception e) {
            System.out.println("\nError writing to file " + targetFilePath + " " + e.getMessage());
        }
    }

    /**
     * Retrieves the current date and time formatted as a string.
     *
     * <p>The date and time are formatted in the pattern "yyyyMMddHHmm", where:
     * <ul>
     *   <li>yyyy: Year</li>
     *   <li>MM: Month</li>
     *   <li>dd: Day of the month</li>
     *   <li>HH: Hour in 24-hour format</li>
     *   <li>mm: Minutes</li>
     * </ul>
     * This method is used for generating unique filenames for receipts.</p>
     *
     * @return A string representing the current date and time in the specified format.
     */
    private String getCurrentDatestamp() {
        DateTimeFormatter stampFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDateTime now = LocalDateTime.now();
        return now.format(stampFormat);
    }

    /**
     * Generates a formatted string representing the receipt for the current cart contents.
     *
     * <p>The receipt includes each product, the quantity purchased, and the total amount.
     * Note: Receipts will include the date of the purchase in future implementations.</p>
     *
     * @return A string representation of the receipt for the cart.
     */
    private String generateReceiptOutput() { // TODO: Receipts need to include the date of the purchase at the top
        StringBuilder outString = new StringBuilder();
        HashMap<Product, Integer> currentCounts = new HashMap<>();
        totalCost = 0.0;

        for (Product product : cartItems) {
            totalCost += product.price();
            currentCounts.put(product, currentCounts.getOrDefault(product, 0) + 1);
        }

        currentCounts.forEach((product, count) -> outString.append(String.format("[x%d] %s%n", count, product.toString())));
        outString.append(String.format("\nTotal amount: $%.2f", totalCost));
        if(paymentAmount > 0.0){
            outString.append(String.format("\nPayment Amount: $%.2f\nChange Due: $%.2f", paymentAmount,getChange()));
        }
        return outString.toString();
    }
}
