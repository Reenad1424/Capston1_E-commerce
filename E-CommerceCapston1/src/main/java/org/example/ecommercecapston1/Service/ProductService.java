package org.example.ecommercecapston1.Service;
import org.example.ecommercecapston1.Model.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class ProductService {
    ArrayList<Product> products = new ArrayList<>();
    private final CategoryService categoryService;

    public ProductService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public ArrayList<Product> get() {
        return products;
    }

    public boolean isIdExists(String id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public int add(Product product) {
        if (isIdExists(product.getId())) {
            return -2;
        }

        for (int i = 0; i < categoryService.get().size(); i++) {
            if (categoryService.get().get(i).getId().equalsIgnoreCase(product.getCategoryId())) {
                products.add(product);
                return 1;
            }
        }
        return -1;
    }

    public Boolean update(String id, Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equalsIgnoreCase(id)) {

                for (int j = 0; j < categoryService.get().size(); j++) {
                    if (categoryService.get().get(j).getId().equalsIgnoreCase(product.getCategoryId())) {

                        product.setId(id);

                        products.set(i, product);
                        return true;
                    }
                }
            }
        }
        return false;
    }



    public Boolean delete(String id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equalsIgnoreCase(id)) {
                products.remove(i);
                return true;
            }
        }
        return false;
    }

    //Extra
    // Method to add a review only if the user has purchased the product
    public int addReview(String userId, String productId, String review, UserService userService) {
        // Step 1: Search for the user in the system
        User currentUser = null;
        for (int i = 0; i < userService.get().size(); i++) {
            if (userService.get().get(i).getId().equalsIgnoreCase(userId)) {
                currentUser = userService.get().get(i);
                break;
            }
        }
        if (currentUser == null) return -1; // User not found

        if (currentUser.getPurchasedProductsIDs() == null) {
            currentUser.setPurchasedProductsIDs(new ArrayList<>());
        }

        // Step 2: Check if the user has actually purchased this product
        boolean hasPurchased = false;
        for (int i = 0; i < currentUser.getPurchasedProductsIDs().size(); i++) {
            if (currentUser.getPurchasedProductsIDs().get(i).equalsIgnoreCase(productId)) {
                hasPurchased = true;
                break;
            }
        }
        if (!hasPurchased) return -2; // User has not purchased this product

        // Step 3: Find the product and add the review
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equalsIgnoreCase(productId)) {
                if (products.get(i).getReviews() == null) {
                    products.get(i).setReviews(new ArrayList<>());
                }
                products.get(i).getReviews().add(review);
                return 1; // Success
            }
        }
        return -3; // Product not found
    }

    //Get by Category Name
    public ArrayList<Product> getByCategoryName(String categoryName) {
        String foundCategoryId = null;

        // search for category ID using category Name
        for (int i = 0; i < categoryService.get().size(); i++) {
            if (categoryService.get().get(i).getName().equalsIgnoreCase(categoryName)) {
                foundCategoryId = categoryService.get().get(i).getId();
                break;
            }
        }
        if (foundCategoryId == null)
            return null;

        // search for product the belonged to the category ID
        ArrayList<Product> categoryProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getCategoryId().equalsIgnoreCase(foundCategoryId)) {
                categoryProducts.add(products.get(i));
            }
        }

        if (categoryProducts.isEmpty()) return null;
        return categoryProducts;
    }


    //Sort by Price
    // Method to get products from cheapest to most expensive
    public ArrayList<Product> getCheapestProducts(String categoryName) {
        String foundCategoryId = null;

        // Step 1: Find Category ID by Name
        for (int i = 0; i < categoryService.get().size(); i++) {
            if (categoryService.get().get(i).getName().equalsIgnoreCase(categoryName)) {
                foundCategoryId = categoryService.get().get(i).getId();
                break;
            }
        }
        if (foundCategoryId == null) return null;

        // Step 2: Filter products
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getCategoryId().equalsIgnoreCase(foundCategoryId)) {
                filteredProducts.add(products.get(i));
            }
        }

        if (filteredProducts.isEmpty()) return null;

        // Step 3: Bubble Sort (Low to High)
        int n = filteredProducts.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (filteredProducts.get(j).getPrice() > filteredProducts.get(j + 1).getPrice()) {
                    Product temp = filteredProducts.get(j);
                    filteredProducts.set(j, filteredProducts.get(j + 1));
                    filteredProducts.set(j + 1, temp);
                }
            }
        }
        return filteredProducts;
    }

    // Method to get products from most expensive to cheapest
    public ArrayList<Product> getMostExpensiveProducts(String categoryName) {
        String foundCategoryId = null;

        // Step 1: Find Category ID by Name
        for (int i = 0; i < categoryService.get().size(); i++) {
            if (categoryService.get().get(i).getName().equalsIgnoreCase(categoryName)) {
                foundCategoryId = categoryService.get().get(i).getId();
                break;
            }
        }
        if (foundCategoryId == null) return null;

        // Step 2: Filter products
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getCategoryId().equalsIgnoreCase(foundCategoryId)) {
                filteredProducts.add(products.get(i));
            }
        }

        if (filteredProducts.isEmpty()) return null;

        // Step 3: Bubble Sort (High to Low)
        int n = filteredProducts.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (filteredProducts.get(j).getPrice() < filteredProducts.get(j + 1).getPrice()) {
                    Product temp = filteredProducts.get(j);
                    filteredProducts.set(j, filteredProducts.get(j + 1));
                    filteredProducts.set(j + 1, temp);
                }
            }
        }
        return filteredProducts;
    }


}
