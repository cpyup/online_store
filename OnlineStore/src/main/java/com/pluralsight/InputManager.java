package com.pluralsight;
import java.util.Scanner;

public class InputManager {

    /**
     * Displays the product menu, allowing users to add products to their cart,
     * search for products, or exit the menu.
     *
     * <p>This method shows all available products then prompts the user to enter
     * a product ID to add to the cart, 'S' to search for a product, or 'E' to exit.</p>
     *
     * @param targetInventory The list of available products. This list cannot be null.
     * @param scanner         The scanner for reading user input.
     * @param cart            The user's shopping cart to which products will be added.
     * @throws NullPointerException if {@code targetInventory} or {@code cart} is null.
     */
    public static void productMenuDisplay(Inventory targetInventory, Scanner scanner, Cart cart) {
        targetInventory.displayAllProducts();
        String input;
        do {
            System.out.print("\nEnter product ID to add to your cart, 'S' to search, or 'E' to exit: ");
            input = scanner.nextLine().trim();

            if ("S".equalsIgnoreCase(input)) {
                inventorySearchDisplay(targetInventory,scanner);
            } else if (!"E".equalsIgnoreCase(input)) {
                parseAddingInput(targetInventory,input,cart);
            }
        } while (!"E".equalsIgnoreCase(input));
    }

    /**
     * Prompts the user to enter a product ID to search for in the inventory.
     *
     * <p>This method reads the user input and displays the search results for the
     * specified product ID from the inventory.</p>
     *
     * @param targetInventory The list of available products. This list cannot be null.
     * @param scanner         The scanner for reading user input.
     * @throws NullPointerException if {@code targetInventory} is null.
     */
    public static void inventorySearchDisplay(Inventory targetInventory, Scanner scanner){
        System.out.print("\nEnter the ID to search for: ");
        String targetId = scanner.nextLine().trim();
        targetInventory.searchInventoryForId(targetId);
    }

    /**
     * Prompts the user for confirmation before proceeding with the checkout process.
     *
     * <p>This method displays the cart contents and asks the user to confirm their
     * intention to complete the purchase or to exit to the main menu.</p>
     *
     * @param scanner The scanner for reading user input.
     * @param cart    The user's shopping cart, containing items for purchase.
     * @return {@code true} if the user confirms the purchase, {@code false} if they choose to exit.
     */
    public static boolean promptUserConfirmation(Scanner scanner, Cart cart){
        System.out.print("\nCHECKOUT CART\n\n"+cart+"\n\nPress 'Enter' to continue with purchase or type 'E' to exit to the main menu.");
        String input = scanner.nextLine().trim();
        return !input.equalsIgnoreCase("E");
    }

    /**
     * Handles user input related to cart operations, such as checking out or removing items.
     *
     * <p>This method displays the current cart contents and provides options to
     * checkout, remove items from the cart, or exit to the main menu.</p>
     *
     * @param scanner The scanner for reading user input.
     * @param cart    The user's shopping cart, containing items.
     */
    public static void handleCartInput(Scanner scanner, Cart cart) {
        cart.displayCartContents();
        if(cart.isEmpty()) return;

        String input;
        do {
            System.out.println("\nOptions:\n\tC - Checkout\n\tR - Remove From Cart\n\tE - Exit to the main menu");
            input = scanner.nextLine().trim();
            if ("C".equalsIgnoreCase(input)) {
                if(promptUserConfirmation(scanner,cart)){
                    cart.checkOutCurrentCart(getUserPayment(scanner));
                }
                return;
            } else if ("R".equalsIgnoreCase(input)) {
                removeCartItemInput(scanner,cart);
                if(cart.isEmpty())return;
            }
        } while (!"E".equalsIgnoreCase(input));
    }

    /**
     * Prompts the user to enter a product ID to remove from the cart.
     *
     * <p>This method reads the user input and calls the cart's method to remove the specified product.</p>
     *
     * @param scanner The scanner for reading user input.
     * @param cart    The user's shopping cart from which to remove the product.
     */
    public static void removeCartItemInput(Scanner scanner, Cart cart){
        System.out.print("\nEnter product ID to remove: ");
        String productId = scanner.nextLine().trim();
        cart.removeProductFromCart(productId,1);
    }

    /**
     * Prompts the user to enter the payment amount for the purchase.
     *
     * <p>This method reads the user input for the payment amount and returns it as a double.</p>
     *
     * @param scanner The scanner for reading user input.
     * @return The amount of cash provided by the user for the purchase.
     */
    public static double getUserPayment(Scanner scanner){
        System.out.print("\nEnter amount of cash for purchase: ");
        double paymentAmount = scanner.nextDouble();
        scanner.nextLine();
        return paymentAmount;
    }

    /**
     * Parses the input string for adding products to the cart.
     *
     * <p>This method splits the input into product ID and quantity, and adds the product
     * to the cart. If the quantity is not specified, it defaults to 1.</p>
     *
     * @param targetInventory The list of available products. This list cannot be null.
     * @param input          The user input containing the product ID and optional quantity.
     * @param cart           The user's shopping cart to which the product will be added.
     * @throws NullPointerException if {@code targetInventory} or {@code cart} is null.
     */
    private static void parseAddingInput(Inventory targetInventory,String input, Cart cart){
        String[] args;
        try{
            args = input.split(" ");
            int itemCount = args.length > 1 ? Integer.parseInt(args[1]) : 1;
            cart.addProductToCart(args[0], targetInventory, Math.max(itemCount, 1));
        }catch (NumberFormatException e) {
            System.out.println("\nError Parsing Item Amount\nExpected Input: item id [String] item amount [int]");
        }
    }
}
