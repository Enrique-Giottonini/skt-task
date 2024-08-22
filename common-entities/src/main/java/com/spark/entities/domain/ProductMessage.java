package com.spark.entities.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMessage {
    private String action;
    private ProductDTO product;
}
