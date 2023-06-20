package com.v3.furry_friend_product.basket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.v3.furry_friend_product.basket.entity.Basket;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    // 장바구니 삭제
    @Modifying
    @Query("delete from Basket b where b.bid = :bid and b.memberid = :memberid")
    void deleteBasketByBasket_id(Long bid, Long memberid);

    // 장바구니 검색
    @Query("select b.bid, p, pi from Basket b left outer join Product p on b.product = p left join ProductImage pi on pi.product = p where b.memberid = :memberid")
    List<Object []> basketByMember(Long memberid);
}
