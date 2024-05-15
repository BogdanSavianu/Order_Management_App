package bll.validators;

import model.Product;

/**
 * Validates that the quantity of a product is greater than 0.
 */
public class QuantityValidator implements Validator<Product> {

    /**
     * Validates the quantity of the specified product.
     *
     * @param product The product to validate.
     * @throws IllegalArgumentException If the quantity is less than or equal to 0.
     */
    @Override
    public void validate(Product product) {
        if (product.getQuantity() <= 0) {
            throw new IllegalArgumentException("The quantity of the product must be greater than 0.");
        }
    }
}
