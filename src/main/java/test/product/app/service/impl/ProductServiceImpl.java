package test.product.app.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import test.product.app.dto.ProductDto;
import test.product.app.model.Product;
import test.product.app.repository.ProductRepository;
import test.product.app.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private StringRedisTemplate redisTemplate;

    private static final String CACHE_NAME = "product";

    public ProductServiceImpl(ProductRepository productRepository, StringRedisTemplate redisTemplate) {
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional
    @CachePut(value = CACHE_NAME, key = "#product.id")
    public ProductDto saveProduct(ProductDto productDto) {
        Product newProduct = mapToEntity(productDto);
        Product addProduct = productRepository.save(newProduct);
        redisTemplate.delete(CACHE_NAME);
        return mapToDTO(addProduct);
    }

    @Override
    public List<ProductDto> getAllProduct() {

        return productRepository.findAll().stream()
                .map(product -> {
                    ProductDto productDto = new ProductDto();
                    productDto.setName(product.getName());
                    productDto.setPrice(product.getPrice());
                    return productDto;
                }).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "#productId")
    public String getProductById(Long productId) {

        String cachedProduct = redisTemplate.opsForValue().get(CACHE_NAME + ":" + productId);

        if (productId == null) {
            System.out.println("Product found in cache: " + productId);
            return cachedProduct;
        } else {
            System.out.println("Product not found in cache. Fetching from the database: " + productId);
            Product databaseProduct = productRepository.findById(productId).orElse(null);

            if (databaseProduct != null) {
                redisTemplate.opsForValue().set(CACHE_NAME + ":" + productId, String.valueOf(databaseProduct));
            }
            return String.valueOf(databaseProduct);
        }
    }

    @Override
    @CachePut(value = CACHE_NAME, key = "#product.id")
    public Product updateProduct(Product uppdateProduct) {
        Product product = productRepository.save(uppdateProduct);

        redisTemplate.opsForValue().set(CACHE_NAME + ":" + product.getId(), String.valueOf(product));
        return product;
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#productId")
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
        redisTemplate.delete(CACHE_NAME + ":" + productId);
    }


    private Product mapToEntity(ProductDto productDto) {
        if (productDto != null) {
            Product product = new Product();
            product.setId(productDto.getId());
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            return product;
        }
        return null;
    }

    private ProductDto mapToDTO(Product product) {
        if (product != null) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setPrice(product.getPrice());
            return productDto;
        }
        return null;
    }
}
