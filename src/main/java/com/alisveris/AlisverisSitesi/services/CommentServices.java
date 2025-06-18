package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.dto.CreateCommentRequest;
import com.alisveris.AlisverisSitesi.models.Auction;
import com.alisveris.AlisverisSitesi.models.Comment;
import com.alisveris.AlisverisSitesi.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service

public class CommentServices {

    private final CommentRepository commentRepository;

    public CommentServices(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentByAuction(Auction auction){

        return commentRepository.findCommentByAuctionId(auction.getAuctionId());
    }


    public Comment addComment(CreateCommentRequest createCommentRequest){

        Comment comment = Comment.builder()
                .auction(createCommentRequest.auction())
                .commentText(createCommentRequest.commentText())
                .commentTime(createCommentRequest.commentTime())
                .user(createCommentRequest.user())
                .parentCommentId(createCommentRequest.parentCommentId())
                .build();

        commentRepository.save(comment);
        return comment;

    }
    @Transactional
    public void deleteCommentByUserId(Integer userId){
        commentRepository.deleteByUserId(userId);

    }
}
