package com.v3.furry_friend_product.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.v3.furry_friend_product.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //상품 정보를 가지고 상품 이미지 정보와 댓글의 개수를 구해주는 메서드 페이지 단위로 구하기
    @Query("select p, pi from Product p left outer join ProductImage pi on pi.product = p" +
        " where p.del = false and (:keyword is null or p.pname like %:keyword%)" +
        " and (:type is null or p.pcategory = :type) group by p")
    Page<Object[]> getList(Pageable pageable, String type, String keyword);

    //상품 상세 보기를 위해서 특정 상품 아이디를 이용해서 위와 동일한 데이터를 찾아오는 쿼리
    @Query("select p, pi from Product p left outer join ProductImage pi on pi.product = p where p.pid = :pid group by pi")
    List<Object []> getProductWithAll(@Param("pid") Long pid);

    Product findByPid(Long pid);
}
