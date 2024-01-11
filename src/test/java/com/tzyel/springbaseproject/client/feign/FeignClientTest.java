package com.tzyel.springbaseproject.client.feign;

import com.tzyel.springbaseproject.client.ClientTestBase;
import com.tzyel.springbaseproject.dto.product.CreateProductDto;
import com.tzyel.springbaseproject.dto.product.ProductDto;
import com.tzyel.springbaseproject.dto.product.UpdateProductDto;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"server.port=8080"}
)
public class FeignClientTest extends ClientTestBase {
    private static Integer productId;

    @Autowired
    private SbpFeignClient sbpFeignClient;

    @Test
    @Order(1)
    public void testCreateProduct() {
        CreateProductDto createProductDto = new CreateProductDto();
        createProductDto.setName("New name");
        createProductDto.setNote("Hello");
        ResponseEntity<ProductDto> response = sbpFeignClient.createProduct(
                generateMemberAuthorizationToken(),
                createProductDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createProductDto.getName(), response.getBody().getName());
        assertEquals(createProductDto.getNote(), response.getBody().getNote());
        productId = response.getBody().getId();
    }

    @Test
    @Order(2)
    public void testGetProducts() {
        ResponseEntity<List<ProductDto>> response = sbpFeignClient.getProducts(
                generateMemberAuthorizationToken(),
                ""
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @Order(3)
    public void testGetProduct() {
        ResponseEntity<ProductDto> response = sbpFeignClient.getProduct(
                generateMemberAuthorizationToken(),
                productId
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(4)
    public void testUpdateProduct() {
        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setName("New name update");
        updateProductDto.setNote("Hello update");

        ResponseEntity<ProductDto> response = sbpFeignClient.updateProduct(
                generateMemberAuthorizationToken(),
                productId,
                updateProductDto
        );
        assertNotNull(response.getBody());
        assertEquals(updateProductDto.getName(), response.getBody().getName());
        assertEquals(updateProductDto.getNote(), response.getBody().getNote());
    }

    @Test
    @Order(5)
    public void testDeleteProduct() {
        ResponseEntity<?> response = sbpFeignClient.deleteProduct(
                generateAdminAuthorizationToken(),
                productId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
