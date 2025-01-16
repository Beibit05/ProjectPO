package com.example.buysell.mapper.product;

import com.example.buysell.dto.product.ProductCreateRequestDto;
import com.example.buysell.dto.product.ProductDto;
import com.example.buysell.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductDto toDto(Product product);

    Product toEntity(ProductCreateRequestDto productDto);
}

