package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a product in the system.
 */

@Getter
@Setter
@RequiredArgsConstructor
public class Product {
    private int id;
    private String name;
    private int quantity;
    private float price;

    @Override
    public String toString() {
        return "ID=" + id +
                ", Name='" + name + '\'' +
                ", Quantity=" + quantity +
                ", Price=" + String.format("%.2f", price);
    }
}
