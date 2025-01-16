package com.example.buysell.controllers;

import com.example.buysell.dto.product.ProductCreateRequestDto;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("product")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public String products(@RequestParam(name = "searchWord", required = false) String title, Principal principal, Model model) {
        model.addAttribute("products", productService.listProducts(title));
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", title);
        return "products";
    }

    @GetMapping("/my/products")
    public String myProducts(Principal principal, Model model) {
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        return "my-products";
    }


    @GetMapping("/info/{id}")
    public String productInfo(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("authorProduct", product.getUser());
        return "product-info";
    }
    @PostMapping("/create")
    public String createProduct(
            @RequestParam("file1") MultipartFile file1,
            @RequestParam("file2") MultipartFile file2,
            @RequestParam("file3") MultipartFile file3,
            @ModelAttribute @Validated ProductCreateRequestDto product,
            Principal principal,  // Используем Principal для получения текущего пользователя
            Model model) throws IOException {

        String username = principal.getName();
        User user = productService.getUserByPrincipal(principal);
        long userId = user.getId();

        if (file1.isEmpty() || file2.isEmpty() || file3.isEmpty()) {
            model.addAttribute("error", "Все файлы должны быть загружены.");
            return "my-products"; // Переход к форме с ошибкой
        }

        // Создаем продукт
        var createdProduct = productService.saveProduct(userId, product, file1, file2, file3);

        model.addAttribute("product", createdProduct);

        return "redirect:/my/products"; // Переход к списку товаров
    }





    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id, Principal principal) {
        productService.deleteProduct(productService.getUserByPrincipal(principal), id);
        return "redirect:/my/products";
    }

    @GetMapping("/user/{userId}")
    public String userProducts(Principal principal, Model model) {
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        return "my-products";
    }
}
