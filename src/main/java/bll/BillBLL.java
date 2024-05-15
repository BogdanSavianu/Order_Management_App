package bll;

import java.util.ArrayList;
import java.util.List;

import bll.validators.Validator;
import dao.BillDAO;
import model.Bill;

/**
 * The CustomerBLL class represents the business logic layer for managing Customer entities.
 * It provides methods for finding, creating, updating, and deleting Customer objects.
 * This class also encapsulates validation logic for Customer objects using a list of validators.
 */
public class BillBLL {

    private List<Validator<Bill>> validators;
    private BillDAO billDAO;

    /**
     * Constructs a new CustomerBLL object.
     * Initializes the list of validators with default validators for Customer objects.
     * Instantiates a CustomerDAO object for data access operations.
     */
    public BillBLL() {
        validators = new ArrayList<Validator<Bill>>();

        billDAO = new BillDAO();
    }

    public void insert(Bill bill) {
        billDAO.insert(bill);
    }

    public List<Bill> findAll() {
        return billDAO.findAll();
    }
}
