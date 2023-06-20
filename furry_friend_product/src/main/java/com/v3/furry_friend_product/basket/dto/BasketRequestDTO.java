package com.v3.furry_friend_product.basket.dto;

import com.v3.furry_friend_product.common.dto.JwtRequest;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BasketRequestDTO {

    private Long bid;
    private Long pid;
    private JwtRequest jwtRequest;
}
