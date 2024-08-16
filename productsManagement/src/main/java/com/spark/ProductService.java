package com.spark;

import com.spark.entities.domain.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product save(Product product);
    void updateList(List<Product> productList);
}
