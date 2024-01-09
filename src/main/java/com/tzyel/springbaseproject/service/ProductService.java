package com.tzyel.springbaseproject.service;

import com.tzyel.springbaseproject.dto.product.CreateProductDto;
import com.tzyel.springbaseproject.dto.product.ProductDto;
import com.tzyel.springbaseproject.dto.product.UpdateProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> searchProductByKeyword(String keyword);

    ProductDto getProduct(Integer productId);

    ProductDto createProduct(CreateProductDto createProductDto);

    ProductDto updateProduct(Integer productId, UpdateProductDto updateProductDto);

    void deleteProduct(Integer productId);
}
