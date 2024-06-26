package com.sidenow.freshgreenish.domain.payment.toss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossPaySuccessInfo {
    private Integer totalAmount;
    private String paymentKey;
    private String lastTransactionKey;
    private String method;
    private String orderId;
    private String orderName;
    private Checkout checkout;
    private String mId;
    private String requestedAt;
    private String approvedAt;
    private TossCard card;
    private Receipt receipt;
    private MobilePhone mobilePhone;
    private Transfer transfer;
    private String orderStatus;

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
