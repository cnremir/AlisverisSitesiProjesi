package com.alisveris.AlisverisSitesi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "conversation")
@Builder
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer conversationId;

    @ManyToOne
    @JoinColumn(name = "user1_Id")
    private User user_1;

    @ManyToOne
    @JoinColumn(name = "user2_Id")
    private User user_2;


    private LocalDateTime createdTime;
}
