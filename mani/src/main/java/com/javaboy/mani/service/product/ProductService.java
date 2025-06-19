package com.javaboy.mani.service.product;

import com.javaboy.mani.model.*;
import com.javaboy.mani.repository.CategoryRepository;
import com.javaboy.mani.repository.ProductRepository;
import com.javaboy.mani.repository.cartItemRepository;
import com.javaboy.mani.repository.orderItemRepository;
import com.javaboy.mani.request.AddProductRequest;
import com.javaboy.mani.request.ProductUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    @Autowired
    private final ProductRepository repo;
    @Autowired
    CategoryRepository catrepo;
    @Autowired
    cartItemRepository cartIRepo;
    @Autowired
    orderItemRepository orderIRepo;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product addProduct(AddProductRequest product) {
        if(ProductExists(product.getName(), product.getBrand())){
            throw new IllegalArgumentException("Product with name " + product
                    .getName() + " and brand " + product.getBrand() + " already exists.");
        }
        Category category = Optional.ofNullable(catrepo.findByName(product.getCategory().getName()))
                .orElseGet(() ->{
                    Category newCategory = new  Category(product.getCategory().getName());
                    return catrepo.save(newCategory);
                });
        product.setCategory(category);
        return repo.save(createProduct(product, category)) ;
    }

    private Boolean ProductExists(String name, String brand){
        return repo.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category);

    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return repo.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(repo :: save)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

     private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){
       existingProduct.setName(request.getName());
       existingProduct.setBrand(request.getBrand());
       existingProduct.setPrice(request.getPrice());
       existingProduct.setInventory(request.getInventory());
       existingProduct.setDescription(request.getDescription());
       existingProduct.setCategory(request.getCategory());
       Category category = catrepo.findByName(request.getCategory().getName());
       return existingProduct;
     }

    @Override
    public Product getProductById(Long productId) {
        return repo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

    @Override
    public void deleteProduct(Long productId) {
        repo.findById(productId)
                .ifPresentOrElse(product -> {
                    List<CartItem> cartItems = cartItemRepository.findByProductId(productId);
                     cartItems.forEach(cartItem -> {
                         Cart cart = cartItem.getCart();
                         cart.removeItem(cartItem);
                            cartIRepo.delete(cartItem);
                     });

                     List<OrderItem> orderItems = orderIRepo.findByProductId(productId);
                     orderItems.forEach(orderItem -> {
                         orderItem.setProduct(null);
                         orderIRepo.save(orderItem);
                     });
                       Optional.ofNullable(product.getCategory())
                               .ifPresent(category -> category.getProducts().remove(product));

                     product.setCategory(null);

                     productRepository.deleteById(product.getId());
                },() -> {
                    throw new EntityNotFoundException("Product not found with id: " + productId);
                });
    }

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public List<Product> getProductsByCategoryandBrand(String category, String brand) {
        return repo.findByCategoryAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return repo.findByCategory(category);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return repo.findByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public List<Product> getProductsByCategoryAndName(String category, String name) {
        return repo.findByCategoryAndName(category, name);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrandAndName(String category, String brand, String name) {
        return repo.findByCategoryAndBrandAndName(category, brand, name);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrandAndPrice(String category, String brand, Double price) {
        return repo.findByCategoryAndBrandAndPrice(category, brand, BigDecimal.valueOf(price));
    }




    @Override
    public List<Product> getProductsByBrand(String brand) {
        return repo.findByBrand(brand);
    }
}
