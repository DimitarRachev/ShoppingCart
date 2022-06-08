import Entities.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Path path = Path.of("./products.cart");
        if (args.length > 0) {
            path = Path.of(args[0]);
        }
        if (path.toFile().isFile()) {
            System.out.println(path.getParent());
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path.toString()));
            Map<Product, Integer> o = (Map<Product, Integer>) in.readObject();
            ShoppingCart.getInstance().setProducts(o);
        }

        Menu menu = new Menu();
        menu.run();
    }
}
