package com.spark.web;

import com.spark.entities.domain.Product;
import com.spark.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductServiceImpl productService;

    // for custom error handling type mismatches from client
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

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
    public RedirectView addProduct(@Valid @ModelAttribute("product") Product product,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Verify your data");
            return new RedirectView("/product/new", true);
        }
        Product savedProduct = productService.save(product);
        redirectAttributes.addFlashAttribute("savedProduct", savedProduct);
        redirectAttributes.addFlashAttribute("addProductSuccess", true);
        return new RedirectView("/product/new", true);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public RedirectView handleTypeMismatchException(TypeMismatchException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("priceError", "Invalid format for price. Please enter a numeric value.");
        return new RedirectView("/product/new", true);
    }
}
