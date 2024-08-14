package com.spark;

import com.spark.entities.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TempRepository {

    @Bean
    public List<Product> productList() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1, "Milk", "LALA", 2.3));
        productList.add((new Product(99, "Orange juice", "Great Value", 1.8)));
        return productList;
    }
}
