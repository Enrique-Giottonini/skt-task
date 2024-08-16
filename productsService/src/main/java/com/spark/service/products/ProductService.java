package com.spark.service.products;

import com.spark.entities.domain.Product;

public interface ProductService {
    void addProduct(Product product);
    void notifyUpdatedList(); // TODO: Documentation?
    void resendList();
}
