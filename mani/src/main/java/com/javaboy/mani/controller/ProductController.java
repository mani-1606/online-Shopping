package com.javaboy.mani.controller;
    /**
     * Search for products where name contains the given text
     * @param searchText Text to search for within product names
     * @return List of products matching the search criteria
     */
   
import com.javaboy.mani.dto.Productdto;
import com.javaboy.mani.model.Product;
import com.javaboy.mani.request.AddProductRequest;
import com.javaboy.mani.request.ProductUpdateRequest;
import com.javaboy.mani.response.ApiResponse;
import com.javaboy.mani.service.product.IProductService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<Productdto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
    }

    @GetMapping("/product/{id}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            Productdto convertedProduct = productService.conertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Product retrieved successfully", convertedProduct));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found with id: " +
                    "" + id, e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            // Log the received product for debugging
            System.out.println("Received product: " + product.getName() + ", " + product.getBrand() + ", " + product.getPrice());

            Product savedProduct = productService.addProduct(product);
            Productdto convertedProduct = productService.conertToDto(savedProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Product added successfully",
                    convertedProduct));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("we can't add  product beacoz " +
                    "it's alrady there: ",
                    e.getMessage()));
        }
    }

    @PutMapping("/product/{id}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest product, @PathVariable Long id) {
        try {
            Product updatedProduct = productService.updateProduct(product, id);
            Productdto convertedProduct = productService.conertToDto(updatedProduct);
            return ResponseEntity.ok(new ApiResponse("Product updated successfully", convertedProduct));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found with id: " +
                    "" + id, e.getMessage()));
        }
    }

    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(new ApiResponse("Product deleted successfully", id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found with id: " +
                    "" + id, e.getMessage()));
        }
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brand, name);
            List<Productdto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found with name" +
                    " and brand : " + name, e.getMessage()));
        }
    }

    @GetMapping("/products/by/category-and-name")
    public ResponseEntity<ApiResponse> getProductByCategoryAndName(@RequestParam String category, @RequestParam String name) {
        try {
            List<Product> products = productService.getProductsByCategoryAndName(category, name);
            List<Productdto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found with" +
                    " category and name : " + category + " and " + name, e.getMessage()));
        }
    }

    @GetMapping("/products/by/category-and-brand-and-name")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrandAndName(@RequestParam String category, @RequestParam
    String brand, @RequestParam String name) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrandAndName(category, brand, name);
            List<Productdto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found with " +
                    "category," +
                    " brand and name : " + category + ", " + brand + " and " + name, e.getMessage()));
        }
    }
    @GetMapping("/products/by/category-and-brand-and-price")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrandAndPrice(@RequestParam String category, @RequestParam
    String brand, @RequestParam Double price) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrandAndPrice(category, brand, price);
            List<Productdto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found with" +
                    " category," +
                    " brand and price : " + category + ", " + brand + " and " + price, e.getMessage()));
        }
    }
    @GetMapping("/products/by/category")
    public ResponseEntity<ApiResponse> getProductByCategory(@RequestParam String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            List<Productdto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found with" +
                    " category : " + category, e.getMessage()));
        }
    }
    @GetMapping("/products/by/brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            List<Productdto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found with" +
                    " brand : " + brand, e.getMessage()));
        }
    }
    @GetMapping("/products/by/name")
    public ResponseEntity<ApiResponse> getProductByName(@RequestParam String name) {
        try {
            List<Product> products = productService.getProductsByName(name);
            List<Productdto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found with" +
                    " name : " + name, e.getMessage()));
        }
    }

    @GetMapping("/products/by/prefix")
    public ResponseEntity<ApiResponse> getProductByNamePrefix(@RequestParam String prefix) {
        try {
            List<Product> products = productService.getProductsByNamePrefix(prefix);
            List<Productdto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products with names starting with '" + prefix + "' retrieved successfully", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Products not found with" +
                    " prefix : " + prefix, e.getMessage()));
        }
    }

    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByCategoryandBrand(category, brand);
            List<Productdto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found with" +
                    " category and brand : " + category + " and " + brand, e.getMessage()));
        }
    }


}