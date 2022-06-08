import Entities.Author;
import Entities.Book;
import Entities.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class ProductManager {
    private int currentIndex;
    private int maxBooksIndex;
    private final EntityManager entityManager;
    private int offset = -6;

    public ProductManager() {
        this.currentIndex = 1;
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("Books");
        this.entityManager = factory.createEntityManager();

    }

    private int getMaxBookIndex(Class clazz) {
        Query query = entityManager.createQuery("SELECT COUNT(b) FROM " + clazz.getSimpleName() + " b ", Long.class);
        long count = (long) query.getSingleResult();
        return (int) count;
    }

    public List<Product> getNext6Products(Class clazz) {
        maxBooksIndex = getMaxBookIndex(clazz);
        offset += 6;
        if (offset > maxBooksIndex - 6) {
            offset = maxBooksIndex - 6;
        }
        return getProducts(offset, clazz);
    }

    public List<Product> getPrevious6Products(Class clazz) {
        offset -= 6;
        if (offset < 0) {
            offset = 0;
        }
        return getProducts(offset, clazz);
    }

    private List<Product> getProducts(int offset, Class clazz) {
        Query query = entityManager.createQuery("SELECT b FROM " + clazz.getSimpleName() + " b ", clazz)
                .setMaxResults(6).setFirstResult(offset);
        List resultList = query.getResultList();
        return resultList;
    }

    public void updateProductCount(Product product) {
        entityManager.getTransaction().begin();
//        entityManager.persist(product);
        entityManager.merge(product);
        entityManager.getTransaction().commit();
    }

    public int availableUnits(Product product) {
        Query query = entityManager.createQuery("SELECT b.unitsAvailable FROM " + product.getClass().getSimpleName() + " b WHERE b.id = :id", Integer.class)
                .setParameter("id", product.getId());
        return (int) query.getSingleResult();
    }

    public List<Product> findAllBooksFromAuthor(Book book) {
        Query query = entityManager.createQuery("Select b.author FROM Book b WHERE b.id = :id", Author.class).setParameter("id", book.getId());
        Author result = (Author) query.getSingleResult();
        query = entityManager.createQuery("SELECT b FROM Book b WHERE b.author = :author")
                .setParameter("author", result);

        List list = query.getResultList();
        return list;
    }
}
