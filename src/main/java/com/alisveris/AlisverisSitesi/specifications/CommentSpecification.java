package com.alisveris.AlisverisSitesi.specifications;

import com.alisveris.AlisverisSitesi.models.Comment;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class CommentSpecification {

    public Specification<Comment> getCommentFromProduct(Integer productId){
        return (root, query, criteriaBuilder) -> {

          return criteriaBuilder.equal(root.get("product").get("productId"),productId);


        };


    }


}
