package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.models.Category;
import com.alisveris.AlisverisSitesi.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public Category getByCategoryId(String name){

        return categoryRepository.getCategoryByCategoryName(name).orElse(null);
    }

    public List<Category> getAllCategories(){

        return categoryRepository.findAll();
    }

    public void addCategory(List  <Category> categories){
        for(Category category : categories){
            categoryRepository.save(category);
        }

    }

    public Category getCategoryByCategoryId(Integer categoryId){
        return categoryRepository.getCategoryByCategoryId(categoryId).orElse(null);
    }


}
