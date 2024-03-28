package com.msbank.inventory.adapter.output.service.inventory.mysql;

import com.msbank.inventory.adapter.output.service.inventory.mysql.repository.InventoryRepository;
import com.msbank.inventory.adapter.output.service.inventory.mysql.repository.mapper.InventoryEntityMapper;
import com.msbank.inventory.core.domain.Inventory;
import com.msbank.inventory.core.output.service.inventory.db.UpdateInventory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UpdateInventoryAdapter implements UpdateInventory {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Override
    public Mono<Inventory> update(Inventory inventory) {
        var inventoryEntity = inventoryRepository.save(InventoryEntityMapper.toInventoryEntity(inventory));
        return Mono.just(InventoryEntityMapper.toInventory(inventoryEntity));
    }
}
