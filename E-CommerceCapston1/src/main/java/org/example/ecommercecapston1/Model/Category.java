package org.example.ecommercecapston1.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {
    @NotEmpty(message = "Category ID cannot be empty")
    private String id;

    @NotEmpty(message = "Category name cannot be empty")
    @Size(min = 4, message = "Category name must be more than 3 characters long")
    private String name;
}
