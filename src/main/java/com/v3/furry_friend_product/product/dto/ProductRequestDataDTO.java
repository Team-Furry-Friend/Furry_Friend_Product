package com.v3.furry_friend_product.product.dto;

import com.v3.furry_friend_product.common.dto.JwtRequest;

import lombok.Getter;

@Getter
public class ProductRequestDataDTO {

    private ProductDTO productDTO;
    private JwtRequest jwtRequest;
}
