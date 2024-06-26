package com.sidenow.freshgreenish.domain.payment.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadyToKakaoPayInfo {
    private String cid;
    private String partner_order_id;
    private String partner_user_id;
    private String item_name;
    private Integer quantity;
    private Integer total_amount;
    private Integer val_amount;
    private Integer tax_free_amount;
    private String approval_url;
    private String fail_url;
    private String cancel_url;

    public void setCid(String cid) {
        this.cid = cid;
    }
}
