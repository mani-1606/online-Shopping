package com.javaboy.mani.repository;

import com.javaboy.mani.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryAndBrand(String category, String brand);

    List<Product> findByCategory(String category);

    List<Product> findByBrandAndName(String brand, String name);
    @Query("SELECT p FROM Product p WHERE LOWER( p.name) LIKE LOWER(CONCAT('%', :name,  '%'))")
    List<Product> findByName(String name);

    List<Product> findByCategoryAndName(String category, String name);

    List<Product> findByCategoryAndBrandAndName(String category, String brand, String name);

    List<Product> findByCategoryAndBrandAndPrice(String category, String brand, BigDecimal bigDecimal);

    List<Product> findByBrand(String brand);

    Boolean existsByNameAndBrand(String name, String brand);
}
