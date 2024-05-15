package gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import singlePointAccess.RepoSinglePointAccess;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The CustomersController class controls the UI components and business logic related to managing customers.
 * It handles actions such as adding, updating, and deleting customers, and initializes the customer table view.
 */
public class CustomersController implements Initializable {

    @FXML
    private TableView<Customer> tableView;

    @FXML
    private TableColumn<Customer, Integer> idColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> emailColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private TableColumn<Customer, Integer> ageColumn;

    /**
     * Handles the event when the "Add Customer" button is clicked.
     * Shows a dialog to add a new customer and adds it to the database.
     * Displays a confirmation or error message accordingly.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void handleAddCustomer(ActionEvent event) {
        try {
            Customer newCustomer = new Customer();

            Field[] fields = Customer.class.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (fieldName.equals("id")) {
                    continue;
                }

                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add Customer");
                dialog.setHeaderText(null);
                dialog.setContentText("Enter " + fieldName + ":");
                Optional<String> inputResult = dialog.showAndWait();

                if (inputResult.isEmpty()) {
                    return;
                }

                Object value;
                if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                    value = Integer.parseInt(inputResult.get());
                } else if (field.getType().equals(float.class) || field.getType().equals(Float.class)) {
                    value = Float.parseFloat(inputResult.get());
                } else {
                    value = inputResult.get();
                }

                field.set(newCustomer, value);
            }

            Customer customerToAdd = RepoSinglePointAccess.getCustomerBLL().createCustomer(newCustomer);

            if (customerToAdd != null) {
                tableView.getItems().add(newCustomer);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Customer Added");
                alert.setHeaderText(null);
                alert.setContentText("Customer added successfully.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to add customer.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while adding the customer.");
            alert.showAndWait();
        }
    }

    /**
     * Handles the event when the "Update Customer" button is clicked.
     * Shows a dialog to update an existing customer and updates it in the database.
     * Displays a confirmation or error message accordingly.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void handleUpdateCustomer(ActionEvent event) {
        try {
            Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();

            if (selectedCustomer != null) {
                String[] prompts = {"Enter new customer name:", "Enter new customer email:", "Enter new customer address:", "Enter new customer age:"};
                String[] variableNames = {"name", "email", "address", "age"};
                Class<?>[] variableTypes = {String.class, String.class, String.class, int.class};
                Object[] variableValues = {selectedCustomer.getName(), selectedCustomer.getEmail(), selectedCustomer.getAddress(), selectedCustomer.getAge()};

                for (int i = 0; i < prompts.length; i++) {
                    TextInputDialog dialog = new TextInputDialog(String.valueOf(variableValues[i]));
                    dialog.setTitle("Update Customer");
                    dialog.setHeaderText(null);
                    dialog.setContentText(prompts[i]);
                    Optional<String> result = dialog.showAndWait();
                    if (!result.isPresent()) {
                        return;
                    }
                    Field field = Customer.class.getDeclaredField(variableNames[i]);
                    field.setAccessible(true);
                    if (variableTypes[i] == int.class) {
                        field.setInt(selectedCustomer, Integer.parseInt(result.get()));
                    } else {
                        field.set(selectedCustomer, result.get());
                    }
                }

                Customer updatedCustomer = RepoSinglePointAccess.getCustomerBLL().update(selectedCustomer);

                if (updatedCustomer != null) {
                    tableView.refresh();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Customer Updated");
                    alert.setHeaderText(null);
                    alert.setContentText("Customer updated successfully.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to update customer.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select a customer to update.");
                alert.showAndWait();
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while updating the customer.");
            alert.showAndWait();
        }
    }


    /**
     * Handles the event when the "Delete Customer" button is clicked.
     * Asks for confirmation before deleting the selected customer from the database.
     * Displays a confirmation or error message accordingly.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void handleDeleteCustomer(ActionEvent event) {
        try {
            Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();

            if (selectedCustomer != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Deletion");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete the selected customer?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Customer customerToDelete = RepoSinglePointAccess.getCustomerBLL().delete(selectedCustomer);

                    if (customerToDelete != null) {
                        tableView.getItems().remove(selectedCustomer);
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Customer Deleted");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Customer deleted successfully.");
                        successAlert.showAndWait();
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Failed to delete customer.");
                        errorAlert.showAndWait();
                    }
                }
            } else {
                Alert selectAlert = new Alert(Alert.AlertType.WARNING);
                selectAlert.setTitle("Warning");
                selectAlert.setHeaderText(null);
                selectAlert.setContentText("Please select a customer to delete.");
                selectAlert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert exceptionAlert = new Alert(Alert.AlertType.ERROR);
            exceptionAlert.setTitle("Error");
            exceptionAlert.setHeaderText(null);
            exceptionAlert.setContentText("An error occurred while deleting the customer.");
            exceptionAlert.showAndWait();
        }
    }

    /**
     * Initializes the customer table view with appropriate column values.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

    }

    /**
     * Initializes the table view with a list of customers.
     *
     * @param customers The list of customers to be displayed in the table view.
     */
    public void initData(List<Customer> customers) {
        tableView.getItems().addAll(customers);
    }
}
