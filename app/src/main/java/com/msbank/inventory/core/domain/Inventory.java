package com.msbank.inventory.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Inventory {

    private Integer id;

    private Integer productId;

    private Integer quantity;

    public void debitQuantity(Integer quantity) {
        this.quantity -= quantity;
    }

    public void creditQuantity(Integer quantity) {
        this.quantity += quantity;
    }

}
