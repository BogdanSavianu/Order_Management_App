package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

/**
 * Represents an order in the system.
 */
@Getter
@Setter
@RequiredArgsConstructor
@ToString

public class Orders {
    private int id;
    private int customerId;
    private int productId;
    private int quantity;
    private Date orderDate;

}
