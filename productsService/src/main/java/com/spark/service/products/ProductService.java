package com.spark.service.products;

import com.spark.entities.domain.ProductDTO;
import com.spark.service.products.exceptions.ProductValidationException;
import org.hibernate.exception.DataException;

public interface ProductService {
    /**
     * Adds a new product to the repository.
     *
     *
     * @param product the product dto containing the information of the product to add
     * @throws ProductValidationException if the product data is invalid
     * @throws DataException if the product could not be saved to the database
     */
    void addProduct(ProductDTO product);
    void notifyUpdatedList(); // Todo: documentation
    void resendList();
}
