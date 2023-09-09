package com.sidenow.freshgreenish.domain.payment.controller;

import com.sidenow.freshgreenish.domain.payment.service.PaymentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentInfoController {
    private final PaymentInfoService paymentInfoService;

    @GetMapping("{type}/{purchaseId}/success")
    public String paymentSuccess(
            @PathVariable("type") String type,
            @PathVariable("purchaseId") Long purchaseId,
            @RequestParam("paymentKey") String paymentKey,
            @RequestParam("orderId") String orderId,
            @RequestParam("amount") Long totalAmount
    ) {
        return paymentInfoService.paymentSuccess(type, paymentKey, purchaseId, orderId, totalAmount);
    }
}
