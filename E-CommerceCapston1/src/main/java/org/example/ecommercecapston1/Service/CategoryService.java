package org.example.ecommercecapston1.Service;

import org.example.ecommercecapston1.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {

    ArrayList<Category> categories=new ArrayList<>();

    public ArrayList<Category> get() {
        return categories;
    }

    public boolean isIdExists(String id) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean add(Category category) {
        if (isIdExists(category.getId())) {
            return false;
        }
        categories.add(category);
        return true;
    }

    public Boolean update(String id, Category category) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equalsIgnoreCase(id)) {
                category.setId(id);

                categories.set(i, category);
                return true;
            }
        }
        return false;
    }


    public Boolean delete(String id){
        for(int i =0;i<categories.size();i++){
            if(categories.get(i).getId().equalsIgnoreCase(id))
            {categories.remove(i);
            return true;}

        }
        return false;
    }

}
