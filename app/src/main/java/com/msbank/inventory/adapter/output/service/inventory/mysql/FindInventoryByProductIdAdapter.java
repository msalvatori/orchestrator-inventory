package com.msbank.inventory.adapter.output.service.inventory.mysql;

import com.msbank.inventory.adapter.output.service.inventory.mysql.repository.InventoryRepository;
import com.msbank.inventory.adapter.output.service.inventory.mysql.repository.mapper.InventoryEntityMapper;
import com.msbank.inventory.core.domain.Inventory;
import com.msbank.inventory.core.output.service.inventory.db.FindInventoryByProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FindInventoryByProductIdAdapter implements FindInventoryByProductId {

    private final InventoryRepository inventoryRepository;
    @Override
    public Mono<Inventory> find(Integer productId) {
        var inventory = inventoryRepository.findByProductId(productId)
                .stream()
                .findFirst()
                .map(InventoryEntityMapper::toInventory);
        return inventory.map(Mono::just).orElse(Mono.empty());
    }
}
