package com.alisveris.AlisverisSitesi.repository;

import com.alisveris.AlisverisSitesi.models.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory,Integer> {

    List<SubCategory> findByCategoryCategoryId(Integer categoryId);
    List<SubCategory> findAll();


    @Query("SELECT sub FROM SubCategory sub WHERE sub.category.categoryId = :categoryId")
    List<SubCategory> findSubCategoriesByCategoryId(@Param("categoryId") Integer categoryId);

}
