package com.alisveris.AlisverisSitesi.controller;

import com.alisveris.AlisverisSitesi.models.Conversation;
import com.alisveris.AlisverisSitesi.models.Message;
import com.alisveris.AlisverisSitesi.models.User;
import com.alisveris.AlisverisSitesi.services.ConversationService;
import com.alisveris.AlisverisSitesi.services.MessageServices;
import com.alisveris.AlisverisSitesi.services.UserServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Controller
public class MessageController {
    private final UserServices userServices;
    private final MessageServices messageServices;

    private final ConversationService conversationService;


    public MessageController(UserServices userServices, MessageServices messageServices, ConversationService conversationService) {
        this.userServices = userServices;
        this.messageServices = messageServices;
        this.conversationService = conversationService;
    }



    public List<User> users(List<Integer> userIdLists){
        List<Integer> list = new ArrayList<>(new HashSet<>(userIdLists));
        List<User> userList = new ArrayList<>();
        for(Integer a : list){
            userList.add(userServices.getUser(a));

        }

        return userList;
    }
    @GetMapping("/messages")
    public String messagePages(Model model) {
        model.addAttribute("chatPartner", null);
        model.addAttribute("users",users(conversationService.findMessagedPeople(userServices.activeUser())));
        model.addAttribute("messages", Collections.emptyList());
        model.addAttribute("loggedInUserId", userServices.activeUser().getUserId());
        model.addAttribute("message",new Message());
        model.addAttribute("user",null);


        return "messagePage";
    }





    @GetMapping("/message/{userId}")
    public String messagePage(@PathVariable("userId") Integer userId_2, Model model) {
        model.addAttribute("chatPartner", userServices.getUser(userId_2).getUsername());
        model.addAttribute("users",users(conversationService.findMessagedPeople(userServices.activeUser())));
        List<Message> messageList = messageServices.getMessagesUser1BetweenUser2(userServices.activeUser(), userServices.getUser(userId_2));
        model.addAttribute("messages", messageList);
        model.addAttribute("loggedInUserId", userServices.activeUser().getUserId());
        model.addAttribute("message",new Message());
        model.addAttribute("user",userServices.getUser(userId_2));


        return "messagePage";
    }


    @PostMapping("/sendMessage/{userId}")
    public String sendMessage(@ModelAttribute("message") Message message, Model model, @PathVariable("userId") Integer user2Id) {
        Conversation conversation = Conversation.builder()
                .user_1(userServices.activeUser())
                .user_2(userServices.getUser(user2Id))
                .createdTime(LocalDateTime.now())
                .build();
        model.addAttribute("user",userServices.getUser(user2Id));


        Message message1 = Message.builder()
                .messageText(message.getMessageText())
                .conversation(conversation)
                .senderUser(userServices.activeUser())
                .createdAt(LocalDateTime.now())
                .build();


        conversationService.addConversation(conversation);
        messageServices.addMessage(message1);

        return "redirect:/message/{userId}";

    }

}
