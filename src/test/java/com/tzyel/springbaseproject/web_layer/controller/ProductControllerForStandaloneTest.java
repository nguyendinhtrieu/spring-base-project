package com.tzyel.springbaseproject.web_layer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tzyel.springbaseproject.controller.ProductController;
import com.tzyel.springbaseproject.dto.product.CreateProductDto;
import com.tzyel.springbaseproject.dto.product.ProductDto;
import com.tzyel.springbaseproject.dto.product.UpdateProductDto;
import com.tzyel.springbaseproject.service.ProductService;
import com.tzyel.springbaseproject.util.VerificationUtil;
import com.tzyel.springbaseproject.web_layer.ControllerTestBase;
import com.tzyel.springbaseproject.web_layer.ModelHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerForStandaloneTest extends ControllerTestBase {
    private static final String BASE_PATH = "/product";

    @Mock
    private ProductService productService;

    @BeforeEach
    public void beforeEach() {
        ProductController controller = new ProductController(productService);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    public void testSearchProductByKeyword() throws Exception {
        when(productService.searchProductByKeyword(ArgumentMatchers.anyString()))
                .thenReturn(Arrays.asList(ModelHelper.getProductDto(), ModelHelper.getProductDto()));

        MvcResult mvcResult = mockMvc.perform(get(BASE_PATH)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("keyword", "Product name new"))
                .andExpect(status().isOk())
                .andReturn();

        List<ProductDto> productDtoList = getResponseObject(mvcResult, new TypeReference<>() {
        });
        assertNotNull(productDtoList);
        assertEquals(2, productDtoList.size());

        productDtoList = getResponseList(mvcResult, ProductDto.class);
        assertNotNull(productDtoList);
        assertEquals(2, productDtoList.size());
    }

    @Test
    public void testGetProduct() throws Exception {
        Integer productId = 999992;
        ProductDto productDtoMock = ModelHelper.getProductDto(productId);
        when(productService.getProduct(productId))
                .thenReturn(productDtoMock);

        MvcResult mvcResult = mockMvc.perform(get(BASE_PATH + "/{productId}", productId)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value(productDtoMock.getName()))
                .andReturn();

        ProductDto response = getResponseObject(mvcResult, ProductDto.class);
        VerificationUtil.verifyFieldsEqual(productDtoMock, response);
    }

    @Test
    public void testCreateProduct() throws Exception {
        ProductDto productDtoMock = ModelHelper.getProductDto();

        CreateProductDto createProductDto = new CreateProductDto();
        createProductDto.setName("New name");
        createProductDto.setNote("Hello");

        when(productService.createProduct(any(CreateProductDto.class)))
                .thenReturn(productDtoMock);

        MvcResult mvcResult = mockMvc.perform(post(BASE_PATH)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(createProductDto)))
                .andExpect(status().isCreated())
                .andReturn();

        ProductDto response = getResponseObject(mvcResult, ProductDto.class);
        VerificationUtil.verifyFieldsEqual(productDtoMock, response);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Integer productId = 999992;
        ProductDto productDtoMock = ModelHelper.getProductDto(productId);

        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setName("New name update");
        updateProductDto.setNote("Hello update");

        when(productService.updateProduct(eq(productId), any(UpdateProductDto.class)))
                .thenReturn(productDtoMock);

        MvcResult mvcResult = mockMvc.perform(put(BASE_PATH + "/{productId}", productId)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(updateProductDto)))
                .andExpect(status().isOk())
                .andReturn();

        ProductDto response = getResponseObject(mvcResult, ProductDto.class);
        VerificationUtil.verifyFieldsEqual(productDtoMock, response);
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Integer productId = 999992;
        ProductDto productDtoMock = ModelHelper.getProductDto(productId);

        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setName("New name update");
        updateProductDto.setNote("Hello update");

        when(productService.updateProduct(eq(productId), any(UpdateProductDto.class)))
                .thenReturn(productDtoMock);

        mockMvc.perform(delete(BASE_PATH + "/{productId}", productId)
                        .principal(adminPrincipal())
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
