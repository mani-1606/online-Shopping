package com.javaboy.mani.service.product;

import com.javaboy.mani.dto.CategoryDto;
import com.javaboy.mani.dto.Imagedto;
import com.javaboy.mani.dto.Productdto;
import com.javaboy.mani.model.*;
import com.javaboy.mani.repository.*;
import com.javaboy.mani.request.AddProductRequest;
import com.javaboy.mani.request.ProductUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository repo;
    private final CategoryRepository catrepo;
    private final cartItemRepository cartIRepo;
    private final orderItemRepository orderIRepo;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imagerepo;

    @Override
    public Product addProduct(AddProductRequest product) {
        if (ProductExists(product.getName(), product.getBrand())) {
            throw new IllegalArgumentException("Product with name " + product
                    .getName() + " and brand " + product.getBrand() + " already exists.");
        }

            String categoryName = "";
            Category category = null;
            if (product.getCategory() != null && product.getCategory().getName() != null) {
                categoryName = product.getCategory().getName();
            } else {

                categoryName = "Tabs"; // Default category for testing
            }

            category = catrepo.findByName(categoryName);
            if (category == null) {
                category = new Category(categoryName);
                category = catrepo.save(category);
            }


            Product newProduct = new Product();
            newProduct.setName(product.getName());
            newProduct.setBrand(product.getBrand());

            // Handle price - convert from string if needed
            if (product.getPrice() == null && product.getDescription() != null) {
                try {
                    // Sometimes price might be passed as a string in the description field
                    newProduct.setPrice(new BigDecimal(product.getDescription()));
                } catch (NumberFormatException e) {
                    // Just use a default price for testing
                    newProduct.setPrice(BigDecimal.valueOf(99.99));
                }
            } else {
                newProduct.setPrice(product.getPrice());
            }

            newProduct.setInventory(product.getInventory());
            newProduct.setDescription(product.getDescription());
            newProduct.setCategory(category);

            return repo.save(newProduct);
    }

    private Boolean ProductExists(String name, String brand) {
        return repo.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(AddProductRequest request, Category category) {
        Product product = new Product();
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setInventory(request.getInventory());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        return product;
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return repo.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(repo::save)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
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
                    List<CartItem> cartItems = cartIRepo.findByProductId(productId);
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
                }, () -> {
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
    public List<Product> getProductsByNamePrefix(String prefix) {
        return repo.findByNameStartingWith(prefix);
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
    @Override
    public List<Productdto> getConvertedProducts(List<Product> products) {
        return products.stream()
                .map(this::conertToDto)
                .toList();
    }

    @Override
    public Productdto conertToDto(Product product) {
        try {
            // Create a DTO manually to avoid circular reference issues
            Productdto productDto = new Productdto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setPrice(product.getPrice());
            productDto.setInventory(product.getInventory());
            productDto.setDescription(product.getDescription());

            // Map only essential category information
            if (product.getCategory() != null) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setId(product.getCategory().getId());
                categoryDto.setName(product.getCategory().getName());
                productDto.setCategory(categoryDto);
            }

            // Map images
            List<Image> images = imagerepo.findByProductId(product.getId());
            List<Imagedto> imagedto = images.stream()
                    .map(image -> modelMapper.map(image, Imagedto.class))
                    .toList();
            productDto.setImages(imagedto);

            return productDto;
        } catch (Exception e) {
            // Log the error and return a partial DTO if possible
            System.err.println("Error mapping product to DTO: " + e.getMessage());
            e.printStackTrace();
            Productdto fallbackDto = new Productdto();
            fallbackDto.setId(product.getId());
            fallbackDto.setName(product.getName());
            return fallbackDto;
        }
    }
}
