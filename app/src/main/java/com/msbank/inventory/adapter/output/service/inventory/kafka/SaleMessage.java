package com.msbank.inventory.adapter.output.service.inventory.kafka;

import com.msbank.inventory.core.domain.Sale;
import com.msbank.inventory.core.domain.enums.SaleEvent;

public record SaleMessage(
        Sale sale,
        SaleEvent saleEvent
) {

}
