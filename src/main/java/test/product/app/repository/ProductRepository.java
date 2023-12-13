package test.product.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import test.product.app.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p. *, c.category_name FROM products p INNER JOIN categories c ON p.category_id = c.id", nativeQuery = true)
    List<Object[]> getProductsAndCategories();

    @Query(value = "SELECT * FROM products WHERE price > :minPrice AND category_id = :categoryId", nativeQuery = true)
    List<Product> findProductsByPriceAndCategory(@Param("minPrice") double minPrice, @Param("categoryId") Long categoryId);

    @Query(value = "SELECT * FROM products order by price DESC LIMIT :limit", nativeQuery = true)
    List<Product> findTopProductsByPriceDesc(@Param("limit") int limit);

    @Query(value = "SELECT AVG (price) FROM products WHERE category_id = :categoryId", nativeQuery = true)
    Double getAveragePriceByCategory(@Param("categoryId") Long categoryId);

    @Query(value = "SELECT * FROM products WHERE price > (SELECT AVG (price) FROM products)", nativeQuery = true)
    List<Product> findProductsAboveAveragePrice();
}
