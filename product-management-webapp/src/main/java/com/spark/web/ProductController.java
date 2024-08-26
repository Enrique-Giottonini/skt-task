package com.spark.web;

import com.spark.entities.domain.ProductDTO;
import com.spark.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductServiceImpl productService;

    @GetMapping
    public String list(Model model) {
        List<ProductDTO> listProduct = productService.findAll();
        model.addAttribute("listOfProduct", listProduct);
        return "product-list";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new ProductDTO());
        return "product-add";
    }

    @PostMapping("/new")
    public String addProduct(@Valid @ModelAttribute("product") ProductDTO product,
                                   BindingResult bindingResult,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors() && bindingResult.getFieldError() != null) {
            FieldError error = bindingResult.getFieldError();
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            if ("price".equals(fieldName) && error.getCode().equals("typeMismatch")) {
                errorMessage = "Invalid format for price. Please enter a numeric value.";
            }

            log.error("Product could not be saved because of error: {}", errorMessage);
            model.addAttribute("error", errorMessage);

            return "product-add";
        }

        productService.sendToProcess(product);
        redirectAttributes.addFlashAttribute("savedProduct", product);
        redirectAttributes.addFlashAttribute("addProductSuccess", true);
        return "redirect:/product/new";
    }
}
