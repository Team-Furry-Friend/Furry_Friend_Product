package com.v3.furry_friend_product.basket.dto;

import com.v3.furry_friend_product.product.dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BasketResponseDTO {

    private Long bid;
    private ProductDTO productDTO;
}
