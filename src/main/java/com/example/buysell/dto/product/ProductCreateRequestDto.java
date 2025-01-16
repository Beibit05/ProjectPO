package com.example.buysell.dto.product;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NonNull
    private Integer price;
    @NotNull
    private String city;

    public ProductCreateRequestDto(String title, double price, String city, String description) {
    }
}
