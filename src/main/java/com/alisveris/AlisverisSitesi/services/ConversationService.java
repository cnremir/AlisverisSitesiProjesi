package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.models.Conversation;
import com.alisveris.AlisverisSitesi.models.User;
import com.alisveris.AlisverisSitesi.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Conversation addConversation(Conversation conversation){
        return conversationRepository.save(conversation);
    }

    public List<Integer> findMessagedPeople(User user){
        return  conversationRepository.findChatPartners(user);
    }
    @Transactional
    public void deleteConversationByUser(User user){
        conversationRepository.deleteByUser(user);
    }

    public List<Conversation> getConversationByUser(User user){
        return conversationRepository.getConversationByUser(user);
    }
}
