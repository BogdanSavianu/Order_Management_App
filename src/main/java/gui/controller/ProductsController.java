package gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Product;
import singlePointAccess.RepoSinglePointAccess;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The ProductsController class controls the functionality of the Products page in the application.
 * It allows users to add, update, and delete products, as well as view existing products.
 */
public class ProductsController implements Initializable {

    @FXML
    private TableView<Product> tableView;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Float> priceColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    /**
     * Handles the action of adding a new product.
     * Shows dialogs to enter product information and adds the new product to the database.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void handleAddProduct(ActionEvent event) {
        try {
            Class<?>[] dialogTypes = {TextInputDialog.class, TextInputDialog.class, TextInputDialog.class};
            String[] titles = {"Add Product", "Add Product","Add Product"};
            String[] prompts = {"Enter product name:", "Enter product price:", "Enter product quantity:"};
            String[] variableNames = {"name", "price", "quantity"};
            Class<?>[] variableTypes = {String.class, float.class, int.class};
            Object[] variableValues = new Object[3];

            for (int i = 0; i < dialogTypes.length; i++) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle(titles[i]);
                dialog.setHeaderText(null);
                dialog.setContentText(prompts[i]);
                Optional<String> result = dialog.showAndWait();
                if (result.isEmpty()) {
                    return;
                }
                if (variableTypes[i] == int.class) {
                    variableValues[i] = Integer.parseInt(result.get());
                } else if (variableTypes[i] == float.class) {
                    variableValues[i] = Float.parseFloat(result.get());
                }
                else {
                    variableValues[i] = variableTypes[i].cast(result.get());
                }
            }

            Product newProduct = new Product();
            for (int i = 0; i < variableNames.length; i++) {
                Field field = Product.class.getDeclaredField(variableNames[i]);
                field.setAccessible(true);
                field.set(newProduct, variableValues[i]);
            }

            Product addedProduct = RepoSinglePointAccess.getProductBLL().create(newProduct);

            if (addedProduct != null) {
                tableView.getItems().add(addedProduct);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Product Added");
                alert.setHeaderText(null);
                alert.setContentText("Product '" + variableValues[0] + "' added successfully.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to add product.");
                alert.showAndWait();
            }
        } catch (NumberFormatException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while adding the product.");
            alert.showAndWait();
        }
    }


    /**
     * Handles the action of updating an existing product.
     * Shows dialogs to update product information and updates the product in the database.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void handleUpdateProduct(ActionEvent event) {
        try {
            Product selectedProduct = tableView.getSelectionModel().getSelectedItem();

            if (selectedProduct != null) {
                String[] prompts = {"Enter new product name:", "Enter new product price:", "Enter new product quantity:"};
                String[] variableNames = {"name", "price", "quantity"};
                Class<?>[] variableTypes = {String.class, Float.class, Integer.class};
                Object[] variableValues = {selectedProduct.getName(), selectedProduct.getPrice(), selectedProduct.getQuantity()};

                for (int i = 0; i < prompts.length; i++) {
                    TextInputDialog dialog = new TextInputDialog(String.valueOf(variableValues[i]));
                    dialog.setTitle("Update Product");
                    dialog.setHeaderText(null);
                    dialog.setContentText(prompts[i]);
                    Optional<String> result = dialog.showAndWait();
                    if (result.isEmpty()) {
                        return;
                    }
                    variableValues[i] = variableTypes[i].getConstructor(String.class).newInstance(result.get());
                }

                for (int i = 0; i < variableNames.length; i++) {
                    Field field = Product.class.getDeclaredField(variableNames[i]);
                    field.setAccessible(true);
                    field.set(selectedProduct, variableValues[i]);
                }

                Product updatedProduct = RepoSinglePointAccess.getProductBLL().update(selectedProduct);

                if (updatedProduct != null) {
                    tableView.refresh();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Product Updated");
                    alert.setHeaderText(null);
                    alert.setContentText("Product updated successfully.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to update product.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select a product to update.");
                alert.showAndWait();
            }
        } catch (NoSuchFieldException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while updating the product.");
            alert.showAndWait();
        }
    }


    /**
     * Handles the action of deleting an existing product.
     * Prompts the user to confirm deletion and removes the product from the database.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void handleDeleteProduct(ActionEvent event) {
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Product");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete the product '" + selectedProduct.getName() + "'?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Product productToDelete = RepoSinglePointAccess.getProductBLL().delete(selectedProduct);

                if (productToDelete != null) {
                    tableView.getItems().remove(selectedProduct);
                    Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
                    confirmationAlert.setTitle("Product Deleted");
                    confirmationAlert.setHeaderText(null);
                    confirmationAlert.setContentText("Product deleted successfully.");
                    confirmationAlert.showAndWait();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to delete product.");
                    errorAlert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product to delete.");
            alert.showAndWait();
        }
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    /**
     * Initializes the data for the Products table view.
     *
     * @param products The list of products to be displayed in the table.
     */
    public void initData (List<Product> products) {
        tableView.getItems().addAll(products);
    }
}
