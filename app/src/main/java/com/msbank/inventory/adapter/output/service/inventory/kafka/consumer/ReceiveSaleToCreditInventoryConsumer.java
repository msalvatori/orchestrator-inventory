package com.msbank.inventory.adapter.output.service.inventory.kafka.consumer;

import com.msbank.inventory.adapter.output.service.inventory.kafka.SaleMessage;
import com.msbank.inventory.core.domain.enums.SaleEvent;
import com.msbank.inventory.core.usecase.CreditInventoryUseCase;
import com.msbank.inventory.core.usecase.FindInventoryByProductIdUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;

import static com.msbank.inventory.core.constants.Constants.Iventory.INICIO_DA_DEVOLUCAO_DA_MERCADORIA;
import static com.msbank.inventory.core.constants.Constants.Iventory.FIM_DA_DEVOLUCAO_DA_MERCADORIA;
import static com.msbank.inventory.core.constants.Constants.Iventory.OPERACAO_R_SUCESSO;
import static com.msbank.inventory.core.constants.Constants.Kafka.SUCCESSFULLY_CONSUMED;

@Component
@RequiredArgsConstructor
public class ReceiveSaleToCreditInventoryConsumer {

    private final Logger LOGGER = LogManager.getLogger("Log4Core");

    private final CreditInventoryUseCase creditInventoryUseCase;

    private final FindInventoryByProductIdUseCase findInventoryByProductIdUseCase;

    private final ReactiveKafkaConsumerTemplate<String, SaleMessage> beanInventoryCredit;


    @EventListener(ApplicationStartedEvent.class)
    public void startKafkaCreditConsumer() {
        beanInventoryCredit
                .receiveAutoAck()
                .map(ConsumerRecord<String, SaleMessage>::value)
                 .doOnNext(saleMessage -> {
                     if(SaleEvent.EXECUTE_ROLLBACK.equals(saleMessage.saleEvent())) {
                        LOGGER.info(INICIO_DA_DEVOLUCAO_DA_MERCADORIA);
                        LOGGER.info(SUCCESSFULLY_CONSUMED, SaleMessage.class.getSimpleName(), saleMessage);
                        findInventoryByProductIdUseCase.execute(saleMessage.sale().getProductId())
                                .doOnSuccess(inventory -> {
                                    creditInventoryUseCase.execute(inventory);

                                    LOGGER.info(OPERACAO_R_SUCESSO, inventory);
                                }).subscribe();

                        LOGGER.info(FIM_DA_DEVOLUCAO_DA_MERCADORIA);
                    }

                })
                 .subscribe();
    }
}
