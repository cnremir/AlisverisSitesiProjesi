package com.alisveris.AlisverisSitesi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Requests")
@Builder
@Data
public class Requests {
    @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 private Integer requestId;

@OneToOne
@JoinColumn(name = "auctionId")
private Auction auction;

@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JoinColumn(name = "userId")
private User user;

@Enumerated(EnumType.STRING)
private Status status;

}
