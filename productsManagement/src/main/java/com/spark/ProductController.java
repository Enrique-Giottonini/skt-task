package com.spark;

import com.spark.entities.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final List<Product> productList;

    public ProductController(List<Product> productList) {
        this.productList = productList;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("listOfProduct", productList);
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
        productList.add(product);
        boolean isSaved = productList.contains(product);
        if (isSaved) redirectAttributes.addFlashAttribute("savedProduct", product);
        redirectAttributes.addFlashAttribute("addProductSuccess", isSaved);
        return redirectView;
    }
}
