package com.pluralsight;

import java.util.ArrayList;
import java.util.Scanner;

public class InputManager {

    public static void productMenuDisplay(ArrayList<Product> targetInventory, Scanner scanner, Cart cart) {
        String input;
        do {
            System.out.print("\nEnter product ID to add to your cart, 'S' to search, or 'E' to exit: ");
            input = scanner.nextLine().trim();

            if ("S".equalsIgnoreCase(input)) {
                InputManager.inventorySearchDisplay(targetInventory,scanner);
            } else if (!"E".equalsIgnoreCase(input)) {
                parseAddingInput(targetInventory,input,cart);
            }
        } while (!"E".equalsIgnoreCase(input));
    }
    public static void inventorySearchDisplay(ArrayList<Product> targetInventory, Scanner scanner){
        System.out.print("\nEnter the ID to search for: ");
        String targetId = scanner.nextLine().trim();
        InventoryManager.searchInventoryForId(targetInventory,targetId);
    }

    public static boolean promptUserConfirmation(Scanner scanner, Cart cart){
        System.out.print("\nCHECKOUT CART\n\n"+cart+"\n\nPress 'Enter' to continue with purchase or type 'E' to exit to the main menu.");
        String input = scanner.nextLine().trim();
        return !input.equalsIgnoreCase("E");
    }

    public static void handleCartInput(Scanner scanner, Cart cart) {  // Should be moved to a new input class
        String input;
        do {
            System.out.println("\nOptions:\n\tC - Checkout\n\tR - Remove From Cart\n\tE - Exit to the main menu");
            input = scanner.nextLine().trim();
            if ("C".equalsIgnoreCase(input)) {
                cart.checkOutCurrentCart(scanner);
                return;
            } else if ("R".equalsIgnoreCase(input)) {
                removeCartItemInput(scanner,cart);
                if(cart.isEmpty())return;
            }
        } while (!"E".equalsIgnoreCase(input));
    }

    public static void removeCartItemInput(Scanner scanner, Cart cart){
        System.out.print("\nEnter product ID to remove: ");
        String productId = scanner.nextLine().trim();
        cart.removeProductFromCart(productId, scanner);
    }

    public static double getUserPayment(Scanner scanner){
        System.out.print("\nEnter amount of cash for purchase: ");
        double paymentAmount = scanner.nextDouble();
        scanner.nextLine();
        return paymentAmount;
    }

    private static void parseAddingInput(ArrayList<Product> targetInventory,String input, Cart cart){
        String[] args;
        try{
            args = input.split(" ");
            int itemCount = args.length > 1 ? Integer.parseInt(args[1]) : 1;
            cart.addProductToCart(args[0], targetInventory, Math.max(itemCount, 1));
        }catch (NumberFormatException e) {
            System.out.println("Error Parsing Item Count\nExpected Input: item id [String] item amount [int]");
        }
    }
}
