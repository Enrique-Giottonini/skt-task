package com.spark;

import com.spark.entities.domain.ProductDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class ProductRepository {

    private final List<ProductDTO> productList = Collections.synchronizedList(new ArrayList<>());

    public List<ProductDTO> findAll() {
        return productList;
    }

    public int count() {
        return productList.size();
    }

    public void replaceAll(List<ProductDTO> newList) {
        productList.clear();
        productList.addAll(newList);
    }

}
