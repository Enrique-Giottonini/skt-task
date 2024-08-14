package com.spark;

import com.spark.entities.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/new")
    public String newProduct(Model model) {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1, "cocoliso"));
        productList.add(new Product(99, "changoleon"));
        model.addAttribute("productList", productList);
        return "product-create";
    }
}
