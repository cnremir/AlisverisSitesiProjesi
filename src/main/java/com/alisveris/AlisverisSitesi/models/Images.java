package com.alisveris.AlisverisSitesi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sound.sampled.Port;
import java.sql.Blob;
import java.util.Date;

@Entity
@Data
@Table(name = "image")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer imageId;

    @Lob
    private Blob image;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

}
