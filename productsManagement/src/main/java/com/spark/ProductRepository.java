package com.spark;

import com.spark.entities.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class ProductRepository {

    private final List<Product> productList = Collections.synchronizedList(new ArrayList<>());

    public List<Product> findAll() {
        return productList;
    }

    public int count() {
        return productList.size();
    }

    public void replaceAll(List<Product> newList) {
        productList.clear();
        productList.addAll(newList);
    }

}
