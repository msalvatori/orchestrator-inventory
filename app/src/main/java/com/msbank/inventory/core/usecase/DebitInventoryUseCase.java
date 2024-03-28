package com.msbank.inventory.core.usecase;

import com.msbank.inventory.core.domain.Inventory;
import com.msbank.inventory.core.domain.Sale;
import com.msbank.inventory.core.output.service.inventory.dto.response.DataResponseDto;
import reactor.core.publisher.Mono;


public interface DebitInventoryUseCase {
    Mono<Inventory> debit(Sale sale);

}
