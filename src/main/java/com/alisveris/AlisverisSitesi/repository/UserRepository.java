package com.alisveris.AlisverisSitesi.repository;

import com.alisveris.AlisverisSitesi.models.Product;
import com.alisveris.AlisverisSitesi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

  Optional<User> findUserByUserId(Integer userId);
  Optional<User> findUserByUserMailAdress(String mailAdress);
  Optional<User> findByUsername(String username);

  @Modifying
  void deleteByUserId(Integer userId);




}
