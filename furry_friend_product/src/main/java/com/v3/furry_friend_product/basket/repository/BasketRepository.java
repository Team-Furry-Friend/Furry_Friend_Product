package com.v3.furry_friend_product.basket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.v3.furry_friend_product.basket.entity.Basket;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    Basket findByBid(Long bid);

    // 장바구니 검색
    @Query("select b.bid, p, pi from Basket b left outer join Product p on b.product = p left join ProductImage pi on pi.product = p where b.memberid = :memberid group by p")
    List<Object []> basketByMember(Long memberid);
}
