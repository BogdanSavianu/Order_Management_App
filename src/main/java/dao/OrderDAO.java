package dao;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import connection.ConnectionFactory;
import model.Orders;
import model.Product;
import singlePointAccess.RepoSinglePointAccess;

import static java.lang.StringTemplate.STR;

/**
 * The OrderDAO class provides data access methods specific to the Orders entity.
 * It extends the AbstractDAO class, inheriting generic CRUD (Create, Read, Update, Delete) operations
 * for Orders objects.
 */
public class OrderDAO extends AbstractDAO<Orders> {

}
