package com.javaboy.mani.service.product;

import com.javaboy.mani.model.Product;
import com.javaboy.mani.request.AddProductRequest;
import com.javaboy.mani.request.ProductUpdateRequest;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest     product);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    Product getProductById(Long productId);
    void deleteProduct(Long productId);

    List<Product> getAllProducts();
    List<Product> getProductsByCategoryandBrand(String category, String brand);
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrandAndName(String brand, String name);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByCategoryAndName(String category, String name);
    List<Product> getProductsByCategoryAndBrandAndName(String category, String brand, String name);
    List<Product> getProductsByCategoryAndBrandAndPrice(String category, String brand, Double price);
    List<Product> getProductsByBrand(String brand);
}
