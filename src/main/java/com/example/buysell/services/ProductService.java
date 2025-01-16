package com.example.buysell.services;

import com.example.buysell.dto.product.ProductCreateRequestDto;
import com.example.buysell.dto.product.ProductDto;
import com.example.buysell.mapper.product.ProductMapper;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.repositories.ProductRepository;
import com.example.buysell.repositories.UserRepository;
import com.example.buysell.services.image.ImageService;
import com.example.buysell.validator.user.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    private final UserValidator userValidator;
    private final ImageService imageService;

    public List<Product> listProducts(String title) {
        if (title != null) return productRepository.findByTitle(title);
        return productRepository.findAll();
    }


    public ProductDto saveProduct(long userId, ProductCreateRequestDto productDto, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        var user = userValidator.existsById(userId);
        var product = productMapper.toEntity(productDto);
        product.setUser(user);
        product.setImages(imageService.getListImagesFromFiles(Arrays.asList(file1, file2, file3)));
        var createdProduct = productRepository.save(product);
        log.info("Saving new Product. product id: {}, user id {}",
                createdProduct.getId(), createdProduct.getUser().getId());
        return productMapper.toDto(createdProduct);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    public void deleteProduct(User user, Long id) {
        Product product = productRepository.findById(id)
                .orElse(null);
        if (product != null) {
            if (product.getUser().getId().equals(user.getId())) {
                productRepository.delete(product);
                log.info("Product with id = {} was deleted", id);
            } else {
                log.error("User: {} haven't this product with id = {}", user.getEmail(), id);
            }
        } else {
            log.error("Product with id = {} is not found", id);
        }
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }


}
