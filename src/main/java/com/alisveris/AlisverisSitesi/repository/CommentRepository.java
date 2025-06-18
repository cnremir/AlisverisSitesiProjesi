package com.alisveris.AlisverisSitesi.repository;

import com.alisveris.AlisverisSitesi.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer>, JpaSpecificationExecutor {


    @Query("SELECT com FROM Comment com WHERE com.auction.auctionId = :auctionId order by com.commentTime")
    List<Comment> findCommentByAuctionId(@Param("auctionId") Integer auctionId);


    @Modifying
    @Transactional
    @Query("DELETE FROM Comment com where com.user.userId = :userId")
     void deleteByUserId(@Param("userId") Integer userId);
}
