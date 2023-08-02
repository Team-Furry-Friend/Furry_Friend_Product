package com.v3.furry_friend_product.basket.dto;

import java.time.LocalDateTime;

import com.v3.furry_friend_product.product.dto.ProductImageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BasketResponseDTO {

    private Long bid;
    private Long pid;       //상품 번호
    private String pcategory;//카테고리
    private String pname;   //상품명
    private String pexplain;//상품설명
    private Long pprice;    //원가
    private boolean del;    //판매여부

    private Long mid;

    //등록일과 수정
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private ProductImageDTO imageDTO;
}
