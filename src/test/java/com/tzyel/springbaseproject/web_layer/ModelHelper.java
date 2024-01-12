package com.tzyel.springbaseproject.web_layer;

import com.tzyel.springbaseproject.dto.product.ProductDto;
import com.tzyel.springbaseproject.entity.ProductEntity;

public class ModelHelper {
    public static ProductDto getProductDto(int productId) {
        ProductDto productDto = new ProductDto();
        productDto.setId(productId);
        productDto.setName("Product name");
        productDto.setNote("Product note");
        productDto.setCompanyId(1);
        return productDto;
    }

    public static ProductDto getProductDto() {
        return getProductDto(1);
    }

    public static ProductEntity getProductEntity() {
        return getProductEntity(1);
    }

    public static ProductEntity getProductEntity(Integer productId) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(productId);
        productEntity.setName("Product name");
        productEntity.setNote("Product note");
        productEntity.setCompanyId(1);
        return productEntity;
    }
}
