package com.msbank.inventory.core.usecase.impl;

import com.msbank.inventory.core.domain.Inventory;
import com.msbank.inventory.core.output.service.inventory.db.UpdateInventory;
import com.msbank.inventory.core.usecase.CreditInventoryUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CreditInventoryUseCaseImpl implements CreditInventoryUseCase {
    Logger LOGGER = LogManager.getLogger("Log4Core");

    private final UpdateInventory updateInventory;
    @Override
    public Mono<Inventory> execute(Inventory inventory) {
        return updateInventory.update(inventory);
    }

}