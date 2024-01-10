package com.tzyel.springbaseproject.client.feign;

import com.tzyel.springbaseproject.dto.product.CreateProductDto;
import com.tzyel.springbaseproject.dto.product.ProductDto;
import com.tzyel.springbaseproject.dto.product.UpdateProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "SbpFeignClient", url = "http://localhost:8080")
public interface SbpFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/product")
    ResponseEntity<List<ProductDto>> getProducts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam("keyword") String keyword
    );

    @RequestMapping(method = RequestMethod.GET, value = "/product/{productId}")
    ResponseEntity<ProductDto> getProduct(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable("productId") Integer productId
    );

    @RequestMapping(method = RequestMethod.POST, value = "/product", consumes = "application/json")
    ResponseEntity<ProductDto> createProduct(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody CreateProductDto createProductDto);

    @RequestMapping(method = RequestMethod.PUT, value = "/product/{productId}", consumes = "application/json")
    ResponseEntity<ProductDto> updateProduct(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable("productId") Integer productId,
            @RequestBody UpdateProductDto updateProductDto);

    @RequestMapping(method = RequestMethod.DELETE, value = "/product/{productId}")
    ResponseEntity<?> deleteProduct(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable("productId") Integer productId);
}
