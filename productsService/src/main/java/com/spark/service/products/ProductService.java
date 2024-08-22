package com.spark.service.products;

import com.spark.entities.domain.ProductDTO;

public interface ProductService {
    void addProduct(ProductDTO product);
    void notifyUpdatedList(); // TODO: documentation?
    void resendList();
}
