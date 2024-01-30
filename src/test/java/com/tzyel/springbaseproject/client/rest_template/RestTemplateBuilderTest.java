package com.tzyel.springbaseproject.client.rest_template;

import com.tzyel.springbaseproject.client.ClientTestBase;
import com.tzyel.springbaseproject.dto.product.CreateProductDto;
import com.tzyel.springbaseproject.dto.product.ProductDto;
import com.tzyel.springbaseproject.dto.product.UpdateProductDto;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RestTemplateBuilderTest extends ClientTestBase {
    private static final String REST_TEMPLATE_BEAN_NAME = "sbpRestTemplate";

    private static Integer productId;

    @Test
    @Order(1)
    public void testCreateProduct() {
        CreateProductDto createProductDto = new CreateProductDto();
        createProductDto.setName("New name");
        createProductDto.setNote("Hello");

        ResponseEntity<ProductDto> response = RestTemplateBuilder.init(REST_TEMPLATE_BEAN_NAME)
                .withMethod(HttpMethod.POST)
                .withHeader(HttpHeaders.AUTHORIZATION, generateMemberAuthorizationToken())
                .withUrl(getServerHost() + "/api/product")
                .withBody(createProductDto)
                .execute(ProductDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createProductDto.getName(), response.getBody().getName());
        assertEquals(createProductDto.getNote(), response.getBody().getNote());
        productId = response.getBody().getId();
    }

    @Test
    @Order(2)
    public void testGetProducts() {
        ResponseEntity<List<ProductDto>> response = RestTemplateBuilder.init(REST_TEMPLATE_BEAN_NAME)
                .withMethod(HttpMethod.GET)
                .withHeader(HttpHeaders.AUTHORIZATION, generateMemberAuthorizationToken())
                .withUrl(getServerHost() + "/api/product")
                .execute(new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @Order(3)
    public void testGetProduct() {
        ResponseEntity<ProductDto> response = RestTemplateBuilder.init(REST_TEMPLATE_BEAN_NAME)
                .withMethod(HttpMethod.GET)
                .withHeader(HttpHeaders.AUTHORIZATION, generateMemberAuthorizationToken())
                .withUrl(getServerHost() + "/api/product/" + productId)
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

        ResponseEntity<ProductDto> response = RestTemplateBuilder.init(REST_TEMPLATE_BEAN_NAME)
                .withMethod(HttpMethod.PUT)
                .withHeader(HttpHeaders.AUTHORIZATION, generateMemberAuthorizationToken())
                .withUrl(getServerHost() + "/api/product/" + productId)
                .withBody(updateProductDto)
                .execute(ProductDto.class);
        assertNotNull(response.getBody());
        assertEquals(updateProductDto.getName(), response.getBody().getName());
        assertEquals(updateProductDto.getNote(), response.getBody().getNote());
    }

    @Test
    @Order(5)
    public void testDeleteProduct() {
        ResponseEntity<?> response = RestTemplateBuilder.init(REST_TEMPLATE_BEAN_NAME)
                .withMethod(HttpMethod.DELETE)
                .withHeader(HttpHeaders.AUTHORIZATION, generateAdminAuthorizationToken())
                .withUrl(getServerHost() + "/api/product/" + productId)
                .execute();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
