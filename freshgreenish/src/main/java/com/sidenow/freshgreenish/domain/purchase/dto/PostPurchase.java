package com.sidenow.freshgreenish.domain.purchase.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostPurchase {
    private Integer count;
    private Integer totalPrice;
}
