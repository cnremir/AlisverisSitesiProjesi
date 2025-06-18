package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.dto.CreateUserRequest;
import com.alisveris.AlisverisSitesi.models.User;
import com.alisveris.AlisverisSitesi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServices {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;



    public UserServices(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;


    }

    public User getUser(Integer userId){
        return userRepository.findUserByUserId(userId).orElse(null);

    }
    public Optional<User> getUser(String username){
        return  userRepository.findByUsername(username);

    }
@Transactional
    public void updateUser(User user){
        userRepository.saveAndFlush(user);

    }

    public boolean isMailAdressUsing(String mailAdress){
        if(userRepository.findUserByUserMailAdress(mailAdress).isEmpty())
            return false;

        return true;
    }
    public void SetUserDetails(Integer userId){
        User user = userRepository.findUserByUserId(userId).orElse(null);

    }
    public User activeUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication.getPrincipal() instanceof UserDetails){
                String username = ((UserDetails) authentication.getPrincipal()).getUsername();
                User user = userRepository.findByUsername(username).orElse(null);
                if(user != null){
                    return user;
                }

            }
        return null;
    }



    public User createUser(CreateUserRequest request){
        User user = new User()
                .builder()
                .name(request.name())
                .username(request.username())
                .surname(request.surname())
                .userMailAdress(request.userMailAdress())
                .balance(request.balance())
                .password(passwordEncoder.encode(request.password())) // burası çok önemli. Şifreleme için
                .authorities(request.authorities())
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .accountNonLocked(true)
                .build();

        return userRepository.save(user);
    }
    public List<User> getAllUser(){

        return userRepository.findAll();


    }


    @Transactional
    public void deleteUserByUserId(Integer userId){

        userRepository.deleteByUserId(userId);

    }





}
