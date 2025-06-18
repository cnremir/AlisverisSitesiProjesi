package com.alisveris.AlisverisSitesi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="SubCategory")
@Builder
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subCategoryId;
    private String subCategoryName;

    // Üst kategoriye bağlı alt kategori
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    // Alt kategorideki ürünler
    @OneToMany(mappedBy = "subCategory")
    private List<Product> products;
}
