package dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;

/**
 * The AbstractDAO class serves as a generic Data Access Object (DAO) template for database operations.
 * It provides generic methods for common CRUD (Create, Read, Update, Delete) operations on entities
 * of a specific type.
 *
 * @param <T> the type of entity that this DAO operates on
 */

public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    /**
     * The Class object representing the type of entity handled by this DAO.
     */
    protected final Class<T> type;

    /**
     * Constructs a new AbstractDAO instance.
     * Retrieves the actual type argument at runtime using reflection.
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    /**
     * Generates a SELECT SQL query for retrieving records from the database based on a specific field value.
     *
     * @param field the name of the field to filter records by
     * @return the generated SELECT query
     */
    String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    /**
     * Retrieves all entities of type T from the database.
     *
     * @return a list of all entities retrieved from the database
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName();
        List<T> entities = new ArrayList<>();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            entities = createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return entities;
    }

    /**
     * Retrieves an entity of type T from the database by its ID.
     *
     * @param id the ID of the entity to retrieve
     * @return the retrieved entity, or null if no entity with the specified ID is found
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Creates and returns a list of entities based on the data retrieved from a ResultSet.
     *
     * @param resultSet the ResultSet containing the data retrieved from the database
     * @return a list of entities created from the ResultSet data
     */
    List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor<?>[] ctors = type.getDeclaredConstructors();
        Constructor<?> ctor = null;
        for (Constructor<?> constructor : ctors) {
            ctor = constructor;
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);

                    if (field.getType() == int.class) {
                        value = resultSet.getInt(fieldName);
                    } else if (field.getType() == float.class) {
                        value = resultSet.getFloat(fieldName);
                    } else if (field.getType() == Date.class) {
                        value = resultSet.getDate(fieldName);
                    }

                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SQLException | IntrospectionException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Inserts a new entity into the database.
     *
     * @param t the entity to insert
     * @return the inserted entity with its ID set (if applicable)
     */
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            Field[] fields = t.getClass().getDeclaredFields();

            StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
            queryBuilder.append(type.getSimpleName()).append(" (");
            for (int i = 0; i < fields.length; i++) {
                if (i > 0) {
                    queryBuilder.append(", ");
                }
                queryBuilder.append(fields[i].getName());
            }
            queryBuilder.append(") VALUES (");
            for (int i = 0; i < fields.length; i++) {
                if (i > 0) {
                    queryBuilder.append(", ");
                }
                queryBuilder.append("?");
            }
            queryBuilder.append(")");
            String query = queryBuilder.toString();

            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object value = fields[i].get(t);
                statement.setObject(i + 1, value);
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting entity failed, no rows affected.");
            }

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int generatedId = resultSet.getInt(1);
                Field idField = t.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(t, generatedId);
            } else {
                throw new SQLException("Inserting entity failed, no ID obtained.");
            }
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param t the entity to update
     * @return the updated entity
     */
    public T update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "UPDATE " + type.getSimpleName() + " SET ";
        try {
            connection = ConnectionFactory.getConnection();

            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                if (i > 0) {
                    query += ", ";
                }
                query += field.getName() + " = ?";
            }
            query += " WHERE id = ?";

            statement = connection.prepareStatement(query);

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                statement.setObject(i + 1, field.get(t));
            }

            Field idField = t.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            statement.setObject(fields.length + 1, idField.get(t));

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating entity failed, no rows affected.");
            }
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

    /**
     * Deletes an entity from the database.
     *
     * @param entity the entity to delete
     * @return the deleted entity
     */
    public T delete(T entity) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "DELETE FROM " + type.getSimpleName() + " WHERE id = ?";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            Field idField = null;
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.getName().equals("id")) {
                    idField = field;
                    break;
                }
            }

            if (idField == null) {
                throw new IllegalArgumentException("No @Id annotation found on any field.");
            }

            idField.setAccessible(true);
            Object idValue = idField.get(entity);
            if (idValue == null) {
                throw new IllegalArgumentException("ID field value is null.");
            }

            statement.setObject(1, idValue);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting entity failed, no rows affected.");
            }
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return entity;
    }



}
