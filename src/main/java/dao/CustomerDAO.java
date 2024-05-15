package dao;

import java.sql.*;
import java.lang.reflect.Field;
import java.util.logging.Level;

import connection.ConnectionFactory;
import model.Customer;

/**
 * The CustomerDAO class provides data access methods specific to the Customer entity.
 * It extends the AbstractDAO class, inheriting generic CRUD (Create, Read, Update, Delete) operations
 * for Customer objects.
 */
public class CustomerDAO extends AbstractDAO<Customer> {

}
