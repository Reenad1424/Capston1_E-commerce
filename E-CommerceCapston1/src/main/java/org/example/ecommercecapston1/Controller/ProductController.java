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
    //Add review for specific product
    @PutMapping("/review/{productId}/{review}")
    public ResponseEntity<?> addReview(@PathVariable String productId, @PathVariable String review) {
        if (productService.addReview(productId, review)) {
            return ResponseEntity.status(200).body(new ApiResponse("Review added successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
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
    @GetMapping("/get-sorted/{categoryName}/{sortType}")
    public ResponseEntity<?> getSorted(@PathVariable String categoryName, @PathVariable String sortType) {

        if (!sortType.equalsIgnoreCase("cheapest") && !sortType.equalsIgnoreCase("expensive")) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid sort type! Please use 'cheapest' or 'expensive'"));
        }

        ArrayList<Product> sortedList = productService.getProductsSortedByPrice(categoryName, sortType);

        if (sortedList == null) {
            return ResponseEntity.status(400).body(new ApiResponse("No products found for this category"));
        }

        return ResponseEntity.status(200).body(sortedList);
    }


}
