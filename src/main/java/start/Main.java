package start;

import bll.CustomerBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import model.Customer;
import model.Orders;
import model.Product;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Main {
    protected static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws SQLException {

        CustomerBLL customerBll = new CustomerBLL();
        ProductBLL productBll = new ProductBLL();
        OrderBLL orderBll = new OrderBLL();
        Customer customer = new Customer();
        Product product = new Product();
        Orders order = new Orders();

        List<Orders> orders = new ArrayList<>();
        try {
            customer = customerBll.findCustomerById(3);
            product = productBll.findProductById(1);
            order = orderBll.findOrderById(6);
            customer.setName("Adriana");
            //customerBll.update(customer);
            //Orders order1 = new Orders(7, 3, 3, new Date(300000000));
            //orderBll.createOrder(order1);
            orders = orderBll.findAll();
            //customerBll.createCustomer(customer1);
            //productBll.createProduct(product1);
            //order.setQuantity(3);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, ex.getMessage());
        }

        Reflection.retrieveProperties(customer);
        System.out.println();
        Reflection.retrieveProperties(product);
        System.out.println();
        Reflection.retrieveProperties(order);
        System.out.println();
        //Reflection.retrieveProperties(customer1);
        System.out.println();
        //Reflection.retrieveProperties(product1);
        System.out.println();
        for(Orders or : orders) {
            Reflection.retrieveProperties(or);
        }
    }

}
