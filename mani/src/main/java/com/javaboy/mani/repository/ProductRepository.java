package com.javaboy.mani.repository;

import com.javaboy.mani.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.category.name = :category AND p.brand = :brand")
    List<Product> findByCategoryAndBrand(String category, String brand);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category")
    List<Product> findByCategory(String category);

    List<Product> findByBrandAndName(String brand, String name);
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByName(String name);
        List<Product> findByNameStartingWith(String prefix);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByCategoryAndName(String category, String name);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category AND p.brand = :brand AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByCategoryAndBrandAndName(String category, String brand, String name);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category AND p.brand = :brand AND p.price = :price")
    List<Product> findByCategoryAndBrandAndPrice(String category, String brand, BigDecimal price);

    List<Product> findByBrand(String brand);

    Boolean existsByNameAndBrand(String name, String brand);
}
