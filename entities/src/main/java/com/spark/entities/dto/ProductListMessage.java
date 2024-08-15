package com.spark.entities.dto;

import com.spark.entities.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListMessage {
    private String status;
    private List<Product> listOfProducts;
}
