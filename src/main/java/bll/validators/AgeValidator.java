package bll.validators;

import model.Customer;

/**
 * Validates that the age of a client is positive.
 */
public class AgeValidator implements Validator<Customer> {

    /**
     * Validates the age of the specified client.
     *
     * @param client The client to validate.
     * @throws IllegalArgumentException If the age is less than or equal to 0.
     */
    @Override
    public void validate(Customer c) {
        if (c.getAge() <= 0) {
            throw new IllegalArgumentException("The age of the client must be positive.");
        }
    }
}
