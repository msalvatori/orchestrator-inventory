package com.msbank.inventory.adapter.output.service.inventory.kafka;


import com.msbank.inventory.core.domain.Sale;
import com.msbank.inventory.core.domain.enums.SaleEvent;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SaleMessage {

    private Sale sale;

    private SaleEvent saleEvent;

}
