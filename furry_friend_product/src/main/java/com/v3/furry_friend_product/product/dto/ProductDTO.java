package com.v3.furry_friend_product.product.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
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

    //builder()라는 메서드를 이용해서 생성할 때 기본으로 사용
    @Builder.Default
    private List<ProductImageDTO> imageDTOList = new ArrayList<>();
}
