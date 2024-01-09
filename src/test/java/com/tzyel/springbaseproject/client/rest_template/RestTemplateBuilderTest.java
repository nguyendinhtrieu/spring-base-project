package com.tzyel.springbaseproject.client.rest_template;

import com.tzyel.springbaseproject.dto.product.CreateProductDto;
import com.tzyel.springbaseproject.dto.product.ProductDto;
import com.tzyel.springbaseproject.dto.product.UpdateProductDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * To run UT: need to start local application first
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestTemplateBuilderTest {
    private static final String MEMBER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZSI6IjA5ODc2NTQzMjEwIiwiZW1haWwiOiJuZ3V5ZW5kaW5odHJpZXUzNjVAZ21haWwuY29tIiwic3ViIjoibXktbmFtZSIsImlhdCI6MTcwNDI2NTExNiwiZXhwIjoxNzQ3NDY1MTE2fQ.b-NXPKEM56lSkQF9dvfF6qqo_hyCkuKZoXFFNY1laKo";
    private static final String ADMIN_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZSI6IjA5ODc2NTQzMjEwIiwiZW1haWwiOiJuZ3V5ZW5kaW5odHJpZXUzNjVAZ21haWwuY29tIiwic3ViIjoiYWRtaW4iLCJpYXQiOjE3MDQyNjUxMTYsImV4cCI6MTc0NzQ2NTExNn0.XmydQzngdDLgCE3VN1tG72sHEecDkWKBdkWw2fSdDrY";
    private static Integer productId;

    @Test
    @Order(1)
    public void testCreateProduct() {
        CreateProductDto createProductDto = new CreateProductDto();
        createProductDto.setName("New name");
        createProductDto.setNote("Hello");

        ResponseEntity<ProductDto> response = RestTemplateBuilder.init("sbpRestTemplate")
                .withMethod(HttpMethod.POST)
                .withHeader(HttpHeaders.AUTHORIZATION, MEMBER_TOKEN)
                .withUrl("http://localhost:8080/product")
                .withBody(createProductDto)
                .execute(ProductDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        productId = response.getBody().getId();
    }

    @Test
    @Order(2)
    public void testGetProducts() {
        ResponseEntity<List<ProductDto>> response = RestTemplateBuilder.init("sbpRestTemplate")
                .withMethod(HttpMethod.GET)
                .withHeader(HttpHeaders.AUTHORIZATION, MEMBER_TOKEN)
                .withUrl("http://localhost:8080/product")
                .execute(new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @Order(3)
    public void testGetProduct() {
        ResponseEntity<ProductDto> response = RestTemplateBuilder.init("sbpRestTemplate")
                .withMethod(HttpMethod.GET)
                .withHeader(HttpHeaders.AUTHORIZATION, MEMBER_TOKEN)
                .withUrl("http://localhost:8080/product/" + productId)
                .execute(ProductDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(4)
    public void testUpdateProduct() {
        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setName("New name update");
        updateProductDto.setNote("Hello update");

        ResponseEntity<ProductDto> response = RestTemplateBuilder.init("sbpRestTemplate")
                .withMethod(HttpMethod.PUT)
                .withHeader(HttpHeaders.AUTHORIZATION, MEMBER_TOKEN)
                .withUrl("http://localhost:8080/product/" + productId)
                .withBody(updateProductDto)
                .execute(ProductDto.class);
        assertNotNull(response.getBody());
        assertEquals("New name update", response.getBody().getName());
    }

    @Test
    @Order(5)
    public void testDeleteProduct() {
        ResponseEntity<?> response = RestTemplateBuilder.init("sbpRestTemplate")
                .withMethod(HttpMethod.DELETE)
                .withHeader(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN)
                .withUrl("http://localhost:8080/product/" + productId)
                .execute();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
