package com.alisveris.AlisverisSitesi.repository;

import com.alisveris.AlisverisSitesi.models.Auction;
import com.alisveris.AlisverisSitesi.models.Requests;
import com.alisveris.AlisverisSitesi.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Requests,Integer> {

List<Requests> findRequestsByStatus(Status status);

Optional<Requests> findRequestsByAuction(Auction auction);

    @Modifying
    @Transactional
    @Query("DELETE FROM Requests req where req.user.userId = :userId")
     void deleteByUserId(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Requests req where req.auction =:auction ")
    void deleteRequestsByAuction(@Param("auction") Auction auction);


}
