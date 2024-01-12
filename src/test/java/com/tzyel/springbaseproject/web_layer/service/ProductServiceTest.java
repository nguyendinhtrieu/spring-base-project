package com.tzyel.springbaseproject.web_layer.service;

import com.tzyel.springbaseproject.dto.product.CreateProductDto;
import com.tzyel.springbaseproject.dto.product.ProductDto;
import com.tzyel.springbaseproject.dto.product.UpdateProductDto;
import com.tzyel.springbaseproject.entity.ProductEntity;
import com.tzyel.springbaseproject.repository.ProductRepository;
import com.tzyel.springbaseproject.service.impl.ProductServiceImpl;
import com.tzyel.springbaseproject.util.VerificationUtil;
import com.tzyel.springbaseproject.web_layer.ModelHelper;
import com.tzyel.springbaseproject.web_layer.WebLayerTestBase;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceTest extends WebLayerTestBase {
    @InjectMocks
    private ProductServiceImpl service;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void testSearchProductByKeyword() {
        String keyword = "keywordMock";

        when(productRepository.findAllByNameContaining(keyword))
                .thenReturn(Arrays.asList(ModelHelper.getProductEntity(), ModelHelper.getProductEntity()));

        List<ProductDto> productDtoList = service.searchProductByKeyword(keyword);

        assertNotNull(productDtoList);
        assertEquals(2, productDtoList.size());
    }

    @Test
    public void testGetProduct() {
        Integer productId = 999992;

        ProductEntity productEntityMock = ModelHelper.getProductEntity(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntityMock));

        ProductDto productDtoActual = service.getProduct(productId);

        VerificationUtil.verifyFieldsEqual(productEntityMock, productDtoActual);
    }

    @Test
    public void testCreateProduct() {
        ProductEntity productEntityMock = ModelHelper.getProductEntity();

        CreateProductDto createProductDto = new CreateProductDto();
        createProductDto.setName("New name");
        createProductDto.setNote("Hello");

        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntityMock);

        ProductDto productDto = service.createProduct(createProductDto);

        VerificationUtil.verifyFieldsEqual(productEntityMock, productDto);
    }

    @Test
    public void testUpdateProduct() {
        Integer productId = 999992;
        ProductEntity productEntityMock = ModelHelper.getProductEntity(productId);

        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setName("New name update");
        updateProductDto.setNote("Hello update");

        when(productRepository.findById(eq(productId))).thenReturn(Optional.of(productEntityMock));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntityMock);

        ProductDto productDto = service.updateProduct(productId, updateProductDto);

        VerificationUtil.verifyFieldsEqual(productEntityMock, productDto);
    }

    @Test
    public void testDeleteProduct() {
        Integer productId = 999992;
        ProductEntity productEntityMock = ModelHelper.getProductEntity(productId);

        when(productRepository.findById(eq(productId))).thenReturn(Optional.of(productEntityMock));

        service.deleteProduct(productId);

        verify(productRepository, times(1)).delete(any(ProductEntity.class));
    }
}
