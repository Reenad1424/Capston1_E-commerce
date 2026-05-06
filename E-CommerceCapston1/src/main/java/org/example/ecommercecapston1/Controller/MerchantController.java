package org.example.ecommercecapston1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercecapston1.Api.ApiResponse;
import org.example.ecommercecapston1.Model.Merchant;
import org.example.ecommercecapston1.Service.MerchantService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/get")
    public ResponseEntity<?> get(){
        return ResponseEntity.status(200).body(merchantService.get());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Merchant merchant, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        if(merchantService.add(merchant)) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant added successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("ID already exists, Merchant ID must be unique"));
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid Merchant merchant, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        if (!merchant.getId().equalsIgnoreCase(id)) {
            return ResponseEntity.status(400).body(new ApiResponse("ID cannot be changed! It must match the ID in the URL"));
        }
        if(merchantService.update(id, merchant))
            return ResponseEntity.status(200).body(new ApiResponse("Merchant updated successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        if(merchantService.delete(id))
            return ResponseEntity.status(200).body(new ApiResponse("Merchant deleted successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
    }
}
