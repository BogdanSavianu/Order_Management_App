package bll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import bll.validators.AgeValidator;
import bll.validators.EmailValidator;
import bll.validators.Validator;
import dao.CustomerDAO;
import model.Customer;

/**
 * The CustomerBLL class represents the business logic layer for managing Customer entities.
 * It provides methods for finding, creating, updating, and deleting Customer objects.
 * This class also encapsulates validation logic for Customer objects using a list of validators.
 */
public class CustomerBLL {

    private List<Validator<Customer>> validators;
    private CustomerDAO customerDAO;

    /**
     * Constructs a new CustomerBLL object.
     * Initializes the list of validators with default validators for Customer objects.
     * Instantiates a CustomerDAO object for data access operations.
     */
    public CustomerBLL() {
        validators = new ArrayList<Validator<Customer>>();
        validators.add(new EmailValidator());
        validators.add(new AgeValidator());

        customerDAO = new CustomerDAO();
    }

    /**
     * Finds a customer by the specified ID.
     *
     * @param id The ID of the customer to find.
     * @return The Customer object if found.
     * @throws NoSuchElementException If no customer with the specified ID is found.
     */
    public Customer findCustomerById(int id) {
        Customer st = customerDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The customer with id = " + id + " was not found!");
        }
        return st;
    }

    /**
     * Retrieves a list of all customers from the database.
     *
     * @return A list of Customer objects.
     * @throws NoSuchElementException If there are no customers in the database.
     */

    public List<Customer> findAll() {
        List<Customer> customers = customerDAO.findAll();
        if (customers == null) {
            throw new NoSuchElementException("There are no customers in the database!");
        }
        return customers;
    }

    /**
     * Creates a new customer in the database.
     *
     * @param customer The Customer object to be created.
     * @return The created Customer object.
     */
    public Customer createCustomer(Customer customer) {
        validators.forEach(validator -> validator.validate(customer));
        return customerDAO.insert(customer);
    }

    /**
     * Updates an existing customer in the database.
     *
     * @param customer The Customer object to be updated.
     * @return The updated Customer object.
     */
    public Customer update(Customer customer) {
        validators.forEach(validator -> validator.validate(customer));
        return customerDAO.update(customer);
    }

    /**
     * Deletes a customer from the database.
     *
     * @param customer The Customer object to be deleted.
     * @return The deleted Customer object.
     */
    public Customer delete(Customer customer) {
        return customerDAO.delete(customer);
    }
}
