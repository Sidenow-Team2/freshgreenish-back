package com.sidenow.freshgreenish.domain.payment.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayHeader {
    private String accept;
    private String adminKey;
    private String contentType;
}
