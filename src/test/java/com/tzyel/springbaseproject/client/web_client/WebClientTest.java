package com.tzyel.springbaseproject.client.web_client;

import com.tzyel.springbaseproject.client.ClientTestBase;
import com.tzyel.springbaseproject.dto.product.CreateProductDto;
import com.tzyel.springbaseproject.dto.product.ProductDto;
import com.tzyel.springbaseproject.dto.product.UpdateProductDto;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"server.port=8080"}
)
public class WebClientTest extends ClientTestBase {
    private static Integer productId;

    @Qualifier("sbpWebClient")
    @Autowired
    private WebClient webClient;

    @Test
    @Order(1)
    public void testCreateProduct() {
        CreateProductDto createProductDto = new CreateProductDto();
        createProductDto.setName("New name");
        createProductDto.setNote("Hello");

        Mono<ProductDto> responseMono = webClient.post()
                .uri("/product")
                .headers(httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, generateMemberAuthorizationToken()))
                .body(BodyInserters.fromValue(createProductDto))
                .exchangeToMono(response -> {
                    assertEquals(HttpStatus.CREATED, response.statusCode());
                    return response.bodyToMono(ProductDto.class);
                });
        ProductDto response = responseMono.block();
        assertNotNull(response);
        assertEquals(createProductDto.getName(), response.getName());
        assertEquals(createProductDto.getNote(), response.getNote());
        productId = response.getId();
    }

    @Test
    @Order(2)
    public void testGetProducts() {
        Mono<List<ProductDto>> responseMono = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/product")
                        .queryParam("keyword", "")
                        .build())
                .headers(httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, generateMemberAuthorizationToken()))
                .exchangeToMono(response -> {
                    assertEquals(HttpStatus.OK, response.statusCode());
                    return response.bodyToMono(new ParameterizedTypeReference<>() {
                    });
                });
        List<ProductDto> response = responseMono.block();
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @Order(3)
    public void testGetProduct() {
        Mono<ProductDto> responseMono = webClient.get()
                .uri("/product/" + productId)
                .headers(httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, generateMemberAuthorizationToken()))
                .exchangeToMono(response -> {
                    assertEquals(HttpStatus.OK, response.statusCode());
                    return response.bodyToMono(ProductDto.class);
                });
        assertNotNull(responseMono.block());
    }

    @Test
    @Order(4)
    public void testUpdateProduct() {
        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setName("New name update");
        updateProductDto.setNote("Hello update");

        Mono<ProductDto> responseMono = webClient.put()
                .uri("/product/" + productId)
                .headers(httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, generateMemberAuthorizationToken()))
                .body(BodyInserters.fromValue(updateProductDto))
                .exchangeToMono(response -> {
                    assertEquals(HttpStatus.OK, response.statusCode());
                    return response.bodyToMono(ProductDto.class);
                });

        ProductDto response = responseMono.block();
        assertNotNull(response);
        assertEquals(updateProductDto.getName(), response.getName());
        assertEquals(updateProductDto.getNote(), response.getNote());
    }

    @Test
    @Order(5)
    public void testDeleteProduct() {
        Mono<HttpStatusCode> responseMono = webClient.delete()
                .uri("/product/" + productId)
                .headers(httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, generateAdminAuthorizationToken()))
                .exchangeToMono(response -> Mono.just(response.statusCode()));

        HttpStatusCode response = responseMono.block();
        assertEquals(HttpStatus.NO_CONTENT, response);
    }
}
