package com.msbank.inventory.adapter.output.service.inventory.mysql.repository.mapper;

import com.msbank.inventory.adapter.output.service.inventory.mysql.repository.entity.InventoryEntity;
import com.msbank.inventory.core.domain.Inventory;

public class InventoryEntityMapper {

    public static Inventory toInventory(InventoryEntity inventoryEntity) {
        return Inventory.builder()
                        .productId(inventoryEntity.getProductId())
                        .id(inventoryEntity.getId())
                        .quantity(inventoryEntity.getQuantity())
                .build();
    }

    public static InventoryEntity toInventoryEntity(Inventory inventory) {
        return InventoryEntity.builder()
                .productId(inventory.getProductId())
                .id(inventory.getId())
                .quantity(inventory.getQuantity())
                .build();
    }

}
