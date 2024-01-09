package com.tzyel.springbaseproject.service.impl;

import com.tzyel.springbaseproject.constant.MessageCode;
import com.tzyel.springbaseproject.dto.product.CreateProductDto;
import com.tzyel.springbaseproject.dto.product.ProductDto;
import com.tzyel.springbaseproject.dto.product.UpdateProductDto;
import com.tzyel.springbaseproject.entity.ProductEntity;
import com.tzyel.springbaseproject.exception.SpringBaseProjectException;
import com.tzyel.springbaseproject.repository.ProductRepository;
import com.tzyel.springbaseproject.service.ProductService;
import com.tzyel.springbaseproject.utils.ObjectMapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDto> searchProductByKeyword(String keyword) {
        List<ProductEntity> products;
        if (StringUtils.hasText(keyword)) {
            products = productRepository.findAllByNameContaining(keyword);
        } else {
            products = productRepository.findAll();
        }
        return ObjectMapperUtil.mapAll(products, ProductDto.class);
    }

    @Override
    public ProductDto getProduct(Integer productId) {
        return ObjectMapperUtil.map(productRepository.findById(productId), ProductDto.class);
    }

    @Override
    @Transactional
    public ProductDto createProduct(CreateProductDto createProductDto) {
        ProductEntity productEntity = new ProductEntity();
        ObjectMapperUtil.map(createProductDto, productEntity);
        productEntity.setCompanyId(1); // Default company
        return ObjectMapperUtil.map(productRepository.save(productEntity), ProductDto.class);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Integer productId, UpdateProductDto updateProductDto) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> SpringBaseProjectException.Builder.notFound(MessageCode.E0010002).build());
        ObjectMapperUtil.map(updateProductDto, productEntity);

        return ObjectMapperUtil.map(productRepository.save(productEntity), ProductDto.class);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> SpringBaseProjectException.Builder.notFound(MessageCode.E0010002).build());
        productRepository.delete(productEntity);
    }
}
