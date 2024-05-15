package dao;

import connection.ConnectionFactory;
import model.Bill;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BillDAO extends AbstractDAO<Bill> {

    @Override
    List<Bill> createObjects(ResultSet resultSet) {
        List<Bill> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date date = resultSet.getDate("date");
                float totalPrice = resultSet.getFloat("totalPrice");

                list.add(new Bill(id, date, totalPrice));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:createObjects " + e.getMessage());
        }
        return list;
    }

    @Override
    public Bill insert(Bill bill) {
        String query = "INSERT INTO Bill (id, date, totalPrice) VALUES (?, ?, ?)";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, bill.id());
            statement.setDate(2, bill.date());
            statement.setFloat(3, bill.totalPrice());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting entity failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Bill(generatedKeys.getInt(1), bill.date(), bill.totalPrice());
                } else {
                    throw new SQLException("Inserting entity failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Bill> findAll() {
        String query = "SELECT * FROM Bill";
        List<Bill> entities = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            entities = createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        }
        return entities;
    }
}
