package com.spark.entities.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private long id;
    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    private String name;
    @Size(max = 100, message ="Description cannot be longer tha 100 characters")
    private String description;
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to zero")
    private double price;
}
