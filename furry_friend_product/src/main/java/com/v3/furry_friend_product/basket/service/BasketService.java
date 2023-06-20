package com.v3.furry_friend_product.basket.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v3.furry_friend_product.basket.dto.BasketRequestDTO;
import com.v3.furry_friend_product.basket.dto.BasketResponseDTO;
import com.v3.furry_friend_product.basket.entity.Basket;
import com.v3.furry_friend_product.basket.repository.BasketRepository;
import com.v3.furry_friend_product.common.service.TokenService;
import com.v3.furry_friend_product.product.dto.ProductDTO;
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

            Map<Long, Product> productMap = new HashMap<>();
            Map<Long, List<ProductImage>> productImageMap = new HashMap<>();

            List<BasketResponseDTO> basketResponseDTOList = new ArrayList<>();

            // result.forEach(arr -> {
            //
            //     Long bid = (Long) arr[0];
            //     Product product =  (Product) arr[1];
            //     ProductImage productImage = (ProductImage) arr[2];
            //
            //     if (productImageMap.containsKey(bid)) {
            //         productImageMap.get(bid).add(productImage);
            //     } else {
            //         List<ProductImage> images = new ArrayList<>();
            //         images.add(productImage);
            //         productImageMap.put(bid, product);
            //     }
            //
            //
            //     productMap.forEach((bid, product) -> {
            //         List<ProductImage> images = productImageMap.get(bid);
            //         basketResponseDTOList.add(entityToDTO(bid, p, images));
            //     });
            // });
            return basketResponseDTOList;
        } catch (Exception e){
            throw new Exception("에러 발생 : " + e.getMessage());
        }

    }

    // 장바구니 삭제하기
    @Transactional
    public void deleteBasketItem(BasketRequestDTO basketRequestDTO, Long memberId){
        basketRepository.deleteBasketByBasket_id(basketRequestDTO.getBid(), memberId);
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
    public BasketResponseDTO entityToDTO(Long bid, Product product, List<ProductImage> productImageList){

        List<ProductImageDTO> productImageDTOList = new ArrayList<>();
        productImageList.forEach(arr -> {
            ProductImageDTO productImageDTO = ProductImageDTO.builder()
                .imgName(arr.getImgName())
                .path(arr.getPath())
                .build();
            productImageDTOList.add(productImageDTO);
        });

        ProductDTO productDTO = ProductDTO.builder()
            .pid(product.getPid())
            .pname(product.getPname())
            .pcategory(product.getPcategory())
            .pexplain(product.getPexplain())
            .pprice(product.getPprice())
            .mid(product.getMemberId())
            .del(product.isDel())
            .regDate(product.getRegDate())
            .modDate(product.getModDate())
            .imageDTOList(productImageDTOList)
            .build();

        return BasketResponseDTO.builder()
            .bid(bid)
            .productDTO(productDTO)
            .build();
    }
}
