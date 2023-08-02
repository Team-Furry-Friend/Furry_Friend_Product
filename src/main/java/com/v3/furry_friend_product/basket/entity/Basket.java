package com.v3.furry_friend_product.basket.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.v3.furry_friend_product.common.entity.BaseEntity;
import com.v3.furry_friend_product.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity     //여러 엔티티간 연관관계를 정의
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder    //복합 객체의 생성 과정과 표현 방법을 분리하여 동일한 생성 절차에서 서로 다른 표현 결과를 만들 수 있게 하는 패턴
public class Basket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bid;

    private Long memberid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}
