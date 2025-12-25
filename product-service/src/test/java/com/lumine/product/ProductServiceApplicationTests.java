package com.lumine.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumine.product.dto.ProductRequest;
import com.lumine.product.model.Product;
import com.lumine.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    /**
     * Test create a new product
     */
    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);

        // Act (Call the endpoint)
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(status().isCreated()); // Assert 201 Created

        Assertions.assertEquals(1, productRepository.findAll().size());
    }

    /**
     * Tests retrieval of all products
     */
    @Test
    void shouldGetProductAllProduct() throws Exception {

        Product product = Product.builder()
                .name("iPhone 15")
                .description("Apple Smartphone")
                .price(BigDecimal.valueOf(1200))
                .build();
        productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldGetProductById() throws Exception {

        Product product = Product.builder()
                .id("pr001")
                .name("iPhone 15")
                .description("Apple Smartphone")
                .price(BigDecimal.valueOf(1200))
                .build();
        productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/pr001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("pr001"))
                .andExpect(jsonPath("$.name").value("iPhone 15"));

    }

    private ProductRequest getProductRequest() {
        return new ProductRequest("iPhone 15", "Apple Smartphone", BigDecimal.valueOf(1200));
    }
}
