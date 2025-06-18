package com.alisveris.AlisverisSitesi.specifications;

import com.alisveris.AlisverisSitesi.models.Product;

import com.alisveris.AlisverisSitesi.models.User;
import com.alisveris.AlisverisSitesi.repository.CommentRepository;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;

public class ProductSpecification {



public static Specification<Product> getCategory(String category){
        return (root, query, criteriaBuilder) -> {
                if(category == null|| category.isEmpty()){
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.equal(root.get("category"),category);


        };

}

public static Specification<Product> getForPrice(Double minPrice,Double maxPrice){
        return ((root, query, criteriaBuilder) -> {
                if(maxPrice == null && minPrice == null){
                        return criteriaBuilder.conjunction();
                }
                else if(maxPrice == null){

                        return criteriaBuilder.lessThanOrEqualTo(root.get("productPrice"),minPrice);
                }
                else if (minPrice == null)
                        return criteriaBuilder.greaterThan(root.get("productPrice"),maxPrice);
                else
                        return criteriaBuilder.between(root.get("productPrice"),minPrice,maxPrice);
        });
}
public static Specification<Product> sortByRating(Integer rating1,Integer rating2){
            return ((root, query, criteriaBuilder) -> {
                    if(rating1 == null && rating2 == null){
                            return criteriaBuilder.conjunction();

                    }
                    else if (rating2 == null){
                            return criteriaBuilder.greaterThan(root.get("productRating"),rating1);

                    }
                    else if (rating1 == null){
                           return criteriaBuilder.lessThanOrEqualTo(root.get("productRating"),rating2);
                    }
                    else{
                            return criteriaBuilder.between(root.get("productRating"),rating1,rating2);
                    }


            });
}








                public static Specification<Product> sortedByComment(){

                        return (root, query, criteriaBuilder) -> {

                                var order = criteriaBuilder.desc(root.get("productComment"));
                                query.orderBy(order);
                                return criteriaBuilder.conjunction();

                        };



}




}












