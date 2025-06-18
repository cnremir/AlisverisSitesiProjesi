package com.alisveris.AlisverisSitesi.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="Product")
@EqualsAndHashCode(exclude = "auction")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productId;
    private String productName;
    private Double productPrice;
    private String productDescripton;
    @ManyToOne
    @JoinColumn(name ="categoryId")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "subCategoryId")
    private SubCategory subCategory;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;



    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Images> images;
    @OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE)
    private Auction auction;

    @Lob
   private String base64Images;
}
