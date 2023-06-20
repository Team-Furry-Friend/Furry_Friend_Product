package com.v3.furry_friend_product.common.dto;

import lombok.Getter;

@Getter
public class JwtResponse {

    private MemberData data;

    @Getter
    public static class MemberData {
        private Long memberId;
    }
}
