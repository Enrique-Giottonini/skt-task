package com.spark.entities.dto;

import com.spark.entities.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMessage {
    private String action;
    private Product product;
}
