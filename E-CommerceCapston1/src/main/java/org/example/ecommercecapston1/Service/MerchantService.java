package org.example.ecommercecapston1.Service;
import org.example.ecommercecapston1.Model.Merchant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantService {

    ArrayList<Merchant> merchants = new ArrayList<>();

    public ArrayList<Merchant> get() {
        return merchants;
    }

    public boolean isIdExists(String id) {
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean add(Merchant merchant) {
        if (isIdExists(merchant.getId())) {
            return false;
        }
        merchants.add(merchant);
        return true;
    }

    public Boolean update(String id, Merchant merchant) {
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getId().equalsIgnoreCase(id)) {

                merchant.setId(id);

                merchants.set(i, merchant);
                return true;
            }
        }
        return false;
    }

    public Boolean delete(String id) {
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getId().equalsIgnoreCase(id)) {
                merchants.remove(i);
                return true;
            }
        }
        return false;
    }
}
