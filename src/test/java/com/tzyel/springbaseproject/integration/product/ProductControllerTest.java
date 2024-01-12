package com.tzyel.springbaseproject.integration.product;

import com.tzyel.springbaseproject.dto.product.CreateProductDto;
import com.tzyel.springbaseproject.dto.product.ProductDto;
import com.tzyel.springbaseproject.dto.product.UpdateProductDto;
import com.tzyel.springbaseproject.integration.DatabaseAlias.ProductTable;
import com.tzyel.springbaseproject.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Sql(scripts = {"classpath:integration/sql/product/setup_project.sql"})
public class ProductControllerTest extends IntegrationTestBase {
    private static final String BASE_PATH = "/product";

    @Test
    public void testSearchProductByKeyword() {
        List<ProductDto> productDtoList = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH)
                        .queryParam("keyword", "Product name new")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .headers(withMember())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductDto.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(productDtoList);
        assertEquals(2, productDtoList.size());
    }

    @Test
    public void testGetProduct() {
        Integer productId = 999992;
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH)
                        .pathSegment(productId.toString())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .headers(withMember())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(productId)
                .jsonPath("name").isEqualTo("Product name new 1");
    }

    @Test
    public void testCreateProduct() {
        CreateProductDto createProductDto = new CreateProductDto();
        createProductDto.setName("New name");
        createProductDto.setNote("Hello");
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .headers(withMember())
                .body(BodyInserters.fromValue(createProductDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("name").isEqualTo(createProductDto.getName())
                .jsonPath("note").isEqualTo(createProductDto.getNote());

        verifyDatabase()
                .table(ProductTable.NAME)
                .columns(ProductTable.Column.NAME, ProductTable.Column.NOTE)
                .existWithValues(createProductDto.getName(), createProductDto.getNote());
    }

    @Test
    public void testUpdateProduct() {
        Integer productId = 999992;
        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setName("New name update");
        updateProductDto.setNote("Hello update");
        webTestClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH)
                        .pathSegment(productId.toString())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .headers(withMember())
                .body(BodyInserters.fromValue(updateProductDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(productId)
                .jsonPath("name").isEqualTo(updateProductDto.getName())
                .jsonPath("note").isEqualTo(updateProductDto.getNote());

        verifyDatabase()
                .table(ProductTable.NAME)
                .columns(ProductTable.Column.ID, ProductTable.Column.NAME, ProductTable.Column.NOTE)
                .existWithValues(productId, updateProductDto.getName(), updateProductDto.getNote());
    }

    @Test
    public void testDeleteProduct() {
        int productId = 999992;
        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH)
                        .pathSegment(Integer.toString(productId))
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .headers(withAdmin())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        verifyDatabase()
                .table(ProductTable.NAME)
                .doesNotExistById(productId);
    }
}
