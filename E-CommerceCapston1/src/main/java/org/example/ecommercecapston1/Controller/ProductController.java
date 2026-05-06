package org.example.ecommercecapston1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercecapston1.Api.ApiResponse;
import org.example.ecommercecapston1.Model.Product;
import org.example.ecommercecapston1.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.status(200).body(productService.get());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        int result = productService.add(product);

        if (result == 1)
            return ResponseEntity.status(200).body(new ApiResponse("Product added successfully"));

        if (result == -2)
            return ResponseEntity.status(400).body(new ApiResponse("Product ID already exists, must be unique"));

        return ResponseEntity.status(400).body(new ApiResponse("Category ID not found"));
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid Product product, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        if (!product.getId().equalsIgnoreCase(id)) {
            return ResponseEntity.status(400).body(new ApiResponse("ID cannot be changed! It must match the ID in the URL"));
        }
        if (productService.update(id, product))
            return ResponseEntity.status(200).body(new ApiResponse("Product updated successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("Product or Category not found"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (productService.delete(id))
            return ResponseEntity.status(200).body(new ApiResponse("Product Deleted successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
    }

    //Extra
    // Endpoint to add a review after verifying the purchase
    @PutMapping("/add-review/{userId}/{productId}/{review}")
    public ResponseEntity<?> addReview(@PathVariable String userId,
                                       @PathVariable String productId,
                                       @PathVariable String review) {

        int result = productService.addReview(userId, productId, review, userService);

        return switch (result) {
            case 1 -> ResponseEntity.status(200).body(new ApiResponse("Review added successfully"));
            case -1 -> ResponseEntity.status(400).body(new ApiResponse("User not found"));
            case -2 -> ResponseEntity.status(400).body(new ApiResponse("You can only review products you have actually purchased!"));
            case -3 -> ResponseEntity.status(400).body(new ApiResponse("Product not found"));
            default -> ResponseEntity.status(400).body(new ApiResponse("An error occurred"));
        };
    }

    //Get Products By category Name
    @GetMapping("/get-product-ByCategory/{categoryName}")
    public ResponseEntity<?> getProductByCategory(@PathVariable String categoryName) {
        ArrayList<Product> products = productService.getByCategoryName(categoryName);

        if (products == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Not found products in this category"));
        }

        return ResponseEntity.status(200).body(products);
    }

    // Get products sorted by price (cheapest or expensive)
    // Endpoint for cheapest products
    @GetMapping("/cheapest/{categoryName}")
    public ResponseEntity<?> getCheapest(@PathVariable String categoryName) {
        ArrayList<Product> list = productService.getCheapestProducts(categoryName);
        if (list == null) return ResponseEntity.status(400).body(new ApiResponse("No products found"));
        return ResponseEntity.status(200).body(list);
    }

    // Endpoint for most expensive products
    @GetMapping("/expensive/{categoryName}")
    public ResponseEntity<?> getExpensive(@PathVariable String categoryName) {
        ArrayList<Product> list = productService.getMostExpensiveProducts(categoryName);
        if (list == null) return ResponseEntity.status(400).body(new ApiResponse("No products found"));
        return ResponseEntity.status(200).body(list);
    }

}
