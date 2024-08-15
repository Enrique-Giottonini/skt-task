package com.spark.controllers;

import com.spark.entities.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import com.spark.services.ProductService;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public String list(Model model) {
        List<Product> listProduct = productService.findAll();
        model.addAttribute("listOfProduct", listProduct);
        return "product-list";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "product-add";
    }

    @PostMapping("/new")
    public RedirectView addProduct(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes) {
        final RedirectView redirectView = new RedirectView("/product/new", true);
        Product savedProduct = productService.save(product);
        redirectAttributes.addFlashAttribute("savedProduct", savedProduct);
        redirectAttributes.addFlashAttribute("addProductSuccess", true);
        return redirectView;
    }
}
