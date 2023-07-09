package com.v3.furry_friend_product.basket.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.v3.furry_friend_product.basket.dto.BasketRequestDTO;
import com.v3.furry_friend_product.basket.dto.BasketResponseDTO;
import com.v3.furry_friend_product.basket.dto.MemberBasketResponseDTO;
import com.v3.furry_friend_product.basket.service.BasketService;
import com.v3.furry_friend_product.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/baskets")
public class BasketController {

    private final BasketService basketService;

    @GetMapping("")
    public ApiResponse<List<BasketResponseDTO>> basket(@RequestHeader(value = "Authorization") String accessToken){

        try {
            List<BasketResponseDTO> basketResponseDTOList = basketService.findBasketList(accessToken);
            return ApiResponse.success("찜 목록 불러오기 성공", basketResponseDTOList);
        }catch (Exception e) {
            log.error("찜 목록 불러오기 실패 : " + e.getMessage(), e);
            return ApiResponse.fail(400, "찜 목록 불러오기 실패 : " + e.getMessage());
        }
    }

    @PostMapping("")
    public ApiResponse basketSave(@RequestBody BasketRequestDTO basketRequestDTO){

        try {
            basketService.saveBasket(basketRequestDTO);
            return ApiResponse.success("찜 성공");
        }catch (Exception e) {
            log.error("찜 실패 : " + e.getMessage(), e);
            return ApiResponse.fail(400, "찜 실패 : " + e.getMessage());
        }
    }

    @DeleteMapping("/{bid}")
    public ApiResponse basketDelete(@PathVariable("bid") Long bid, @RequestHeader(value = "Authorization") String accessToken) {

        try {
            basketService.deleteBasketItem(bid, accessToken);
            return ApiResponse.success("찜 삭제 성공");
        } catch (Exception e) {
            log.error("찜 삭제 실패 : " + e.getMessage(), e);
            return ApiResponse.fail(400, "찜 삭제 실패 : " + e.getMessage());
        }
    }

    @GetMapping("/member")
    public ApiResponse<List<MemberBasketResponseDTO>> getMemberbasket(@RequestHeader(value = "Authorization") String accessToken){

        try {
            List<MemberBasketResponseDTO> memberBasketResponseDTOList = basketService.getMemberBasket(accessToken);
            return ApiResponse.success("사용자 찜 목록 불러오기 성공", memberBasketResponseDTOList);
        }catch (Exception e) {
            log.error("사용자 찜 목록 불러오기 실패 : " + e.getMessage(), e);
            return ApiResponse.fail(400, "사용자 찜 목록 불러오기 실패 : " + e.getMessage());
        }
    }
}
