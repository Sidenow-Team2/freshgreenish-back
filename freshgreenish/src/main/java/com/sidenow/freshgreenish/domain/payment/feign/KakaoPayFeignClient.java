package com.sidenow.freshgreenish.domain.payment.feign;

import com.sidenow.freshgreenish.domain.payment.kakao.*;
import com.sidenow.freshgreenish.domain.payment.util.PayConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "kakaopay", url = "https://kapi.kakao.com", configuration = {FeignErrorConfig.class})
public interface KakaoPayFeignClient {
    @PostMapping(value = "/v1/payment/ready")
    KakaoPayReadyInfo readyForPayment(@RequestHeader(PayConstants.AUTHORIZATION) String authorization,
                                      @RequestHeader(PayConstants.ACCEPT) String accept,
                                      @RequestHeader(PayConstants.CONTENT_TYPE) String contentType,
                                      @SpringQueryMap ReadyToKakaoPayInfo params);

    @PostMapping(value = "/v1/payment/approve")
    KakaoPaySuccessInfo successForPayment(@RequestHeader(PayConstants.AUTHORIZATION) String authorization,
                                          @RequestHeader(PayConstants.ACCEPT) String accept,
                                          @RequestHeader(PayConstants.CONTENT_TYPE) String contentType,
                                          @SpringQueryMap RequestForKakaoPayInfo query);

    @PostMapping(value = "/v1/payment/cancel")
    KakaoPayCancelInfo cancelForPayment(@RequestHeader(PayConstants.AUTHORIZATION) String authorization,
                                        @RequestHeader(PayConstants.ACCEPT) String accept,
                                        @RequestHeader(PayConstants.CONTENT_TYPE) String contentType,
                                        @SpringQueryMap RequestForKakaoPayCancelInfo params);

    @PostMapping(value = "/v1/payment/subscription")
    KakaoSubReadyInfo readyForSubscription(@RequestHeader(PayConstants.AUTHORIZATION) String authorization,
                                           @RequestHeader(PayConstants.ACCEPT) String accept,
                                           @RequestHeader(PayConstants.CONTENT_TYPE) String contentType,
                                           @SpringQueryMap ReadyToKakaoSubInfo params);

    @PostMapping(value = "/v1/payment/manage/subscription/inactive")
    KakaoPayRegularCancelInfo cancelForSubscription(@RequestHeader(PayConstants.AUTHORIZATION) String authorization,
                                                    @RequestHeader(PayConstants.ACCEPT) String accept,
                                                    @RequestHeader(PayConstants.CONTENT_TYPE) String contentType,
                                                    @SpringQueryMap RequestForKakaoRegularPayCancel params);
}
