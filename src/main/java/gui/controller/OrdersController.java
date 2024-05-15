package gui.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Bill;
import model.Customer;
import model.Orders;
import model.Product;
import singlePointAccess.RepoSinglePointAccess;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {

    @FXML
    private TableView<Orders> tableView;

    @FXML
    private TableColumn<Orders, Integer> idColumn;

    @FXML
    private TableColumn<Orders, String> customerNameColumn;

    @FXML
    private TableColumn<Orders, String> productNameColumn;

    @FXML
    private TableColumn<Orders, Integer> quantityColumn;

    @FXML
    private TableColumn<Orders, LocalDate> dateColumn;

    @FXML
    private void handleAddOrder(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Order");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to choose an existing customer or create a new one?");
        ButtonType existingCustomerButton = new ButtonType("Existing Customer");
        ButtonType newCustomerButton = new ButtonType("New Customer");
        alert.getButtonTypes().setAll(existingCustomerButton, newCustomerButton);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == existingCustomerButton) {
                Customer selectedCustomer = chooseExistingCustomer();
                if (selectedCustomer != null) {
                    addOrder(selectedCustomer);
                }
            } else if (result.get() == newCustomerButton) {
                Customer newCustomer = createNewCustomer();
                if (newCustomer != null) {
                    addOrder(newCustomer);
                }
            }
        }
    }

    private Customer chooseExistingCustomer() {
        List<Customer> existingCustomers = RepoSinglePointAccess.getCustomerBLL().findAll();
        ObservableList<Customer> customerOptions = FXCollections.observableArrayList(existingCustomers);

        ChoiceBox<Customer> choiceBox = new ChoiceBox<>(customerOptions);
        choiceBox.setTooltip(new Tooltip("Select a customer"));

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Choose Customer");
        dialog.getDialogPane().setContent(choiceBox);
        dialog.showAndWait();

        return choiceBox.getValue();
    }

    private Customer createNewCustomer() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("New Customer");
        nameDialog.setHeaderText(null);
        nameDialog.setContentText("Enter customer name:");
        Optional<String> nameResult = nameDialog.showAndWait();

        if (nameResult.isPresent()) {
            String name = nameResult.get();

            TextInputDialog emailDialog = new TextInputDialog();
            emailDialog.setTitle("New Customer");
            emailDialog.setHeaderText(null);
            emailDialog.setContentText("Enter customer email:");
            Optional<String> emailResult = emailDialog.showAndWait();

            if (emailResult.isPresent()) {
                String email = emailResult.get();

                TextInputDialog addressDialog = new TextInputDialog();
                addressDialog.setTitle("New Customer");
                addressDialog.setHeaderText(null);
                addressDialog.setContentText("Enter customer address:");
                Optional<String> addressResult = addressDialog.showAndWait();

                if (addressResult.isPresent()) {
                    String address = addressResult.get();

                    TextInputDialog ageDialog = new TextInputDialog();
                    ageDialog.setTitle("New Customer");
                    ageDialog.setHeaderText(null);
                    ageDialog.setContentText("Enter customer age:");
                    Optional<String> ageResult = ageDialog.showAndWait();

                    if (ageResult.isPresent()) {
                        int age = Integer.parseInt(ageResult.get());

                        Customer newCustomer = new Customer();
                        newCustomer.setName(name);
                        newCustomer.setEmail(email);
                        newCustomer.setAddress(address);
                        newCustomer.setAge(age);

                        Customer addedCustomer = RepoSinglePointAccess.getCustomerBLL().createCustomer(newCustomer);

                        if (addedCustomer != null) {
                            Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
                            confirmationAlert.setTitle("New Customer Added");
                            confirmationAlert.setHeaderText(null);
                            confirmationAlert.setContentText("Customer '" + addedCustomer.getName() + "' added successfully.");
                            confirmationAlert.showAndWait();

                            return addedCustomer;
                        } else {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Error");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Failed to add customer.");
                            errorAlert.showAndWait();
                        }
                    }
                }
            }
        }
        return null;
    }

    private void addOrder(Customer customer) {
        List<Product> products = RepoSinglePointAccess.getProductBLL().findAll();
        ObservableList<Product> productOptions = FXCollections.observableArrayList(products);

        ChoiceBox<Product> productChoiceBox = new ChoiceBox<>(productOptions);
        productChoiceBox.setTooltip(new Tooltip("Select a product"));

        Alert productDialog = new Alert(Alert.AlertType.CONFIRMATION);
        productDialog.setTitle("Choose Product");
        productDialog.getDialogPane().setContent(productChoiceBox);
        productDialog.showAndWait();

        Product selectedProduct = productChoiceBox.getValue();
        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product.");
            alert.showAndWait();
            return;
        }

        TextInputDialog quantityDialog = new TextInputDialog();
        quantityDialog.setTitle("New Order");
        quantityDialog.setHeaderText(null);
        quantityDialog.setContentText("Enter quantity:");
        Optional<String> quantityResult = quantityDialog.showAndWait();

        if (quantityResult.isPresent()) {
            int quantity = Integer.parseInt(quantityResult.get());

            Orders newOrder = new Orders();
            newOrder.setCustomerId(customer.getId());
            newOrder.setProductId(selectedProduct.getId());
            newOrder.setQuantity(quantity);
            LocalDate currentDate = LocalDate.now();
            newOrder.setOrderDate(Date.valueOf(currentDate));

            Orders addedOrder = RepoSinglePointAccess.getOrderBLL().createOrder(newOrder);

            if (addedOrder != null) {
                Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
                confirmationAlert.setTitle("New Order Added");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("New order added successfully.");
                confirmationAlert.showAndWait();

                Bill bill = new Bill(addedOrder.getId(), addedOrder.getOrderDate(), addedOrder.getQuantity() * RepoSinglePointAccess.getProductBLL().findProductById(addedOrder.getProductId()).getPrice());
                RepoSinglePointAccess.getBillBLL().insert(bill);

                tableView.getItems().add(addedOrder);
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Failed to add order.");
                errorAlert.showAndWait();
            }
        }
    }

    @FXML
    private void handleUpdateOrder(ActionEvent event) {
        try {
            Orders selectedOrder = tableView.getSelectionModel().getSelectedItem();

            if (selectedOrder != null) {
                List<Product> products = RepoSinglePointAccess.getProductBLL().findAll();
                ObservableList<Product> productOptions = FXCollections.observableArrayList(products);

                ChoiceBox<Product> productChoiceBox = new ChoiceBox<>(productOptions);
                productChoiceBox.setValue(RepoSinglePointAccess.getProductBLL().findProductById(selectedOrder.getProductId()));
                productChoiceBox.setTooltip(new Tooltip("Select a product"));

                Alert productDialog = new Alert(Alert.AlertType.CONFIRMATION);
                productDialog.setTitle("Choose Product");
                productDialog.getDialogPane().setContent(productChoiceBox);
                productDialog.showAndWait();

                Product selectedProduct = productChoiceBox.getValue();
                if (selectedProduct == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Please select a product.");
                    alert.showAndWait();
                    return;
                }

                TextInputDialog quantityDialog = new TextInputDialog(String.valueOf(selectedOrder.getQuantity()));
                quantityDialog.setTitle("Update Order Quantity");
                quantityDialog.setHeaderText(null);
                quantityDialog.setContentText("Enter new quantity:");
                Optional<String> quantityResult = quantityDialog.showAndWait();

                if (quantityResult.isPresent()) {
                    selectedOrder.setProductId(selectedProduct.getId());
                    selectedOrder.setQuantity(Integer.parseInt(quantityResult.get()));

                    Orders updatedOrder = RepoSinglePointAccess.getOrderBLL().update(selectedOrder);

                    if (updatedOrder != null) {
                        tableView.refresh();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Order Updated");
                        alert.setHeaderText(null);
                        alert.setContentText("Order updated successfully.");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to update order.");
                        alert.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select an order to update.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while updating the order.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteOrder(ActionEvent event) {
        try {
            Orders selectedOrder = tableView.getSelectionModel().getSelectedItem();

            if (selectedOrder != null) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirm Deletion");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("Are you sure you want to delete this order?");
                Optional<ButtonType> result = confirmationAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Orders orderToDelete = RepoSinglePointAccess.getOrderBLL().delete(selectedOrder);

                    if (orderToDelete != null) {
                        tableView.getItems().remove(selectedOrder);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Order Deleted");
                        alert.setHeaderText(null);
                        alert.setContentText("Order deleted successfully.");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to delete order.");
                        alert.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select an order to delete.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while deleting the order.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(cellData -> {
            int customerId = cellData.getValue().getCustomerId();
            String customerName = RepoSinglePointAccess.getCustomerBLL().findCustomerById(customerId).getName();
            return new SimpleObjectProperty<>(customerName);
        });

        productNameColumn.setCellValueFactory(cellData -> {
            int productId = cellData.getValue().getProductId();
            String productName = RepoSinglePointAccess.getProductBLL().findProductById(productId).getName();
            return new SimpleObjectProperty<>(productName);
        });

        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
    }

    public void initData(List<Orders> orders) {
        tableView.getItems().addAll(orders);
    }
}
