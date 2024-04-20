package com.msbank.inventory.core.usecase;

import com.msbank.inventory.core.domain.Inventory;
import reactor.core.publisher.Mono;


public interface FindInventoryByProductIdUseCase {
    Mono<Inventory> execute(Integer productId);

}
