package com.spark.services;

import com.spark.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final List<Product> productList;

    public List<Product> findAll() {
        return productList;
    }

    public Product save(Product product) {
        productList.add(product);
        return product;
    }
}
