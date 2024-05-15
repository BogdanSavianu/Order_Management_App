package gui.controller;

import dao.BillDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Bill;
import model.Customer;

import java.sql.Date;
import java.util.List;

public class BillController {
    @FXML
    private TableView<Bill> tableView;

    @FXML
    private TableColumn<Bill, Integer> idColumn;

    @FXML
    private TableColumn<Bill, Date> dateColumn;

    @FXML
    private TableColumn<Bill, Float> totalPriceColumn;;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new RecordPropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new RecordPropertyValueFactory<>("date"));
        totalPriceColumn.setCellValueFactory(new RecordPropertyValueFactory<>("totalPrice"));
    }

    public void initData(List<Bill> bills) {
        tableView.getItems().addAll(bills);
    }
}
