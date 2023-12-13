package test.product.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import test.product.app.dto.ProductDto;
import test.product.app.kafka.KafkaProductProducer;
import test.product.app.service.ProductService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private KafkaProductProducer kafkaProductProducer;

    @Test
    public void testAddProduct() throws Exception {

        //Given
        ProductDto productDto = new ProductDto();
        productDto.setName("Iphone 15 PRO MAX");
        productDto.setPrice(100);

        //When
        mockMvc.perform(post("/api/products/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetAllProducts() throws Exception {

        //Given
        ProductDto productDto = new ProductDto();
        productDto.setName("Iphone 15 PRO MAX");
        productDto.setPrice(100);

        mockMvc.perform(get("/api/products/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].price").exists());
    }
}
