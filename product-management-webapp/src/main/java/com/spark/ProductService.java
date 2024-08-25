package com.spark;

import com.spark.entities.domain.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAll();
    void sendToProcess(ProductDTO product);
    void updateList(List<ProductDTO> productList);
}
