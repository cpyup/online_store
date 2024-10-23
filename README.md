# Online Store

## Project Summary

Online Store is a Java CLI application, implementing a mock (cash based) storefront. 

This application loads the products from the inventory.csv data file then displays them to the user. 

The user can then select which items to add to the cart and proceed to checkout.

## User Stories

User stories that guided the design of my application:

> As a user, I would like to be able to load the inventory data to the application from an external source, so the application is populated with the most current information.
>
> As a user, I would like to search the product inventory for the id of the product I am looking for so that I can easily locate it for adding to my cart.
>
> As a user, I would like to be able to process payment for the products currently in my cart, so that I can complete my purchase.
>
> As a user, I would like to have a receipt of my transaction saved to my computer and displayed after purchasing, so that I can retain for my financial records.
>
> As a user, I would like to to view the available products in a simple format inside the application so I can easily browse the current inventory.
>
> As a user, I would like the user interface to be consistent throughout the application so that there is never any confusion when navigating.
>
> As a user, I would like a way to view the items currently in my cart so that I can decide if I am ready to checkout.
>
> As a busy user, I would like to add multiple products to my cart at once, that way if I have to order more than one, I have less typing to do.
>
> As a user with memory issues, I would to be able to search for fragments of product IDs, not just the exact ID, so that if I cannot remember exactly what I am looking for, I have the ability to better narrow down my options.
>
> As a potential contributor to the project, I would like for the code to be as clean and efficient as possible, so that I can easily understand, navigate, and contribute.

## Setup Instructions

### Prerequisites

- IntelliJ IDEA: Ensure you have IntelliJ IDEA installed, which you can download from [here](https://www.jetbrains.com/idea/download/).
- Java SDK: Make sure Java SDK is installed and configured in IntelliJ.

### Running the Application in IntelliJ

Follow these steps to get your application running within IntelliJ IDEA:

1. Open IntelliJ IDEA.
2. Select "Open" and navigate to the directory where you cloned or downloaded the project.
3. After the project opens, wait for IntelliJ to index the files and set up the project.
4. Find the main class with the `public static void main(String[] args)` method.
5. Right-click on the file and select 'Run 'Store.main()'' to start the application.

## Technologies Used

- IntelliJ IDEA Community Edition 2022.3.2.0
- Java Version 17

## Additional Tools

- [Notepad++](https://notepad-plus-plus.org/) (README, General Code Edits)
- [GIMP](https://www.gimp.org/) (Cropping Screenshots)

## Example Outputs

### Adding Products To Cart:
![Adding To Cart](https://github.com/cpyup/online_store/blob/main/screenshots/add_to_cart.png?raw=true)

### Searching Products In Inventory:
![Inventory Search](https://github.com/cpyup/online_store/blob/main/screenshots/inventory_searching.png?raw=true)

### Checking Out Current Cart:
![Checkout Cart](https://github.com/cpyup/online_store/blob/main/screenshots/checkout_cart.png?raw=true)

### Error Examples:
![Error Examples](https://github.com/cpyup/online_store/blob/main/screenshots/error_examples.png?raw=true)

### Project Highlights

#### Bonus Conditions
> While my efforts were primarily focused on completing Capstone #1, I was still able to implement the workshop bonus (functioning checkout, saving receipts, displaying multiples as count).

#### Adding Multiple Products
> In addition to the bonus, I also implemented the option for adding multiple amounts of the same item at once, by parsing input for the optional 'amount' int.
```java
public void addProductToCart(String productId, Inventory inventory, int amount) {
        Product product = inventory.findSingleProductById(productId);

        if (product != null) {
            for (int i = 0; i < amount; i++) {
                items.add(product);
            }
            System.out.printf("\n%s %s been added to your cart.%n", amount > 1 ? (amount + " " + product.name() + "s") :
            product.name(), amount > 1 ? "have" : "has");
        } else {
            System.out.printf("\nProduct with ID '%s' not found.%n", productId);
        }
    }
```

## Future Work

### Cart Handling

> Removing products from the current cart should ideally function similarly to adding products to the cart, in that an optional amount parameter can be passed to the method, allowing a designated amount of that product to be removed. Currently, when removing an item, if there are multiple instances of that product every product of that id will be removed. This is fine for default behavior, but to maintain consistency, this should be implemented as an option.

## Resources

- [Google](www.google.com)

## Thanks

Special thanks to Raymond, for answering my questions at (seemingly) any hour of the day!