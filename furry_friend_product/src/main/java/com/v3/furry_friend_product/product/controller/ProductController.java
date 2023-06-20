package com.v3.furry_friend_product.product.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.v3.furry_friend_product.common.response.ApiResponse;
import com.v3.furry_friend_product.product.dto.PageRequestDTO;
import com.v3.furry_friend_product.product.dto.PageResponseDTO;
import com.v3.furry_friend_product.product.dto.ProductDTO;
import com.v3.furry_friend_product.product.dto.ProductRequestDataDTO;
import com.v3.furry_friend_product.product.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    // 상품 목록
    @GetMapping("")
    public ApiResponse<PageResponseDTO> list(PageRequestDTO pageRequestDTO){

        try {
            PageResponseDTO pageResponseDTO = productService.getList(pageRequestDTO);

            return ApiResponse.success("상품 리스트 반환 성공", pageResponseDTO);
        }catch (Exception e){
            log.error("상품 리스트 반환 실패 : " + e.getMessage());
            return ApiResponse.fail(400, "상품 리스트 반환 실패 : " + e.getMessage());
        }
    }

    // 상품 등록
    @PostMapping("")
    public ApiResponse register(@RequestBody ProductRequestDataDTO productRequestDataDTO){

        try {
            productService.register(productRequestDataDTO);
            return ApiResponse.success("상품 등록 성공");
        }catch (Exception e){
            log.error("상품 등록 실패 : " + e.getMessage());
            return ApiResponse.fail(400, "상품 등록 실패 : " + e.getMessage());
        }
    }

    // 상품 상세 페이지
    @GetMapping("/detail")
    public ApiResponse<ProductDTO> read(@RequestParam("pid") Long pid){

        log.info("pid:" + pid);
        try {
            ProductDTO productDTO = productService.getProduct(pid);
            return ApiResponse.success("상세 페이지 불러오기 성공", productDTO);
        }catch (Exception e){
            log.error(e.getMessage());
            return ApiResponse.fail(400, "상세 페이지 불러오기 실패 : " + e.getMessage());
        }

    }
}
