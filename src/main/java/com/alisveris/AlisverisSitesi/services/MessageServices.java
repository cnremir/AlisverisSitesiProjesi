package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.models.Conversation;
import com.alisveris.AlisverisSitesi.models.Message;
import com.alisveris.AlisverisSitesi.models.User;
import com.alisveris.AlisverisSitesi.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageServices {
private final MessageRepository messageRepository;

    public MessageServices(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessagesUser1BetweenUser2(User user1, User user2){
        return messageRepository.getMessageByUser1AndUser2(user1,user2);
    }


    public Message addMessage(Message message){
        return messageRepository.save(message);
    }
    @Transactional
    public void deleteMessageByConversation(Conversation conversation){
        messageRepository.deleteMessageByConversation(conversation);
    }
}
