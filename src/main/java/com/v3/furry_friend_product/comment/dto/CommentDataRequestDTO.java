package com.v3.furry_friend_product.comment.dto;

import com.v3.furry_friend_product.common.dto.JwtRequest;

import lombok.Getter;

@Getter
public class CommentDataRequestDTO {
    private CommentDTO commentDTO;
    private JwtRequest jwtRequest;
}
