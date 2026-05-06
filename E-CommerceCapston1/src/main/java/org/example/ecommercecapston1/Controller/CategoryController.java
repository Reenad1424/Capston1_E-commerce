package org.example.ecommercecapston1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercecapston1.Api.ApiResponse;
import org.example.ecommercecapston1.Model.Category;
import org.example.ecommercecapston1.Service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity<?> get(){
        return ResponseEntity.status(200).body(categoryService.get());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Category category, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        if(categoryService.add(category)) {
            return ResponseEntity.status(200).body(new ApiResponse("Category added successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("ID already exists, Category ID must be unique"));
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid Category category, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        if (!category.getId().equalsIgnoreCase(id)) {
            return ResponseEntity.status(400).body(new ApiResponse("ID cannot be changed! It must match the ID in the URL"));
        }
        if(categoryService.update(id, category))
            return ResponseEntity.status(200).body(new ApiResponse("Category Updated successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        if(categoryService.delete(id))
            return ResponseEntity.status(200).body(new ApiResponse("Category Deleted successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
    }
}

