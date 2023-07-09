package com.v3.furry_friend_product.comment.service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.v3.furry_friend_product.comment.dto.CommentDTO;
import com.v3.furry_friend_product.comment.dto.CommentDataRequestDTO;
import com.v3.furry_friend_product.comment.entity.Comment;
import com.v3.furry_friend_product.comment.repository.CommentRepository;
import com.v3.furry_friend_product.common.dto.JwtResponse;
import com.v3.furry_friend_product.common.service.TokenService;
import com.v3.furry_friend_product.product.entity.Product;
import com.v3.furry_friend_product.product.service.ProductServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final TokenService tokenService;

    private final ProductServiceImpl productServiceImpl;

    @Override
    public List<CommentDTO> getList(Long pid) {
        Product product = Product.builder().pid(pid).build();
        List<Comment> result = commentRepository.findByProduct(product);

        return result.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    public void register(CommentDataRequestDTO commentDataRequestDTO) {

        JwtResponse jwtResponse = tokenService.getMemberId(commentDataRequestDTO.getJwtRequest().getAccess_token());
        Long mid = jwtResponse.getMemberId();

        String nickname = productServiceImpl.getMemberName(mid);

        CommentDTO commentDTO = commentDataRequestDTO.getCommentDTO();
        commentDTO.setMid(mid);
        commentDTO.setNickname(nickname);

        Comment comment = dtoToEntity(commentDTO);
        commentRepository.save(comment);
    }

    //수정과 삽입은 동일하다.
    @Override
    public Long modify(CommentDTO commentDTO) {
        Comment comment = dtoToEntity(commentDTO);
        commentRepository.save(comment);
        return comment.getRid();
    }

    @Override
    public void remove(String accessToken, Long rid) throws AccessDeniedException {
        JwtResponse jwtResponse = tokenService.getMemberId(accessToken);

        Comment comment = commentRepository.findByRid(rid);
        if (comment.getMemberId().equals(jwtResponse.getMemberId())){
            commentRepository.deleteById(rid);
        }else{
            throw new AccessDeniedException("댓글 삭제 권한이 없습니다.");
        }


    }
}