package test.product.app.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import test.product.app.dto.ProductDto;
import test.product.app.service.ProductService;

@Service
public class KafkaProductConsume {

    @Autowired
    private final ProductService productService;

    public KafkaProductConsume(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "${spring.kafka.product-topic}")
    public void consume(ProductDto productDto) {
        productService.saveProduct(productDto);
    }
}
