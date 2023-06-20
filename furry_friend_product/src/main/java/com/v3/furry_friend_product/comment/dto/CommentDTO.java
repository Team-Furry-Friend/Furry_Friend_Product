package com.v3.furry_friend_product.comment.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long rid;
    private Long pid;
    private Long mid;
    private String nickname;
    private String email;
    private String text;

    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
