package bll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import bll.validators.EmailValidator;
import bll.validators.PriceValidator;
import bll.validators.QuantityValidator;
import bll.validators.Validator;
import dao.ProductDAO;
import model.Product;

/**
 * The ProductBLL class represents the business logic layer for managing Product entities.
 * It provides methods for finding, creating, updating, and deleting Product objects.
 */
public class ProductBLL {

    private List<Validator<Product>> validators;
    private ProductDAO productDAO;

    /**
     * Constructs a new ProductBLL object.
     * Instantiates a ProductDAO object for data access operations.
     */
    public ProductBLL() {
        validators = new ArrayList<Validator<Product>>();
        validators.add(new QuantityValidator());
        validators.add(new PriceValidator());

        productDAO = new ProductDAO();
    }

    /**
     * Finds a product by the specified ID.
     *
     * @param id The ID of the product to find.
     * @return The Product object if found.
     * @throws NoSuchElementException If no product with the specified ID is found.
     */
    public Product findProductById(int id) {
        Product st = productDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The product with id = " + id + " was not found!");
        }
        return st;
    }

    /**
     * Retrieves a list of all products from the database.
     *
     * @return A list of Product objects.
     * @throws NoSuchElementException If there are no products in the database.
     */
    public List<Product> findAll() {
        List<Product> products = productDAO.findAll();
        if(products == null) {
            throw new NoSuchElementException("The!");
        }
        return products;
    }

    /**
     * Creates a new product in the database.
     *
     * @param product The Product object to be created.
     * @return The created Product object.
     */
    public Product create(Product product) {
        validators.forEach(validator -> validator.validate(product));
        return productDAO.insert(product);
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product The Product object to be updated.
     * @return The updated Product object.
     */
    public Product update(Product product) {
        validators.forEach(validator -> validator.validate(product));
        return productDAO.update(product);
    }

    /**
     * Deletes a product from the database.
     *
     * @param product The Product object to be deleted.
     * @return The deleted Product object.
     */
    public Product delete(Product product) {
        return productDAO.delete(product);
    }
}
