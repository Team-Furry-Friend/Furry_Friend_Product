package com.v3.furry_friend_product.comment.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.v3.furry_friend_product.comment.dto.CommentDTO;
import com.v3.furry_friend_product.comment.dto.CommentDataRequestDTO;
import com.v3.furry_friend_product.comment.service.CommentService;
import com.v3.furry_friend_product.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/reviews")
public class CommentController {
    private final CommentService commentService;

    //상품 번호에 해당하는 댓글 목록을 처리
    @GetMapping("/{pid}")
    public ApiResponse<List<CommentDTO>> list(@PathVariable("pid") Long pid){

        try {
            List<CommentDTO> result = commentService.getList(pid);
            return ApiResponse.success("댓글 조회 성공", result);
        }catch (Exception e){
            log.error("상품 리스트 반환 실패" + e.getMessage(), e);
            return ApiResponse.fail(400, "댓글 조회 실패");
        }

    }

    //댓글 추가
    @PostMapping("/")
    public ApiResponse addReview(@RequestBody CommentDataRequestDTO commentDataRequestDTO) {

        try {
            commentService.register(commentDataRequestDTO);
            return ApiResponse.success("댓글 등록 성공");
        }catch (Exception e){
            log.error("댓글 등록 실패 : " + e.getMessage(), e);
            return ApiResponse.fail(400, "댓글 등록 실패 : " + e.getMessage());
        }
    }

    //댓글 수정
    // @PutMapping("/{rid}")
    // public ResponseEntity<Long> updateReview(@PathVariable("rid") Long rid,
    //                                       @RequestBody CommentDTO commentDTO, @CookieValue(name = "access_token", required = false) String accessToken){
    //     commentDTO.setMid(tokenService.getMemberId(accessToken));
    //     Long result = commentService.modify(commentDTO);
    //     return new ResponseEntity<>(result, HttpStatus.OK);
    // }

    //댓글 삭제
    @DeleteMapping("/{rid}")
    public ApiResponse deleteReview(@PathVariable("rid") Long rid, @RequestHeader(value = "Authorization") String accessToken){
        try {
            commentService.remove(accessToken, rid);
            return ApiResponse.success("댓글 삭제 성공");
        }catch (AccessDeniedException accessDeniedException){
            log.error("댓글 삭제 권한이 없습니다. : " + accessDeniedException.getMessage(), accessDeniedException);
            return ApiResponse.error(403, "댓글 삭제 권한이 없습니다. : " + accessDeniedException.getMessage());
        }
        catch (Exception e) {
            log.error("댓글 삭제 실패 : " + e.getMessage(), e);
            return ApiResponse.fail(400, "댓글 삭제 실패 : " + e.getMessage());
        }
    }
}
