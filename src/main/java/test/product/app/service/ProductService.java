package test.product.app.service;

import test.product.app.dto.ProductDto;
import test.product.app.model.Product;

import java.util.List;

public interface ProductService {

    ProductDto saveProduct(ProductDto productDto);

    List<ProductDto> getAllProduct();

    String getProductById(Long productId);

    Product updateProduct(Product uppdateProduct);

    void deleteProduct(Long productId);
}
