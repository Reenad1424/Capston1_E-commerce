package org.example.ecommercecapston1.Service;

import org.example.ecommercecapston1.Model.MerchantStock;
import org.example.ecommercecapston1.Model.Product;
import org.example.ecommercecapston1.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class UserService {
    ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> get() {
        return users;
    }

    public boolean isIdExists(String id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean add(User user) {
        if (isIdExists(user.getId())) {
            return false;
        }
        users.add(user);
        return true;
    }

    public Boolean update(String id, User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equalsIgnoreCase(id)) {

                user.setId(id);
                users.set(i, user);
                return true;
            }
        }
        return false;
    }




    public Boolean delete(String id){
        for(int i =0;i<users.size();i++){
            if(users.get(i).getId().equalsIgnoreCase(id))
            {users.remove(i);
                return true;}

        }
        return false;
    }

   public int buyProduct(String userId, String productId, String merchantId,
                          ProductService productService,
                          MerchantStockService merchantStockService) {

        User currentUser = null;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equalsIgnoreCase(userId)) {
                currentUser = users.get(i);
                break;
            }
        }
        if (currentUser == null) return -1;

        Product currentProduct = null;
        for (int i = 0; i < productService.get().size(); i++) {
            if (productService.get().get(i).getId().equalsIgnoreCase(productId)) {
                currentProduct = productService.get().get(i);
                break;
            }
        }
        if (currentProduct == null) return -2;

        MerchantStock currentStock = null;
        for (int i = 0; i < merchantStockService.get().size(); i++) {
            if (merchantStockService.get().get(i).getMerchantId().equalsIgnoreCase(merchantId) &&
                    merchantStockService.get().get(i).getProductId().equalsIgnoreCase(productId)) {
                currentStock = merchantStockService.get().get(i);
                break;
            }
        }

        if (currentStock == null) return -3;
        if (currentStock.getStock() <= 0) return -4;

        if (currentUser.getBalance() < currentProduct.getPrice()) return -5;

        currentUser.setBalance(currentUser.getBalance() - currentProduct.getPrice());
        currentUser.setTotalSpent(currentUser.getTotalSpent() + currentProduct.getPrice());
        currentStock.setStock(currentStock.getStock() - 1);
        if (currentUser.getPurchasedProductsIDs() == null) {
            currentUser.setPurchasedProductsIDs(new ArrayList<>());
        }
        currentUser.getPurchasedProductsIDs().add(productId);

        if(currentUser.getTotalSpent() >= 5000) {
            currentUser.setVip(true);
        }

        return 1;
    }

    //Extra
    //Get user By role
    public ArrayList<User> getUsersByRole(String role) {
        ArrayList<User> filteredUsers = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getRole().equalsIgnoreCase(role)) {
                filteredUsers.add(users.get(i));
            }
        }
        return filteredUsers;
    }

    // Method to handle product returns: restores balance, updates total spent, and increases stock
    public int returnProduct(String userId, String productId, String merchantId,
                             ProductService productService, MerchantStockService merchantStockService) {

        // Step 1: Search for the user and verify existence
        User user = null;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equalsIgnoreCase(userId)) {
                user = users.get(i);
                break;
            }
        }
        if (user == null) return -1; // User not found

        //Initialize purchasedProductsIDs if it is null
        if (user.getPurchasedProductsIDs() == null) {
            user.setPurchasedProductsIDs(new ArrayList<>());
        }

        // Step 2: Check if the user actually purchased this product before
        boolean hasBought = false;
        for (int i = 0; i < user.getPurchasedProductsIDs().size(); i++) {
            if (user.getPurchasedProductsIDs().get(i).equalsIgnoreCase(productId)) {
                hasBought = true;
                user.getPurchasedProductsIDs().remove(i); // Remove one instance from purchase history
                break;
            }
        }
        if (!hasBought) return -5; // Error: Product was not purchased by this user

        // Step 3: Search for the product to verify existence and get its price
        Product product = null;
        for (int i = 0; i < productService.get().size(); i++) {
            if (productService.get().get(i).getId().equalsIgnoreCase(productId)) {
                product = productService.get().get(i);
                break;
            }
        }
        if (product == null) return -2; // Product not found

        // Step 4: System Protection (Cannot return a price higher than the user's total spent)
        if (user.getTotalSpent() < product.getPrice()) return -3;

        // Step 5: Search for the merchant's stock record to return the item
        for (int i = 0; i < merchantStockService.get().size(); i++) {
            MerchantStock stock = merchantStockService.get().get(i);
            if (stock.getMerchantId().equalsIgnoreCase(merchantId) && stock.getProductId().equalsIgnoreCase(productId)) {

                // Data Update: Refund balance + Deduct from total spent + Increase stock count
                user.setBalance(user.getBalance() + product.getPrice());
                user.setTotalSpent(user.getTotalSpent() - product.getPrice());
                stock.setStock(stock.getStock() + 1);

                // Revoke VIP status if total spent falls below the threshold (5000)
                if (user.getTotalSpent() < 5000) {
                    user.setVip(false);
                }

                return 1; // Return successful
            }
        }

        return -4; // Merchant stock record for this product not found
    }


    //VIP & Coupon
    public String generateCoupon(String userId) {
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getId().equalsIgnoreCase(userId)) {
                if (u.getTotalSpent() > 5000) {
                    u.setVip(true);
                    // random Coupon
                    int randomNum = (int)(Math.random() * 999);
                    return u.getUserName() + u.getTotalSpent() + randomNum;
                }
            }
        }
        return "No coupon available";
    }
    }



