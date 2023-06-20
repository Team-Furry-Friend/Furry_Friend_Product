package com.v3.furry_friend_product.product.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.v3.furry_friend_product.common.service.TokenService;
import com.v3.furry_friend_product.common.dto.JwtRequest;
import com.v3.furry_friend_product.product.dto.PageRequestDTO;
import com.v3.furry_friend_product.product.dto.PageResponseDTO;
import com.v3.furry_friend_product.product.dto.ProductDTO;
import com.v3.furry_friend_product.product.dto.ProductRequestDataDTO;
import com.v3.furry_friend_product.product.entity.Product;
import com.v3.furry_friend_product.product.entity.ProductImage;
import com.v3.furry_friend_product.product.repository.ProductImageRepository;
import com.v3.furry_friend_product.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository;

    private final TokenService tokenService;

    @Override
    public void register(ProductRequestDataDTO productRequestDataDTO) {

        ProductDTO productDTO = productRequestDataDTO.getProductDTO();
        JwtRequest jwtRequest = productRequestDataDTO.getJwtRequest();
        
        // 등록자 삽입
        Long memberId = tokenService.getMemberId(jwtRequest.getAccess_token());
        productDTO.setMid(memberId);

        Map<String, Object> entityMap = dtoToEntity(productDTO);
        //상품과 상품 이미지 정보 찾아오기
        Product product = (Product) entityMap.get("product");
        List<ProductImage> productImageList = (List<ProductImage>) entityMap.get("imgList");

        productRepository.save(product);
        if (productImageList != null) {
            productImageRepository.saveAll(productImageList);
        }

    }

    @Override
    public PageResponseDTO<ProductDTO, Object[]> getList(PageRequestDTO requestDTO) {

        log.info("PageRequestDTO" + requestDTO);

        Pageable pageable = requestDTO.getPageable(Sort.by("pid").descending());
        //데이터베이스에 요청
        Page<Object []> result = productRepository.getList(pageable);

        //Object 배열을 ProductDTO 타입으로 변경하기 위해서
        //함수를 생성
        //첫 번째 데이터가 Product
        //두 번째 데이터가 List<ProductImage>
        Function<Object [], ProductDTO> fn = (arr -> {
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            if (productImage == null) {
                // ProductImage가 없는 경우에 대한 처리
                return entitiesToDTO(product, Collections.emptyList());
            } else {
                // ProductImage가 있는 경우에 대한 처리
                return entitiesToDTO(product, Collections.singletonList(productImage));
            }
        });

        return new PageResponseDTO<>(result, fn);
    }

    @Override
    public ProductDTO getProduct(Long pid) {
        //데이터 베이스에서 결과 가져오기
        List<Object []> result = productRepository.getProductWithAll(pid);
        Product product = (Product) result.get(0)[0];
        System.out.println(result);
        List<ProductImage> productImageList = new ArrayList<>();
        result.forEach(arr -> {
            ProductImage productImage = (ProductImage) arr[1];
            productImageList.add(productImage);
        });

        return entitiesToDTO(product, productImageList);
    }
}
