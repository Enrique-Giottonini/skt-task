package com.spark.entities.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class ProductListMessage {
    private String action;
    private List<ProductDTO> listOfProducts;
}
