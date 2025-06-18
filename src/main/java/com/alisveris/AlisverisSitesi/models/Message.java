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
@Table(name = "message")
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Integer messageId;


    @ManyToOne
    @JoinColumn(name = "conversation")
    private Conversation conversation;

    @ManyToOne()
    @JoinColumn(name = "senderId")
    private User senderUser;

    private String messageText;
    private LocalDateTime createdAt;

}
