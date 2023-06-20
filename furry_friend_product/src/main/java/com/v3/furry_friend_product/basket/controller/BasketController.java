package com.v3.furry_friend_product.basket.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.v3.furry_friend_product.basket.dto.BasketRequestDTO;
import com.v3.furry_friend_product.basket.dto.BasketResponseDTO;
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



    @GetMapping("/{access_token}")
    public ApiResponse<List<BasketResponseDTO>> basket(@PathVariable("access_token") String accessToken){

        try {
            List<BasketResponseDTO> basketResponseDTOList = basketService.findBasketList(accessToken);
            return ApiResponse.success("찜 목록 불러오기 성공", basketResponseDTOList);
        }catch (NullPointerException nullPointerException){
            log.error("NullPointerException: " + nullPointerException.getMessage());
            return ApiResponse.error(500, "access_token 미포함 : " + nullPointerException.getMessage());
        }catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.fail(400, "찜 목록 불러오기 실패 : " + e.getMessage());
        }
    }

    @PostMapping("/")
    public ApiResponse basketSave(@RequestBody BasketRequestDTO basketRequestDTO){

        try {
            basketService.saveBasket(basketRequestDTO);
            return ApiResponse.success("찜 성공");
        }catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.fail(400, "찜 실패 : " + e.getMessage());
        }
    }

    @DeleteMapping("/basket")
    public ResponseEntity<String> basketDelete(@RequestParam(value = "bid") Long bid, @CookieValue(name = "access_token", required = false) String accessToken) {

        try {
            BasketRequestDTO basketRequestDTO = BasketRequestDTO.builder()
                .bid(bid)
                .build();
            // basketService.deleteBasketItem(basketRequestDTO, tokenService.getMemberId(accessToken));

            String redirectUrl = "/basket/basket"; // 리다이렉트할 URL
            return ResponseEntity.ok().body(redirectUrl);
        } catch (Exception e) {
            // 실패했을 때의 동작을 구현
            log.error("삭제 실패");
            return ResponseEntity.badRequest().build(); // 실패 응답
        }
    }
}
