package com.v3.furry_friend_product.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.v3.furry_friend_product.comment.entity.Comment;
import com.v3.furry_friend_product.product.entity.Product;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //이름을 기반으로 하는 메서드 생성이 가능
    @EntityGraph(attributePaths = {"product"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Comment> findByProduct(Product product);

    Comment findByRid(Long rid);
}
