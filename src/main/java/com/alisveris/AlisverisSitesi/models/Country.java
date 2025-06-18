package com.alisveris.AlisverisSitesi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Table(name = "Country")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Country {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Integer countryId;
private String countryName;
private String countryFlag;

}
