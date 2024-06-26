package com.sidenow.freshgreenish.domain.payment.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sidenow.freshgreenish.domain.payment.kakao.ReadyToKakaoPayInfo;
import com.sidenow.freshgreenish.domain.payment.toss.ReadyToTossPayInfo;
import com.sidenow.freshgreenish.domain.purchase.entity.Purchase;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Setter
    @Column(updatable = false)
    private LocalDateTime paymentDate;
    @Setter
    private LocalDateTime deliveryDate;
    private Integer quantity = 0;

    /* ---------- 카카오 ---------- */

    private String cid;
    private String tid;
    private String sid;
    private String partnerOrderId;
    private String partnerUserId;
    private String itemName;
    private Integer totalAmount;
    private Integer valAmount;
    private Integer taxFreeAmount;
    private String approvalUrl;
    private String cancelUrl;

    /* ---------- 공통 ---------- */

    private String failUrl;

    /* ---------- 토스 ---------- */

    private Integer amount;
    private String orderId;
    private String orderName;
    private String successUrl;
    private String paymentKey;

    /* -------------------- */

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PURCHASE_ID")
    @JsonManagedReference
    private Purchase purchase;

    @Builder
    public PaymentInfo(Purchase purchase, String orderName) {
        this.purchase = purchase;
        this.orderName = orderName;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public void setPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setTossPaymentInfo(ReadyToTossPayInfo body) {
        this.failUrl = body.getFailUrl();
        this.amount = body.getAmount();
        this.orderId = body.getOrderId();
        this.orderName = body.getOrderName();
        this.successUrl = body.getSuccessUrl();
    }

    public void setKakaoPaymentInfo(ReadyToKakaoPayInfo params, String tid) {
        this.cid = params.getCid();
        this.tid = tid;
        this.partnerOrderId = params.getPartner_order_id();
        this.partnerUserId = params.getPartner_user_id();
        this.itemName = params.getItem_name();
        this.quantity = params.getQuantity();
        this.totalAmount = params.getTotal_amount();
        this.valAmount = params.getVal_amount();
        this.taxFreeAmount = params.getTax_free_amount();
        this.approvalUrl = params.getApproval_url();
        this.failUrl = params.getFail_url();
        this.cancelUrl = params.getCancel_url();
    }
}

