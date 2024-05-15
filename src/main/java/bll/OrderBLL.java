package bll;

import java.util.List;
import java.util.NoSuchElementException;

import bll.validators.Validator;
import dao.OrderDAO;
import model.Orders;

/**
 * The OrderBLL class represents the business logic layer for managing Orders entities.
 * It provides methods for finding, creating, updating, and deleting Order objects.
 */
public class OrderBLL {

    private List<Validator<Orders>> validators;
    private OrderDAO orderDAO;

    /**
     * Constructs a new OrderBLL object.
     * Instantiates an OrderDAO object for data access operations.
     */
    public OrderBLL() {
        //validators = new ArrayList<Validator<Product>>();
        //validators.add(new EmailValidator());

        orderDAO = new OrderDAO();
    }

    /**
     * Finds an order by the specified ID.
     *
     * @param id The ID of the order to find.
     * @return The Orders object if found.
     * @throws NoSuchElementException If no order with the specified ID is found.
     */
    public Orders findOrderById(int id) {
        Orders st = orderDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The order with id = " + id + " was not found!");
        }
        return st;
    }

    /**
     * Retrieves a list of all orders from the database.
     *
     * @return A list of Orders objects.
     * @throws NoSuchElementException If there are no orders in the database.
     */
    public List<Orders> findAll() {
        List<Orders> orders = orderDAO.findAll();
        if (orders == null) {
            throw new NoSuchElementException("There are no orders found!");
        }
        return orders;
    }

    /**
     * Creates a new order in the database.
     *
     * @param order The Orders object to be created.
     * @return The created Orders object.
     */
    public Orders createOrder(Orders order) {
        return orderDAO.insert(order);
    }

    /**
     * Updates an existing order in the database.
     *
     * @param order The Orders object to be updated.
     * @return The updated Orders object.
     */
    public Orders update(Orders order) {
        return orderDAO.update(order);
    }

    /**
     * Deletes an order from the database.
     *
     * @param order The Orders object to be deleted.
     * @return The deleted Orders object.
     */
    public Orders delete(Orders order) {
        return orderDAO.delete(order);
    }
}
