package com.spark.entities.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListMessage {
    private String action;
    private List<ProductDTO> listOfProducts;
}
