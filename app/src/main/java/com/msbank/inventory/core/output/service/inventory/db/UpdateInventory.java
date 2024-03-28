package com.msbank.inventory.core.output.service.inventory.db;

import com.msbank.inventory.core.domain.Inventory;
import reactor.core.publisher.Mono;

public interface UpdateInventory {
    Mono<Inventory> update(Inventory inventory);
}
