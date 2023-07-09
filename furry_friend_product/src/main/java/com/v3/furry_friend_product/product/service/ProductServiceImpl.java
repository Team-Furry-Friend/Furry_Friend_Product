package com.v3.furry_friend_product.product.service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.v3.furry_friend_product.common.dto.JwtResponse;
import com.v3.furry_friend_product.common.service.TokenService;
import com.v3.furry_friend_product.common.dto.JwtRequest;
import com.v3.furry_friend_product.product.dto.PageRequestDTO;
import com.v3.furry_friend_product.product.dto.PageResponseDTO;
import com.v3.furry_friend_product.product.dto.ProductDTO;
import com.v3.furry_friend_product.product.dto.ProductImageDTO;
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

    @Value("${member.getmemberName}")
    private String url;

    @Override
    public void register(ProductRequestDataDTO productRequestDataDTO) {

        ProductDTO productDTO = productRequestDataDTO.getProductDTO();
        JwtRequest jwtRequest = productRequestDataDTO.getJwtRequest();
        
        // 등록자 삽입
        JwtResponse jwtResponse = tokenService.getMemberId(jwtRequest.getAccess_token());
        productDTO.setMid(jwtResponse.getMemberId());

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

        Pageable pageable = requestDTO.getPageable(Sort.by("pid").descending());

        log.info("getType : " + requestDTO.getPage());
        log.info("getType : " + requestDTO.getSize());
        log.info("getType : " + requestDTO.getType());
        log.info("getType : " + requestDTO.getKeyword());
        Page<Object []> result = productRepository.getList(pageable, requestDTO.getType(), requestDTO.getKeyword());

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

        List<ProductImage> productImageList = new ArrayList<>();

        result.forEach(arr -> {
            ProductImage productImage = (ProductImage) arr[1];
            productImageList.add(productImage);
        });

        ProductDTO productDTO = entitiesToDTO(product, productImageList);
        productDTO.setMName(getMemberName(productDTO.getMid()));

        return productDTO;
    }

    // 상품 삭제 메서드
    @Override
    public void deleteProduct(Long pid, String accessToken) throws AccessDeniedException {

        JwtResponse jwtResponse = tokenService.getMemberId(accessToken);
        Product product = productRepository.findByPid(pid);
        if (jwtResponse.getMemberId().equals(product.getMemberId())){
            product.setDel(true);
            productRepository.save(product);
        }else{
            throw new AccessDeniedException("상품 삭제 권한이 없습니다.");
        }

    }


    // 상품 수정을 위한 메서드
    @Override
    public void updateProduct(ProductRequestDataDTO productRequestDataDTO) {
        ProductDTO productDTO = productRequestDataDTO.getProductDTO();
        JwtRequest jwtRequest = productRequestDataDTO.getJwtRequest();

        // 등록자 확인
        JwtResponse jwtResponse = tokenService.getMemberId(jwtRequest.getAccess_token());

        if (productDTO.getMid().equals(jwtResponse.getMemberId())){
            Map<String, Object> entityMap = dtoToEntity(productDTO);
            //상품과 상품 이미지 정보 찾아오기
            Product product = (Product) entityMap.get("product");
            product.setRegDate(productDTO.getRegDate());
            productRepository.save(product);
        }
    }

    @Override
    public List<ProductDTO> getpopularityList() {

        Pageable pageable = PageRequest.of(0, 5); // pageNumber: 페이지 번호, pageSize: 한 페이지에 표시할 결과 수
        List<Object[]> popularityList = productRepository.getpopularityList(pageable);
        List<ProductDTO> productDTOList = new ArrayList<>();
        popularityList.forEach(arr -> {

            ProductDTO productDTO = null;
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            if (productImage == null) {
                // ProductImage가 없는 경우에 대한 처리
                productDTO = entitiesToDTO(product, Collections.emptyList());
            } else {
                // ProductImage가 있는 경우에 대한 처리
                productDTO = entitiesToDTO(product, Collections.singletonList(productImage));
            }

            productDTOList.add(productDTO);
        });


        return productDTOList;
    }

    public String getMemberName(Long mid){

        // RestTemplate를 통한 API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = null;

        try {
            // RestTemplate를 통한 API 호출
            response = restTemplate.exchange(url + mid, HttpMethod.GET, entity, String.class);
        } catch (RestClientException re) {
            log.error("API 호출 오류 및 재시도 실행: " + re);
            try {
                // 5초 대기 후 재시도
                Thread.sleep(5000);
                response = restTemplate.exchange(url + mid, HttpMethod.GET, entity, String.class);
            } catch (Exception e) {
                log.error("재시도 중 Exception 발생: " + e);
            }
        }

        // Gson 객체를 생성합니다.
        Gson gson = new Gson();

        // JSON 문자열을 JsonObject로 변환합니다.
        JsonObject jsonObject = gson.fromJson(response.getBody(), JsonObject.class);

        // "data" 필드의 값을 추출합니다.
        String name = jsonObject.get("data").getAsString();


        log.info("response: " +name);

        return name;
    }
}
