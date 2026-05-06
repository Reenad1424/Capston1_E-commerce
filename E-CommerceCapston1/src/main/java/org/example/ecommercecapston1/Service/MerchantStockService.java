package org.example.ecommercecapston1.Service;
import lombok.RequiredArgsConstructor;
import org.example.ecommercecapston1.Model.MerchantStock;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class MerchantStockService {
    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();
    private final ProductService productService;
    private final MerchantService merchantService;


    public ArrayList<MerchantStock> get() {
        return merchantStocks;
    }

    public boolean isIdExists(String id) {
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public int add(MerchantStock merchantStock) {
        if (isIdExists(merchantStock.getId())) {
            return -2;
        }

        boolean productFound = false;
        boolean merchantFound = false;

        for (int i = 0; i < productService.get().size(); i++) {
            if (productService.get().get(i).getId().equalsIgnoreCase(merchantStock.getProductId())) {
                productFound = true;
                break;
            }
        }

        for (int i = 0; i < merchantService.get().size(); i++) {
            if (merchantService.get().get(i).getId().equalsIgnoreCase(merchantStock.getMerchantId())) {
                merchantFound = true;
                break;
            }
        }

        if (productFound && merchantFound) {
            merchantStocks.add(merchantStock);
            return 1;
        }
        return -1;
    }


    public Boolean update(String id, MerchantStock merchantStock) {
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getId().equalsIgnoreCase(id)) {

                merchantStock.setId(id);

                merchantStocks.set(i, merchantStock);
                return true;
            }
        }
        return false;
    }

    public Boolean delete(String id) {
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getId().equalsIgnoreCase(id)) {
                merchantStocks.remove(i);
                return true;
            }
        }
        return false;
    }

    public int moreStock(String merchantId, String productId, int amount) {
        boolean merchantFound = false;
        for (int i = 0; i < merchantService.get().size(); i++) {
            if (merchantService.get().get(i).getId().equalsIgnoreCase(merchantId)) {
                merchantFound = true;
                break;
            }
        }

        boolean productFound = false;
        for (int i = 0; i < productService.get().size(); i++) {
            if (productService.get().get(i).getId().equalsIgnoreCase(productId)) {
                productFound = true;
                break;
            }
        }

        if (!merchantFound || !productFound) {
            return -1;
        }

        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getMerchantId().equalsIgnoreCase(merchantId) &&
                    merchantStocks.get(i).getProductId().equalsIgnoreCase(productId)) {

                int currentStock = merchantStocks.get(i).getStock();
                merchantStocks.get(i).setStock(currentStock + amount);
                return 1;
            }
        }

        return 0;
    }


    //Extra
    //Get Low Stock
    public ArrayList<MerchantStock> getLowStock() {
        ArrayList<MerchantStock> lowStockProduct = new ArrayList<>();
        // search for all the stocks in the system
        for (int i = 0; i < merchantStocks.size(); i++) {
            //check what if the product amount in the merchant < 5
            if (merchantStocks.get(i).getStock() <= 10) {
                lowStockProduct.add(merchantStocks.get(i));
            }
        }
        if (lowStockProduct.isEmpty())
            return null;
        return lowStockProduct;
    }


    //Find Merchants for Product
    public ArrayList<String> findMerchantsForProduct(String productId, MerchantService merchantService) {
        ArrayList<String> merchantNames = new ArrayList<>();

        //search in stocks about product need
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getProductId().equalsIgnoreCase(productId)) {
                String mId = merchantStocks.get(i).getMerchantId();

                //search for the merchant name using ID in the merchant service
                for (int j = 0; j < merchantService.get().size(); j++) {
                    if (merchantService.get().get(j).getId().equalsIgnoreCase(mId)) {
                        merchantNames.add(merchantService.get().get(j).getName());
                    }
                }
            }
        }
        if(merchantNames.isEmpty())
            return null;
        return merchantNames;
    }
    //Transfer Stock
    public int transferStock(String fromMId, String toMId, String pId, int amount) {
        MerchantStock fromS = null;
        MerchantStock toS = null;
        //search for the product stocks in both sender ind receiver merchant
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getProductId().equalsIgnoreCase(pId)) {
                if (merchantStocks.get(i).getMerchantId().equalsIgnoreCase(fromMId))
                    fromS = merchantStocks.get(i);
                if (merchantStocks.get(i).getMerchantId().equalsIgnoreCase(toMId))
                    toS = merchantStocks.get(i);
            }
        }

        if (fromS == null || toS == null)
            return -1;
        if (fromS.getStock() < amount)
            return -2;

        fromS.setStock(fromS.getStock() - amount);
        toS.setStock(toS.getStock() + amount);
        return 1;
    }

    //Total Inventory Value
    public Double getTotalInventoryValue(String merchantId, ProductService productService) {
        double total = 0;
        boolean merchantHasStock = false;

        //search what if the merchant has stocks or not
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getMerchantId().equalsIgnoreCase(merchantId)) {
                merchantHasStock = true;
                String pId = merchantStocks.get(i).getProductId();

                //gain product price from product service
                for (int j = 0; j < productService.get().size(); j++) {
                    if (productService.get().get(j).getId().equalsIgnoreCase(pId)) {
                        total += (productService.get().get(j).getPrice() * merchantStocks.get(i).getStock());
                    }
                }
            }
        }
        if(merchantHasStock)
            return total;
        return null;
    }



}