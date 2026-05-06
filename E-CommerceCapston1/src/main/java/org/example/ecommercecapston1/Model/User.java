package org.example.ecommercecapston1.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    @NotEmpty(message = "User ID cannot be empty")
    private String id;

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 6, message = "Username must be more than 5 characters long")
    private String userName;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 7, message = "Password must be more than 6 characters long")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "Password must contain both characters and digits")
    private String password;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotEmpty(message = "Role cannot be empty")
    @Pattern(regexp = "(?i)Admin|Customer", message = "Role must be either 'Admin' or 'Customer'")
    private String role;

    @NotNull(message = "Balance cannot be empty")
    @Positive(message = "Balance must be a positive number")
    private double balance;

    //Extra
    @NotNull(message = "Total spent cannot be empty")
    private double totalSpent;

    private boolean isVip;
}
