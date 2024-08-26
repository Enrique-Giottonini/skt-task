package com.spark.web;

import com.spark.entities.domain.ProductDTO;
import com.spark.impl.ProductServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.apache.kafka.common.KafkaException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl productService;

    @Test
    public void testList() throws Exception {
        ProductDTO product1 = new ProductDTO(1L, "Product 1", "Description 1", BigDecimal.valueOf(10.00));
        ProductDTO product2 = new ProductDTO(2L, "Product 2", "Description 2", BigDecimal.valueOf(20.00));
        List<ProductDTO> mockProductList = Arrays.asList(product1, product2);

        when(productService.findAll()).thenReturn(mockProductList);

        mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-list"))
                .andExpect(model().attributeExists("listOfProduct"))
                .andExpect(model().attribute("listOfProduct", mockProductList));
    }

    @Test
    public void testNewProduct() throws Exception {
        mockMvc.perform(get("/product/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-add"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", new ProductDTO()));
    }

    @Test
    public void testAddProduct_Success() throws Exception {
        mockMvc.perform(post("/product/new")
                        .param("name", "Valid Product Name")
                        .param("price", "10.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/new"))
                .andExpect(flash().attributeExists("savedProduct", "addProductSuccess"))
                .andReturn();

        // Verify that the productService method was called
        verify(productService).sendToProcess(any(ProductDTO.class));
    }

    @Test
    public void testAddProduct_NameTooLong() throws Exception {
        int maxLength = 50;
        String longName = String.join("", Collections.nCopies(maxLength + 1, "*"));
        mockMvc.perform(post("/product/new")
                        .param("name", longName)
                        .param("description", "Valid Description")
                        .param("price", "10"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testAddProduct_NameIsBlank() throws Exception {
        mockMvc.perform(post("/product/new")
                        .param("name", "      ")
                        .param("description", "Valid Description")
                        .param("price", "10"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testAddProduct_DescriptionTooLong() throws Exception {
        int maxLength = 100;
        String longDescription = String.join("", Collections.nCopies(maxLength + 1, "*"));
        mockMvc.perform(post("/product/new")
                        .param("name", "Valid Name")
                        .param("description", longDescription)
                        .param("price", "10"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testAddProduct_PriceIsNull() throws Exception {
        mockMvc.perform(post("/product/new")
                        .param("name", "Valid Name")
                        .param("description", "Valid Description"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testAddProduct_PriceNegative() throws Exception {
        mockMvc.perform(post("/product/new")
                        .param("name", "Valid Name")
                        .param("description", "Valid Description")
                        .param("price", "-5.0"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testAddProduct_NonNumericPrice() throws Exception {
        mockMvc.perform(post("/product/new")
                        .param("name", "Valid Product Name")
                        .param("description", "Valid Description")
                        .param("price", "abc")) // invalid price (non-numeric)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testAddProduct_PriceWithTooManyIntegerDigits() throws Exception {
        mockMvc.perform(post("/product/new")
                        .param("name", "Valid Product Name")
                        .param("description", "Valid Description")
                        .param("price", "12345678901234567.89"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testAddProduct_PriceWithTooManyFractionalDigits() throws Exception {
        mockMvc.perform(post("/product/new")
                        .param("name", "Valid Product Name")
                        .param("description", "Valid Description")
                        .param("price", "10.123")) // invalid price (too many fractional digits)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testAddProduct_KafkaUnavailable() throws Exception {
        Mockito.doThrow(new KafkaException("Kafka is currently unavailable. Please try again later."))
                .when(productService).sendToProcess(Mockito.any(ProductDTO.class));

        mockMvc.perform(post("/product/new")
                        .param("name", "Valid Product Name")
                        .param("description", "Valid Description")
                        .param("price", "10.0"))  // Valid price format
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

}
