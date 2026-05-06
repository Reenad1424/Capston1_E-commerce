package org.example.ecommercecapston1.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class Product {
    @NotEmpty(message = "Product ID cannot be empty")
    private String id;

    @NotEmpty(message = "Product name cannot be empty")
    @Size(min = 4, message = "Product name must be more than 3 characters long")
    private String name;

    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be a positive number")
    private double price;

    @NotEmpty(message = "Category ID cannot be empty")
    private String categoryId;

    private ArrayList<String> reviews;
}
