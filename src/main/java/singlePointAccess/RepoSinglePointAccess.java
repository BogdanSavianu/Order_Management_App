package singlePointAccess;

import bll.BillBLL;
import bll.CustomerBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import lombok.Getter;
import model.Customer;

/**
 * The RepoSinglePointAccess class provides a single point of access to business logic layer (BLL) objects
 * related to Customer, Product, and Order entities.
 * It contains static instances of CustomerBLL, ProductBLL, and OrderBLL classes for convenient access.
 */
public class RepoSinglePointAccess {
    /**
     * Static instance of CustomerBLL for accessing Customer-related business logic operations.
     * -- GETTER --
     *  Retrieves the static instance of CustomerBLL.
     *
     * @return The static instance of CustomerBLL.

     */
    @Getter
    public static CustomerBLL customerBLL = new CustomerBLL();

    /**
     * Static instance of ProductBLL for accessing Product-related business logic operations.
     * -- GETTER --
     *  Retrieves the static instance of ProductBLL.
     *
     * @return The static instance of ProductBLL.

     */
    @Getter
    public static ProductBLL productBLL = new ProductBLL();

    /**
     * Static instance of OrderBLL for accessing Order-related business logic operations.
     * -- GETTER --
     *  Retrieves the static instance of OrderBLL.
     *
     * @return The static instance of OrderBLL.

     */
    @Getter
    public static OrderBLL orderBLL = new OrderBLL();

    /**
     * Static instance of BillBLL for accessing Bill-related business logic operations.
     * -- GETTER --
     *  Retrieves the static instance of BillBLL.
     *
     * @return The static instance of BillBLL.

     */
    @Getter
    public static BillBLL billBLL = new BillBLL();

}
