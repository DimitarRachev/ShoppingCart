import Entities.Product;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCart {
    private static ShoppingCart shoppingCart = new ShoppingCart();
    private   Map<Product, Integer> products;

    private ShoppingCart() {
        products = new LinkedHashMap<>();
    }

    public static ShoppingCart getInstance() {
        if (shoppingCart == null) {
            return new ShoppingCart();
        } else {
            return shoppingCart;
        }
    }
    public  void addProductToCart(Product product)  {
        if (product.isAvailable()) {
            products.putIfAbsent(product, 0);
            products.put(product, products.get(product) + 1);
            product.setUnitsAvailable(product.getUnitsAvailable() - 1);
        }
    }

    public  boolean removeBookFromCart(Product product) {
        boolean isRemoved = false;
        int count = products.get(product);
        if (count <= 1) {
            products.remove(product);
            isRemoved = true;
        } else {
            products.put(product, products.get(product) - 1);
        }
        product.setUnitsAvailable(product.getUnitsAvailable() + 1);
        return isRemoved;
    }

    public  int getTotal() {
        int sum = 0;
        for (var entry : products.entrySet()) {
            sum += entry.getKey().getCost() * entry.getValue();
        }
        return sum;
    }

    public  Map<Product, Integer> checkout() {
        Map<Product, Integer> order = new HashMap<>();
        order.putAll(products);

        products.clear();

        return order;
    }

    public  int getSize() {
        return products.size();
    }

    public  Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }
}
