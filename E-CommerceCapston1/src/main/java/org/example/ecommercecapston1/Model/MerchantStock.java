package org.example.ecommercecapston1.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {
    @NotEmpty(message = "Stock ID cannot be empty")
    private String id;

    @NotEmpty(message = "Product ID cannot be empty")
    private String productId;

    @NotEmpty(message = "Merchant ID cannot be empty")
    private String merchantId;

    @NotNull(message = "Stock cannot be empty")
    @Min(value = 11, message = "Stock must be more than 10 at start")
    private int stock;
}
