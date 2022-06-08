
import Entities.Fruit;
import Entities.Book;
import Entities.Product;
import Entities.Vegetable;

import java.io.*;
import java.util.*;

public class Menu {

    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";
    private final Scanner scanner = new Scanner(System.in);
    private ProductManager productManager;

    public void run() throws IOException {
        System.out.println("Welcome");
        while (true) {
            printWelcomeMsg();
        }
    }

    private void printWelcomeMsg() throws IOException {
        System.out.println("Please choose action :");
        System.out.println("1. Choose category");
        if (ShoppingCart.getInstance().getSize() > 0) {
            System.out.println("2.View cart");
        }
        System.out.println("3. Exit");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                chooseCategory();
                break;
            case "2":
                if (ShoppingCart.getInstance().getSize() > 0) {
                    viewCart();
                }
                break;
            case "3":
                if (ShoppingCart.getInstance().getProducts().size() > 0) {
                    saveShoppingCartToFile();
                }
                System.exit(0);
        }
    }

    private void saveShoppingCartToFile() throws IOException {
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream("./products.cart"));
        out.writeObject(ShoppingCart.getInstance().getProducts());
    }

    private void chooseCategory() {
        System.out.println("1 -> Books, 2 -> Fruits, 3 -> Vegetables, any key -> previous menu");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                productManager = new ProductManager();
                productBrowseMenu(productManager.getNext6Products(Book.class));
                break;
            case "2":
                productManager = new ProductManager();
                productBrowseMenu(productManager.getNext6Products(Fruit.class));
                break;
            case "3":
                productManager = new ProductManager();
                productBrowseMenu(productManager.getNext6Products(Vegetable.class));
        }
    }

    private void productBrowseMenu(List<Product> products) {
        for (int i = 0; i < 6 && i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i));
        }
        System.out.println("1-6 -> Add product to cart,7 -> previous products, 8 -> next products, 9 -> Exit to main menu");
        String input = scanner.nextLine().trim();
        switch (input) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
                Product product = products.get(Integer.parseInt(input) - 1);
                if (productManager.availableUnits(product) > 0) {
                    ShoppingCart.getInstance().addProductToCart(product);
                    productManager.updateProductCount(product);
                    System.out.println(TEXT_RED + " Product " + product.getName() + " added to cart." + TEXT_RESET);

                } else {
                    System.out.println(TEXT_RED + " Product " + product.getName() + " is unavailable." + TEXT_RESET);
                }
                productBrowseMenu(products);
                break;
            case "7":
                productBrowseMenu(productManager.getPrevious6Products(products.get(0).getClass()));
                break;
            case "8":
                productBrowseMenu(productManager.getNext6Products(products.get(0).getClass()));
                break;
        }
    }

    private void viewCart() {
        List<Map.Entry<Product, Integer>> iterableProducts = new ArrayList<>();
        ShoppingCart.getInstance().getProducts().entrySet().forEach(iterableProducts::add);

        int index = 6;
        int start = 0;
        int end = 5;

        while (true) {
            print6BooksFromCart(iterableProducts, start, end);

            System.out.println("1-6 -> edit product 7 -> previous product 8-> next products 9-> Get total, 10 -> checkout, any key -> main menu");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":

                    Map.Entry<Product, Integer> entry = iterableProducts.get(start + Integer.parseInt(input) - 1);
                    editProductInCart(entry, iterableProducts);
                    viewCart();
                    return;
                case "7":    //print previous 6
                    end = index - 7;
                    start = end - 5;
                    index = end + 1;
                    if (start < 0) {
                        start = 0;
                        end = start + 5;
                        index = end + 1;
                        if (end >= iterableProducts.size()) {
                            end = iterableProducts.size() - 1;
                        }
                    }
//                    print6BooksFromCart(iterableProducts, start, end);
                    break;
                case "8":   //print next 6
                    start = end + 1;
                    end = start + 5;
                    index = end + 1;
                    if (end >= iterableProducts.size()) {
                        end = index = iterableProducts.size() - 1;
                        start = end - 5;
                        if (start < 0) {
                            start = 0;
                        }
                    }
//                    print6BooksFromCart(iterableProducts, start, end);
                    break;
                case "9":   //print getTotal
                    System.out.println("Total: $" + ShoppingCart.getInstance().getTotal());
                    viewCart();
                    break;
                case "10":  //do checkout
                    Map<Product, Integer> checkout = ShoppingCart.getInstance().checkout();
                    System.out.println("Purchased items:");
                    if (checkout.size() == 0) {
                        System.out.println("None");
                    }
                    int sum = 0;
                    for (var entry1 : checkout.entrySet()) {
                        System.out.println(String.format("%s %-40s",
                                entry1.getKey(),
                                (TEXT_RED + " Count: " + TEXT_RESET + entry1.getValue())));
                        sum += entry1.getValue() * entry1.getKey().getCost();
                    }
                    System.out.println("Total: " + sum);
                    System.exit(0);
                    break;
                default:
                    return;
            }
        }
    }

    private void editProductInCart(Map.Entry<Product, Integer> entry, List<Map.Entry<Product, Integer>> iterableProducts) {

        System.out.println("Product: " + entry.getKey() + " Count: " + entry.getValue());
        System.out.println("1 -> add another 2 -> remove one from cart, "
                + (entry.getKey() instanceof Book ? "3 -> view other books from that author," : "")
                + "  anything else -> previous menu");

        String input = scanner.nextLine();
        switch (input) {
            case "1":
                if (productManager.availableUnits(entry.getKey()) > 0) {
                    ShoppingCart.getInstance().addProductToCart(entry.getKey());
                    productManager.updateProductCount(entry.getKey());
                } else {
                    System.out.println("Product " + entry.getKey().getName() + " unavailable.");
                }
                break;
            case "2":
                if (ShoppingCart.getInstance().removeBookFromCart(entry.getKey())) {
                    iterableProducts.remove(entry);
                }
                productManager.updateProductCount(entry.getKey());
                break;

            case "3":
                if (entry.getKey() instanceof Book book) {
                    while (true) {
                        List<Product> allBooksFromAuthor = productManager.findAllBooksFromAuthor(book);
                        for (int i = 0; i < allBooksFromAuthor.size(); i++) {
                            System.out.println(i + 1 + "." + allBooksFromAuthor.get(i));
                        }
                        System.out.println("Enter number of the book to add it to the cart of any other to exit to previous menu");
                        String s = scanner.nextLine();
                        try {
                            int index = Integer.parseInt(s) - 1;
//
                            Product toAdd = allBooksFromAuthor.get(index);
                            ShoppingCart.getInstance().addProductToCart(toAdd);
                            productManager.updateProductCount(toAdd);
//
                        } catch (Exception e) {
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void print6BooksFromCart(List<Map.Entry<Product, Integer>> iterableProducts, int start, int end) {
        List<Map.Entry<Product, Integer>> temp = new ArrayList<>();
        if (iterableProducts.size() == 0) {
            System.out.println("None");
        }
        for (int i = start; i <= end && i < iterableProducts.size(); i++) {
            temp.add(iterableProducts.get(i));
        }
        for (int j = 0; j < temp.size(); j++) {
            Product product = temp.get(j).getKey();
            int count = temp.get(j).getValue();
            System.out.println(String.format("%-60s %-60s %s",
                    (j + 1) + ". " + product.getName(),
                    (" $" + product.getCost()),
                    (" Count:" + count)));
        }
    }
}
