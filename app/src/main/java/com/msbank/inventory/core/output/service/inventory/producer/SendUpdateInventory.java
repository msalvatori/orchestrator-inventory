package com.msbank.inventory.core.output.service.inventory.producer;

import com.msbank.inventory.core.domain.Sale;
import com.msbank.inventory.core.domain.enums.SaleEvent;
import com.msbank.inventory.core.output.service.inventory.dto.response.DataResponseDto;
import reactor.core.publisher.Mono;

public interface SendUpdateInventory {
    Mono<DataResponseDto> sendInventory(Sale sale, SaleEvent event);
}
