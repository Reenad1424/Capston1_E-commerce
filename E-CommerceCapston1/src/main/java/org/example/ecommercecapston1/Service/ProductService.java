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
    //Add Review
    public boolean addReview(String productId, String review) {
        //search for product
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equalsIgnoreCase(productId)) {

                // if the Review list not initialize we will initialize here
                if (products.get(i).getReviews() == null) {
                    products.get(i).setReviews(new ArrayList<>());
                }
                products.get(i).getReviews().add(review);
                return true;
            }
        }
        return false;
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
    // Method to sort products by price based on user preference (cheapest first or most expensive first)
    public ArrayList<Product> getProductsSortedByPrice(String categoryName, String sortType) {
        String foundCategoryId = null;

        // Step 1: Search for the Category ID using the Category Name provided
        for (int i = 0; i < categoryService.get().size(); i++) {
            if (categoryService.get().get(i).getName().equalsIgnoreCase(categoryName)) {
                foundCategoryId = categoryService.get().get(i).getId();
                break; // Category found, exit the loop
            }
        }

        // If no category matches the provided name, return null
        if (foundCategoryId == null) return null;

        // Step 2: Extract all products that belong to this Category ID
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getCategoryId().equalsIgnoreCase(foundCategoryId)) {
                filteredProducts.add(products.get(i));
            }
        }

        // Step 3: Perform Bubble Sort based on the user's sorting choice
        int n = filteredProducts.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                boolean shouldSwap = false;

                // Logic for "cheapest": Sort from Low to High price
                if (sortType.equalsIgnoreCase("cheapest")) {
                    if (filteredProducts.get(j).getPrice() > filteredProducts.get(j + 1).getPrice()) {
                        shouldSwap = true;
                    }
                }
                // Logic for "expensive": Sort from High to Low price
                else if (sortType.equalsIgnoreCase("expensive")) {
                    if (filteredProducts.get(j).getPrice() < filteredProducts.get(j + 1).getPrice()) {
                        shouldSwap = true;
                    }
                }

                // If the condition is met, swap the products in the list
                if (shouldSwap) {
                    Product temp = filteredProducts.get(j);
                    filteredProducts.set(j, filteredProducts.get(j + 1));
                    filteredProducts.set(j + 1, temp);
                }
            }
        }

        // If no products were found in this category, return null; otherwise, return the sorted list
        if (filteredProducts.isEmpty()) return null;

        return filteredProducts;
    }



}
