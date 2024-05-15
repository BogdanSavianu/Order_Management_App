package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents a customer in the system.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Customer {
    private int id;
    private String name;
    private String address;
    private String email;
    private int age;


    /**
     * Returns a string representation of the customer.
     *
     * @return A string representation of the customer.
     */
    @Override
    public String toString() {
        return String.format(
                "ID: %d\nName: %s\nAddress: %s\nEmail: %s\nAge: %d",
                id, name, address, email, age);
    }

}
