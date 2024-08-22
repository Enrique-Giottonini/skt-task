package com.spark;

import com.spark.entities.domain.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAll();
    ProductDTO save(ProductDTO product);
    void updateList(List<ProductDTO> productList);
}
