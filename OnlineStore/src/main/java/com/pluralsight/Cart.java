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

public class Cart { // TODO: Move methods related to external files to new class. If time allows, move everything related to receipts to another class too.

    private static final String FOLDER_PATH = "receipts\\";
    private final List<Product> items = new ArrayList<>();
    private double totalAmount;

    public Cart() {
        totalAmount = 0.0;
    }

    /**
     * Adds a specified quantity of a product to the shopping cart.
     *
     * <p>This method searches for the product in the inventory using its ID. If the product
     * is found, it adds the specified number of items to the cart and displays a confirmation message.
     * If the product is not found, it informs the user.</p>
     *
     * @param productId     The ID of the product to add to the cart.
     * @param inventory     The list of available products to search in. This list cannot be null.
     * @param amount  The number of units of the product to add to the cart.
     * @throws NullPointerException if {@code inventory} is null.
     */
    public void addProductToCart(String productId, Inventory inventory, int amount) {
        Product product = searchForProductInInventory(productId, inventory);

        if (product != null) {
            for (int i = 0; i < amount; i++) {
                items.add(product);
            }
            System.out.printf("\n%s %s been added to your cart.%n", amount > 1 ? (amount + " " + product.name() + "s") : product.name(), amount > 1 ? "have" : "has");
        } else {
            System.out.printf("\nProduct with ID '%s' not found.%n", productId);
        }
    }

    /**
     * Displays the contents of the shopping cart.
     *
     * <p>If the cart is empty, a message indicating this will be printed. Otherwise,
     * the method will generate and display a detailed receipt of the items in the cart.</p>
     */
    public void displayCartContents() {
        if (isEmpty()) {
            System.out.println("\nYour cart is empty.");
            return;
        }
        System.out.println("\nYour cart items:\n" + generateReceiptOutput());
    }

    /**
     * Removes a specified quantity of a product from the shopping cart.
     *
     * <p>This method searches for the product in the cart using its ID. If the product
     * is found, it removes the specified number of units. If the quantity is invalid or
     * the product is not found, it informs the user accordingly.</p>
     *
     * @param productId The ID of the product to remove from the cart.
     * @param amount  The number of units of the product to remove. Must be greater than zero.
     */
    public void removeProductFromCart(String productId, int amount) {
        amount = Math.max(amount,1);

        for (int i = 0; i < amount; i++) {  // TODO: Make this work for removing multiple, possibly later merge with adding
            if (!items.removeIf(product -> product.id().equalsIgnoreCase(productId))) {
                System.out.printf("\nNo product found with ID: %s in cart.%n", productId);
                return;
            }
        }
        System.out.println("\nProduct removed from cart.");
        displayCartContents();
    }

    /**
     * Checks if the shopping cart is empty.
     *
     * @return {@code true} if the cart contains no items, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Processes the checkout for the current shopping cart.
     *
     * <p>If the cart is empty, an error message is displayed. Otherwise, it processes the payment
     * using the provided amount.</p>
     *
     * @param paymentAmount The amount of money used to pay for the cart items.
     */
    public void checkOutCurrentCart(double paymentAmount) {
        if (isEmpty()) {
            System.out.println("\nYour cart is empty. Please add items to your cart before checking out.\n");
            return;
        }
        processPayment(paymentAmount);
    }

    /**
     * Returns a string representation of the shopping cart's contents.
     *
     * <p>This method returns a string formatted for display as a receipt.</p>
     *
     * @return A string representing the current contents of the cart.
     */
    @Override
    public String toString() {
        return generateReceiptOutput();
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
        totalAmount = 0.0;

        for (Product product : items) {
            totalAmount += product.price();
            currentCounts.put(product, currentCounts.getOrDefault(product, 0) + 1);
        }

        currentCounts.forEach((product, count) -> outString.append(String.format("[x%d] %s%n", count, product.toString())));
        outString.append(String.format("\nTotal amount: $%.2f", totalAmount));
        return outString.toString();
    }

    /**
     * Processes the payment for the current cart contents.
     *
     * <p>If the payment amount is sufficient, the payment is completed. Otherwise, an error
     * message is displayed indicating insufficient funds.</p>
     *
     * @param paymentAmount The amount of money used to pay for the cart items.
     */
    private void processPayment(double paymentAmount) {
        if (paymentAmount >= totalAmount) {
            completePayment(paymentAmount);
        } else {
            System.out.println("\nPurchase canceled: Insufficient Funds");
        }
    }

    /**
     * Completes the payment process for a valid transaction and generates a receipt.
     *
     * <p>This method calculates the change due and prints the receipt, including the payment amount
     * and the total amount spent. After display the receipt, it clears the current cart.</p>
     *
     * @param paymentAmount The amount of money used to pay for the cart items.
     */
    private void completePayment(double paymentAmount) {
        double change = paymentAmount - totalAmount;
        String receiptOut = generateReceiptOutput() + String.format("\nPayment Amount: $%.2f", paymentAmount) +
                (change == 0.0 ? "\n" : String.format("\nChange Due: $%.2f", change));

        System.out.println("\n" + receiptOut+"\nPurchase successful!");
        saveNewReceipt(receiptOut);
        clearCartItems();
    }

    /**
     * Clears all items from the shopping cart and resets the total amount.
     *
     * <p>This method removes all products from the cart and sets the total amount
     * value to zero. It is typically used after completing a purchase or when
     * the user decides to empty the cart.</p>
     */
    private void clearCartItems(){
        totalAmount = 0.0;
        items.clear();
    }

    /**
     * Searches for a product in the inventory by its ID.
     *
     * <p>This method performs a case-insensitive search for a product in the provided inventory list.</p>
     *
     * @param id        The ID of the product to search for.
     * @param inventory The list of available products to search in. This list cannot be null.
     * @return The {@link Product} if found, or null if not found.
     * @throws NullPointerException if {@code inventory} is null.
     */
    private Product searchForProductInInventory(String id, Inventory inventory) { // TODO: This should be in inventory
        return inventory.getCurrentInventory().stream()
                .filter(product -> product.id().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Saves a new receipt to a file.
     *
     * <p>This method attempts to create a new file for the receipt using the current date and time
     * as the filename. If successful, it writes the receipt data to the file.</p>
     *
     * @param newReceiptOutput The receipt data to be saved to the file.
     */
    private void saveNewReceipt(String newReceiptOutput) {
        try {
            String targetPathFull = FOLDER_PATH + getCurrentDatestamp() + ".txt";
            createNewFile(targetPathFull);
            writeToFile(targetPathFull, "Transaction Receipt " + getCurrentDatestamp() + "\n\nPurchased Item(s):\n" + newReceiptOutput);
        } catch (Exception e) {
            System.out.println("\nError saving receipt:\n" + e.getMessage());
        }
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
}
