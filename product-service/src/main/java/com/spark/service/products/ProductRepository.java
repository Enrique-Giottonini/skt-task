package com.spark.service.products;

import com.spark.service.products.entities.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    @Query(value = "EXEC dbo.getAllProducts", nativeQuery = true)
    List<Product> getAllProducts();

    @Query(value = "EXEC dbo.insertProduct :#{#product.name}, :#{#product.description}, :#{#product.price}", nativeQuery = true)
    Product insertProduct(@Param("product") Product product);
}