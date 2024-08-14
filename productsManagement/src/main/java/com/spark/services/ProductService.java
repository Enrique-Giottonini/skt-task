package com.spark.services;

import com.spark.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final List<Product> productList;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public List<Product> findAll() {
        return productList;
    }

    public Product save(Product product) {
        kafkaTemplate.send("products", "product.creation: " + product);
        productList.add(product);
        return product;
    }
}
