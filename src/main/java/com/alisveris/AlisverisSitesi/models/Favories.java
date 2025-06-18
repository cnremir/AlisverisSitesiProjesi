package com.alisveris.AlisverisSitesi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "Favories")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class Favories {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer favoriId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="productId")
    private Product product;
}
