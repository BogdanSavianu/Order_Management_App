package bll.validators;

import model.Product;

/**
 * Validates that the price of a product is positive.
 */
public class PriceValidator implements Validator<Product> {

    /**
     * Validates the price of the specified product.
     *
     * @param product The product to validate.
     * @throws IllegalArgumentException If the price is less than or equal to 0.
     */
    @Override
    public void validate(Product product) {
        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("The price of the product must be positive.");
        }
    }
}
