package com.spark.service.products;

import com.spark.entities.domain.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TempRepository {

    @Bean
    public List<Product> productList() {
        List<Product> productList = new ArrayList<>();
        return productList;
    }
}
