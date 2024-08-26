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
import org.springframework.test.web.servlet.MvcResult;

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
        MvcResult result = mockMvc.perform(post("/product/new")
                        .param("name", "Valid Product Name")
                        .param("price", "10.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/new"))
                .andExpect(flash().attributeExists("savedProduct", "addProductSuccess"))
                .andReturn();

        // Verify that the productService method was called
        verify(productService).sendToProcess(any(ProductDTO.class));
    }
}
