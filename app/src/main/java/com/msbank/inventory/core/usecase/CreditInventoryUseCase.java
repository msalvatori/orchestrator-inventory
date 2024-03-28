package com.msbank.inventory.core.usecase;

import com.msbank.inventory.core.domain.Inventory;
import reactor.core.publisher.Mono;

public interface CreditInventoryUseCase {
     Mono<Inventory> credit(Inventory inventory);
}
