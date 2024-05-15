package dao;

import connection.ConnectionFactory;
import model.Product;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * The ProductDAO class provides data access methods specific to the Product entity.
 * It extends the AbstractDAO class, inheriting generic CRUD (Create, Read, Update, Delete) operations
 * for Product objects.
 */
public class ProductDAO extends AbstractDAO<Product> {

}
