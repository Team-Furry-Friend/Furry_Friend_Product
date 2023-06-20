package com.v3.furry_friend_product.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.v3.furry_friend_product.product.dto.PageRequestDTO;
import com.v3.furry_friend_product.product.dto.PageResponseDTO;
import com.v3.furry_friend_product.product.dto.ProductDTO;
import com.v3.furry_friend_product.product.dto.ProductImageDTO;
import com.v3.furry_friend_product.product.dto.ProductRequestDataDTO;
import com.v3.furry_friend_product.product.entity.Product;
import com.v3.furry_friend_product.product.entity.ProductImage;

public interface ProductService {
    //데이터 삽입을 위한 메서드
    void register(ProductRequestDataDTO productRequestDataDTO);

    //데이터 목록을 위한 메서드
    PageResponseDTO<ProductDTO, Object []> getList(PageRequestDTO requestDTO);

    ProductDTO getProduct(Long pid);

    //DTO를 Entity로 변환
    //하나의 Entity가 아니라 Movie와 MovieImage로 변환이 되어야 해서
    //Map으로 리턴
    default Map<String, Object> dtoToEntity(ProductDTO productDTO){
        Map<String, Object> entityMap = new HashMap<>();

        Product product = Product.builder()
                .pcategory(productDTO.getPcategory())
                .pexplain(productDTO.getPexplain())
                .pname(productDTO.getPname())
                .pprice(productDTO.getPprice())
                .memberId(productDTO.getMid())
                .del(productDTO.isDel())
                .build();
        entityMap.put("product", product);

        //MovieImageDTO의 List
        List<ProductImageDTO> imageDTOList = productDTO.getImageDTOList();

        //MOvieImageDTO의 List를 MovieImage Entity의 List로 변환
        if(imageDTOList != null && imageDTOList.size() > 0){
            List<ProductImage> productImageList = imageDTOList.stream().map(productImageDTO -> {
                ProductImage productImage = ProductImage.builder()
                        .path(productImageDTO.getPath())
                        .imgName(productImageDTO.getImgName())
                        .product(product)
                        .build();
                return productImage;
            }).collect(Collectors.toList());
            entityMap.put("imgList",productImageList);
        }

        return entityMap;
    }

    //검색 결과를 DTO로 변환해주는 메서드
    default ProductDTO entitiesToDTO(Product product, List<ProductImage> productImages){
        ProductDTO productDTO = ProductDTO.builder()
                .pid(product.getPid())
                .pcategory(product.getPcategory())
                .pexplain(product.getPexplain())
                .pname(product.getPname())
                .pprice(product.getPprice())
                .del(product.isDel())
                .mid(product.getMemberId())
                .regDate(product.getRegDate())
                .modDate(product.getModDate())
                .build();

        // productImages 리스트에 null 값이 포함되어 있는 경우 예외 처리
        List<ProductImageDTO> productImageDTOList = productImages.stream()
            .filter(Objects::nonNull)
            .map(productImage -> {
                return ProductImageDTO.builder()
                    .imgName(productImage.getImgName())
                    .path(productImage.getPath())
                    .build();
            })
            .collect(Collectors.toList());
        productDTO.setImageDTOList(productImageDTOList);

        return productDTO;
    }
}
