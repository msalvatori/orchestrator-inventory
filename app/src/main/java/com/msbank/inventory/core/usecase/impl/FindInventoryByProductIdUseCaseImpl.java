package com.msbank.inventory.core.usecase.impl;

import com.msbank.inventory.core.domain.Inventory;
import com.msbank.inventory.core.output.service.inventory.db.FindInventoryByProductId;
import com.msbank.inventory.core.usecase.FindInventoryByProductIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FindInventoryByProductIdUseCaseImpl implements FindInventoryByProductIdUseCase {

    private final FindInventoryByProductId findInventoryByProductId;
    @Override
    public Mono<Inventory> execute(Integer productId) {
        return  findInventoryByProductId.find(productId);
    }

}
