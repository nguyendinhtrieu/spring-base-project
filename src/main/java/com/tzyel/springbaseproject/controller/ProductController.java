package com.tzyel.springbaseproject.controller;

import com.tzyel.springbaseproject.dto.product.CreateProductDto;
import com.tzyel.springbaseproject.dto.product.ProductDto;
import com.tzyel.springbaseproject.dto.product.UpdateProductDto;
import com.tzyel.springbaseproject.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController extends BaseController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> searchProductByKeyword(@RequestParam(value = "keyword", required = false) String keyword) {
        return productService.searchProductByKeyword(keyword);
    }

    @GetMapping("{product_id}")
    public ProductDto getProduct(@PathVariable("product_id") Integer productId) {
        return productService.getProduct(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody CreateProductDto createProductDto) {
        return productService.createProduct(createProductDto);
    }

    @PutMapping("{product_id}")
    public ProductDto updateProduct(@PathVariable("product_id") Integer productId,
                                    @RequestBody UpdateProductDto updateProductDto) {
        return productService.updateProduct(productId, updateProductDto);
    }

    @DeleteMapping("{product_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(Principal principal, @PathVariable("product_id") Integer productId) {
        productService.deleteProduct(productId);
    }
}
