package com.v3.furry_friend_product.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.v3.furry_friend_product.product.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
