package com.example.buysell.dto.product;

import com.example.buysell.models.Image;

import java.util.List;

public class ProductDto {
    private Long id;
    private String title;
    private String description;
    private Integer price;
    private String city;
    private List<Long> imageIds; // Только идентификаторы изображений
}


