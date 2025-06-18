package com.alisveris.AlisverisSitesi.repository;

import com.alisveris.AlisverisSitesi.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    Optional<Category> getCategoryByCategoryName(String name);
    List<Category> findAll();

    Optional<Category> getCategoryByCategoryId(Integer categoryId);
}
