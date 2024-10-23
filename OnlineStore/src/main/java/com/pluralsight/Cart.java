package com.pluralsight;


import java.util.ArrayList;
import java.util.List;

public class Cart { // TODO: Move methods related to external files to new class. If time allows, move everything related to receipts to another class too.
    private final List<Product> items = new ArrayList<>();
    private double totalAmount;

    public Cart() {
        calculateAmount();
    }

    public String getTotalAmount(){
        calculateAmount();
        return String.format("%.2f",totalAmount);
    }

    private void calculateAmount(){
        totalAmount = 0;
        items.forEach(product -> totalAmount += product.price());
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
        Receipt cartReceipt = new Receipt(totalAmount,items);

        System.out.println("\nYour cart items:\n" + cartReceipt);
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
     * Processes the payment for the current cart contents.
     *
     * <p>If the payment amount is sufficient, the payment is completed. Otherwise, an error
     * message is displayed indicating insufficient funds.</p>
     *
     * @param paymentAmount The amount of money used to pay for the cart items.
     */
    private void processPayment(double paymentAmount) { // TODO: Maybe create receipt here then pass to complete?
        if (paymentAmount >= totalAmount) {
            Receipt receipt = new Receipt(totalAmount,paymentAmount,items);
            completePayment(receipt);
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
     */
    private void completePayment(Receipt receipt) {
        System.out.println("\n" + receipt+"\nPurchase successful!");
        receipt.saveNewReceipt(receipt);
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


}
