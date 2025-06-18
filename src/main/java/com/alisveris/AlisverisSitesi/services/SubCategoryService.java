package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.models.SubCategory;
import com.alisveris.AlisverisSitesi.repository.SubCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    private final CategoryService categoryService;
    public SubCategoryService(SubCategoryRepository subCategoryRepository, CategoryService categoryService) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryService = categoryService;
    }

    public void saveSubCategory(SubCategory subCategory){
            subCategoryRepository.save(subCategory);

    }

    public List<SubCategory> findByParentCategoryCategoryId(Integer category_id){
        return subCategoryRepository.findByCategoryCategoryId(category_id);
    }

    public List<SubCategory> getSubCategory(){
        return subCategoryRepository.findAll();
    }

    public void addSubCategory(SubCategory subCategory){
        subCategoryRepository.save(subCategory);
    }


    public void addSubCategory(List<String> list,Integer categoryId){
        for(String s :list ){
            SubCategory subCategory = new SubCategory().builder()
                    .subCategoryName(s)
                    .category(categoryService.getCategoryByCategoryId(categoryId))
                    .build();
            addSubCategory(subCategory);
        }

    }

    public List<SubCategory> getSubCategoryByCategoryId(Integer categoryId){
        return subCategoryRepository.findSubCategoriesByCategoryId(categoryId);
    }
}
