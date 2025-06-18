package com.alisveris.AlisverisSitesi.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "auction")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "auction")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer auctionId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "productId")
    @ToString.Exclude
    private Product product;


    private Double startingPrice;
    private Double currentPrice;

    @CreationTimestamp
    private LocalDateTime startingTime;

    private LocalDateTime finishTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Double accrual;

    private long kalanSure;
    private boolean showBox;
    private String boxMessage;
    @OneToMany(mappedBy = "auction", cascade = CascadeType.REMOVE) // Product silindiğinde Comments de silinir
    private List<Comment> commentList;


    @Override
    public String toString() {
        return "Auction{" +
                "auctionId=" + auctionId +
                ", startingPrice=" + startingPrice +
                ", currentPrice=" + currentPrice +
                ", status=" + status +
                ", kalanSure=" + kalanSure +
                ", productId=" + (product != null ? product.getProductId() : "null") +
                '}';
    }


}
