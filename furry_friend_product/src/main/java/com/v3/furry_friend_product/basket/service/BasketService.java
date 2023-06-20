package com.v3.furry_friend_product.basket.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.v3.furry_friend_product.basket.dto.BasketRequestDTO;
import com.v3.furry_friend_product.basket.dto.BasketResponseDTO;
import com.v3.furry_friend_product.basket.entity.Basket;
import com.v3.furry_friend_product.basket.repository.BasketRepository;
import com.v3.furry_friend_product.common.service.TokenService;
import com.v3.furry_friend_product.product.dto.ProductImageDTO;
import com.v3.furry_friend_product.product.entity.Product;
import com.v3.furry_friend_product.product.entity.ProductImage;
import com.v3.furry_friend_product.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class BasketService {

    private final BasketRepository basketRepository;

    private final ProductRepository productRepository;

    private final TokenService tokenService;

    // 장바구니 읽어오기
    public List<BasketResponseDTO> findBasketList(String accessToken) throws Exception{

        try {
            Long memberId = tokenService.getMemberId(accessToken);
            List<Object []> result = basketRepository.basketByMember(memberId);

            List<BasketResponseDTO> basketResponseDTOList = new ArrayList<>();


            result.forEach(arr -> {

                // 0번째는 찜 고유 번호
                // 1번째는 상품 정보
                // 2번째는 상품 이미지 1장
                basketResponseDTOList.add(entityToDTO((Long) arr[0], (Product) arr[1], (ProductImage) arr[2]));

            });

            return basketResponseDTOList;
        } catch (Exception e){
            throw new Exception("에러 발생 : " + e.getMessage());
        }

    }

    // 장바구니 삭제하기
    public void deleteBasketItem(Long bid, String accessToken){

        Long memberId = tokenService.getMemberId(accessToken);
        Basket basket = basketRepository.findByBid(bid);

        if (memberId.equals(basket.getMemberid())){
            basketRepository.deleteById(bid);
        }
    }

    public void saveBasket(BasketRequestDTO basketRequestDTO){

        Product product = productRepository.findByPid(basketRequestDTO.getPid());
        Long memberId = tokenService.getMemberId(basketRequestDTO.getJwtRequest().getAccess_token());

        Basket basket = Basket.builder()
            .product(product)
            .memberid(memberId).build();

        basketRepository.save(basket);
    }

    // Entity를 DTO로 변경해주는 메서드
    public BasketResponseDTO entityToDTO(Long bid, Product product, ProductImage productImage){

        ProductImageDTO productImageDTO = ProductImageDTO.builder()
            .imgName(productImage.getImgName())
            .path(productImage.getPath())
            .build();

        return BasketResponseDTO.builder()
            .bid(bid)
            .pid(product.getPid())
            .pname(product.getPname())
            .pcategory(product.getPcategory())
            .pexplain(product.getPexplain())
            .pprice(product.getPprice())
            .mid(product.getMemberId())
            .del(product.isDel())
            .regDate(product.getRegDate())
            .modDate(product.getModDate())
            .imageDTO(productImageDTO)
            .build();
    }
}
