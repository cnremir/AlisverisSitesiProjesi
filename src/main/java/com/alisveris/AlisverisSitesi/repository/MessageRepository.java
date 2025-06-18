package com.alisveris.AlisverisSitesi.repository;

import com.alisveris.AlisverisSitesi.models.Conversation;
import com.alisveris.AlisverisSitesi.models.Message;
import com.alisveris.AlisverisSitesi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer> {

    @Query("SELECT message from Message message where (message.conversation.user_1 = :user1 AND message.conversation.user_2 = :user2) OR " +
            "message.conversation.user_1 = :user2 AND message.conversation.user_2 = : user1")
List<Message> getMessageByUser1AndUser2(@Param("user1") User user1,@Param("user2")User user2);




    @Modifying
    @Transactional
    @Query("DELETE FROM Message message where message.conversation = :conversation")
    void deleteMessageByConversation(@Param("conversation") Conversation conversation);
}
