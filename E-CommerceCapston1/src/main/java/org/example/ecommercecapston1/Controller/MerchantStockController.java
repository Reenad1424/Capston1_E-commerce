package org.example.ecommercecapston1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommercecapston1.Api.ApiResponse;
import org.example.ecommercecapston1.Model.MerchantStock;
import org.example.ecommercecapston1.Service.MerchantService;
import org.example.ecommercecapston1.Service.MerchantStockService;
import org.example.ecommercecapston1.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;
    private final MerchantService merchantService;
    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.status(200).body(merchantStockService.get());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        int result = merchantStockService.add(merchantStock);

        if (result == 1) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock added successfully"));
        }

        if (result == -2) {
            return ResponseEntity.status(400).body(new ApiResponse("ID already exists, Stock ID must be unique"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Merchant ID or Product ID not found"));
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        if (!merchantStock.getId().equalsIgnoreCase(id)) {
            return ResponseEntity.status(400).body(new ApiResponse("ID cannot be changed! It must match the ID in the URL"));
        }

        if (merchantStockService.update(id, merchantStock)) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock updated successfully"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Merchant Stock not found"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (merchantStockService.delete(id)) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Merchant Stock not found"));
    }

    @PutMapping("/add-stock/{merchantId}/{productId}/{amount}")
    public ResponseEntity<?> addMoreStock(@PathVariable String merchantId, @PathVariable String productId, @PathVariable int amount) {
        int result = merchantStockService.moreStock(merchantId, productId, amount);

        if (result == 1) {
            return ResponseEntity.status(200).body(new ApiResponse("Stock increased successfully"));
        } else if (result == -1) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant or Product not found"));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant does not have this product in stock"));
        }
    }

    //Extra
    //Get low stock
    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStock() {
        ArrayList<MerchantStock> low = merchantStockService.getLowStock();
        if (low == null)
            return ResponseEntity.status(400).body(new ApiResponse("No low stock found"));
        return ResponseEntity.status(200).body(low);
    }

    //Find Merchants for Product
    @GetMapping("/merchants-by-product/{productId}")
    public ResponseEntity<?> findMerchants(@PathVariable String productId) {
        ArrayList<String> names = merchantStockService.findMerchantsForProduct(productId, merchantService);
        if (names == null) return
                ResponseEntity.status(400).body(new ApiResponse("No merchants found for this product"));
        return ResponseEntity.status(200).body(names);
    }

    //Transfer Stock
    @PutMapping("/transfer/{fromMId}/{toMId}/{pId}/{amount}")
    public ResponseEntity<?> transfer(@PathVariable String fromMId, @PathVariable String toMId,
                                      @PathVariable String pId, @PathVariable int amount) {
        int res = merchantStockService.transferStock(fromMId, toMId, pId, amount);
        if (res == 1)
            return ResponseEntity.status(200).body(new ApiResponse("Transfer successful"));
        if (res == -1)
            return ResponseEntity.status(400).body(new ApiResponse("Merchant stock records not found"));
        return ResponseEntity.status(400).body(new ApiResponse("Insufficient stock to transfer"));
    }

    //Total Inventory Value
    @GetMapping("/total-value/{merchantId}")
    public ResponseEntity<?> getTotalValue(@PathVariable String merchantId) {
        Double total = merchantStockService.getTotalInventoryValue(merchantId, productService);
        if (total == null)
            return ResponseEntity.status(400).body(new ApiResponse("Merchant has no stock"));
        return ResponseEntity.status(200).body(new ApiResponse("Total inventory value: " + total));
    }
}
