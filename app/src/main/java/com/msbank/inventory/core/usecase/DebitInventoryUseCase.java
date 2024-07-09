package com.msbank.inventory.core.usecase;

import com.msbank.inventory.core.domain.Sale;

public interface DebitInventoryUseCase {
    void execute(Sale sale);

}
