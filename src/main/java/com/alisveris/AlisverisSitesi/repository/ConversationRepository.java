package com.alisveris.AlisverisSitesi.repository;

import com.alisveris.AlisverisSitesi.models.Conversation;
import com.alisveris.AlisverisSitesi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation,Integer> {

    @Query("SELECT CASE WHEN con.user_1 = :user THEN con.user_2.userId ELSE con.user_1.userId END FROM " +
            "Conversation con WHERE con.user_1 = :user OR con.user_2 = :user")
    List<Integer> findChatPartners(@Param("user") User user);


    @Modifying
    @Transactional
    @Query("DELETE FROM Conversation c  where c.user_1 = :user OR c.user_2 = :user")
    void deleteByUser(@Param("user") User user);


    @Query("SELECT con FROM Conversation con where con.user_1 = :user OR con.user_2 = :user")
    List<Conversation> getConversationByUser(@Param("user") User user);
}
