package com.msbank.inventory.core.usecase.impl;

import com.msbank.inventory.core.domain.Inventory;
import com.msbank.inventory.core.domain.Sale;
import com.msbank.inventory.core.domain.enums.SaleEvent;
import com.msbank.inventory.core.output.service.inventory.db.UpdateInventory;
import com.msbank.inventory.core.output.service.inventory.producer.SendUpdateInventory;
import com.msbank.inventory.core.usecase.DebitInventoryUseCase;
import com.msbank.inventory.core.usecase.FindInventoryByProductIdUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static com.msbank.inventory.core.constants.Constants.Iventory.ESTOQUE_INSUFICIENTE;

@Component
@RequiredArgsConstructor
public class DebitInventoryUseCaseImpl implements DebitInventoryUseCase {
    Logger LOGGER = LogManager.getLogger("Log4Core");

    private final DebitInventoryUseCase debitInventoryUseCase;

    private final FindInventoryByProductIdUseCase findInventoryByProductIdUseCase;

    private final SendUpdateInventory sendUpdateInventory;

    private final UpdateInventory updateInventory;

    @Override
    public Mono<Inventory> debit(Sale sale) {
        var inventory1 = findInventoryByProductIdUseCase.find(sale.getProductId())
                .filter(inventory -> inventory.getQuantity() >= sale.getQuantity())
                .doOnSuccess(inventory -> {
                    if (Objects.nonNull(inventory)) {
                        inventory.debitQuantity(sale.getQuantity());
                        updateInventory.update(inventory);
                        sendUpdateInventory.sendInventory(sale, SaleEvent.UPDATED_INVENTORY);
                    } else {
                        sendUpdateInventory.sendInventory(sale, SaleEvent.ROLLBACK_INVENTORY);
                        LOGGER.info(ESTOQUE_INSUFICIENTE);
                    }
                }).block();
     
        return Objects.nonNull(inventory1) ? Mono.just(inventory1) : Mono.empty();
    }

}
