package com.spark.web;

import com.spark.entities.domain.ProductDTO;
import com.spark.impl.ProductServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
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

}
