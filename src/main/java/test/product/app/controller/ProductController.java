package test.product.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.product.app.dto.ProductDto;
import test.product.app.kafka.KafkaProductProducer;
import test.product.app.model.Product;
import test.product.app.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final KafkaProductProducer producer;

    public ProductController(ProductService productService, KafkaProductProducer producer) {
        this.productService = productService;
        this.producer = producer;
    }

    @PostMapping("/add")
    public ProductDto addProduct(@RequestBody ProductDto productDto) {
        producer.sendProduct(productDto);
        return productDto;
    }

    @GetMapping("/all")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProduct();
    }

    @GetMapping("/productId")
    public ResponseEntity<String> getProductById(@PathVariable Long productId) {
        String product = productService.getProductById(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product updateProduct) {
        Product product = productService.updateProduct(updateProduct);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct (@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
}
