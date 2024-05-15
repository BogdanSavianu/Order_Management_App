package gui.controller;

import bll.BillBLL;
import bll.CustomerBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import dao.CustomerDAO;
import dao.ProductDAO;
import dao.OrderDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import singlePointAccess.RepoSinglePointAccess;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The HomePageController class controls the main functionality of the home page of the application.
 * It provides methods to display information about customers, products, and orders.
 */
public class HomePageController implements Initializable {

    private CustomerBLL customerBLL;
    private ProductBLL productBLL;
    private OrderBLL orderBLL;
    private BillBLL billBLL;

    /**
     * Initializes instances of BLL classes for customers, products, and orders.
     */
    public HomePageController() {
        customerBLL = RepoSinglePointAccess.getCustomerBLL();
        productBLL = RepoSinglePointAccess.getProductBLL();
        orderBLL = RepoSinglePointAccess.getOrderBLL();
        billBLL = RepoSinglePointAccess.getBillBLL();
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Displays information about customers and navigates to the Customers page.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void showCustomers(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Customers.fxml"));
            Parent root = loader.load();
            CustomersController controller = loader.getController();
            controller.initData(customerBLL.findAll());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays information about products and navigates to the Products page.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void showProducts(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Products.fxml"));
            Parent root = loader.load();
            ProductsController controller = loader.getController();
            controller.initData(productBLL.findAll());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays information about orders and navigates to the Orders page.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void showOrders(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Orders.fxml"));
            Parent root = loader.load();
            OrdersController controller = loader.getController();
            controller.initData(orderBLL.findAll());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays information about bills and navigates to the Bills page.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void showBills(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bills.fxml"));
            Parent root = loader.load();
            BillController controller = loader.getController();
            controller.initData(billBLL.findAll());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
