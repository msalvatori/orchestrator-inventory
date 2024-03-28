package com.msbank.inventory.core.domain;

import com.msbank.inventory.core.domain.enums.SalesStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class Sale {

    private Integer id;
    private Integer productId;
    private Integer userId;
    private BigDecimal value;
    private SalesStatus status;
    private Integer quantity;

}
