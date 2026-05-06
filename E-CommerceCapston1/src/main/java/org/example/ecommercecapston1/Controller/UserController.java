package org.example.ecommercecapston1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercecapston1.Api.ApiResponse;
import org.example.ecommercecapston1.Model.User;
import org.example.ecommercecapston1.Service.MerchantStockService;
import org.example.ecommercecapston1.Service.ProductService;
import org.example.ecommercecapston1.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;
        private final ProductService productService;
        private final MerchantStockService merchantStockService;

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.status(200).body(userService.get());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }


        if (userService.add(user)) {
            return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("ID already exists, User ID must be unique"));
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        if (!user.getId().equalsIgnoreCase(id)) {
            return ResponseEntity.status(400).body(new ApiResponse("ID cannot be changed! It must match the ID in the URL"));
        }
        if (userService.update(id, user))
            return ResponseEntity.status(200).body(new ApiResponse("User Updated successfully"));

        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
    }

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<?> delete(@PathVariable String id){
            if(userService.delete(id))
                return ResponseEntity.status(200).body(new ApiResponse("User Deleted successfully"));

            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }

       @PutMapping("/buy/{userId}/{productId}/{merchantId}")
       public ResponseEntity<?> buyProduct(@PathVariable String userId,
                                        @PathVariable String productId,
                                        @PathVariable String merchantId) {

        int result = userService.buyProduct(userId, productId, merchantId, productService, merchantStockService);

        return switch (result) {
            case 1 -> ResponseEntity.status(200).body(new ApiResponse("Purchase successful"));
            case -1 -> ResponseEntity.status(400).body(new ApiResponse("User ID not found"));
            case -2 -> ResponseEntity.status(400).body(new ApiResponse("Product ID not found"));
            case -3 -> ResponseEntity.status(400).body(new ApiResponse("Merchant does not have this product"));
            case -4 -> ResponseEntity.status(400).body(new ApiResponse("Product out of stock"));
            case -5 -> ResponseEntity.status(400).body(new ApiResponse("Insufficient balance"));
            default -> ResponseEntity.status(400).body(new ApiResponse("An error occurred"));
        };
    }
    //Extra
    // Return Product
    @PutMapping("/return/{userId}/{productId}/{merchantId}")
    public ResponseEntity<?> returnProduct(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId) {
        int result = userService.returnProduct(userId, productId, merchantId, productService, merchantStockService);
        return switch (result) {
            case 1 -> ResponseEntity.status(200).body(new ApiResponse("Product returned successfully"));
            case -1 -> ResponseEntity.status(400).body(new ApiResponse("User not found"));
            case -2 -> ResponseEntity.status(400).body(new ApiResponse("Product not found"));
            case -3 -> ResponseEntity.status(400).body(new ApiResponse("Invalid return: amount exceeds total spent"));
            case -4 -> ResponseEntity.status(400).body(new ApiResponse("Merchant stock record not found"));
            default -> ResponseEntity.status(400).body(new ApiResponse("Error occurred"));
        };
    }

    // Get Users by Role
    @GetMapping("/get-by-role/{role}")
    public ResponseEntity<?> getByRole(@PathVariable String role) {
        return ResponseEntity.status(200).body(userService.getUsersByRole(role));
    }

    // Generate Coupon/Check VIP
    @GetMapping("/coupon/{userId}")
    public ResponseEntity<?> getCoupon(@PathVariable String userId) {
        String coupon = userService.generateCoupon(userId);
        if (coupon.equals("No coupon available")) {
            return ResponseEntity.status(400).body(new ApiResponse(coupon));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Your VIP Coupon: " + coupon));
    }
}



