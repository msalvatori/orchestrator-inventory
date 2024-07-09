package com.msbank.inventory.adapter.output.service.inventory.kafka.consumer;

import com.msbank.inventory.adapter.output.service.inventory.kafka.SaleMessage;
import com.msbank.inventory.core.domain.Sale;
import com.msbank.inventory.core.domain.enums.SaleEvent;
import com.msbank.inventory.core.output.service.inventory.producer.SendUpdateInventory;
import com.msbank.inventory.core.usecase.DebitInventoryUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static com.msbank.inventory.core.constants.Constants.Iventory.INICIO_DA_SEPARACAO_DE_MERCADORIA;
import static com.msbank.inventory.core.constants.Constants.Iventory.FIM_DA_SEPARACAO_DE_MERCADORIA;
import static com.msbank.inventory.core.constants.Constants.Iventory.OPERACAO_ROLLBACK;
import static com.msbank.inventory.core.constants.Constants.Iventory.ERROR_MESSAGE;
import static com.msbank.inventory.core.constants.Constants.Kafka.SUCCESSFULLY_CONSUMED;


@Component
@RequiredArgsConstructor
public class ReceiveSaleToDebitInventoryConsumer {

    Logger LOGGER = LogManager.getLogger("Log4Core");

    private final DebitInventoryUseCase debitInventoryUseCase;


    private final SendUpdateInventory sendUpdateInventory;

    private final ReactiveKafkaConsumerTemplate<String, SaleMessage> beanInventoryDebit;

    @EventListener(ApplicationStartedEvent.class)
    public void startKafkaDebitConsumer() {
        AtomicReference<Sale> sale;
        sale = new AtomicReference<>(Sale.builder().build());
        beanInventoryDebit
               .receiveAutoAck()
               .map(ConsumerRecord<String, SaleMessage>::value)
               .doOnNext(saleMessage -> {
                   if(SaleEvent.PREPARE_INVENTORY.equals(saleMessage.saleEvent())) {
                       LOGGER.info(SUCCESSFULLY_CONSUMED, SaleMessage.class.getSimpleName(), saleMessage.toString());
                       LOGGER.info(INICIO_DA_SEPARACAO_DE_MERCADORIA);
                       sale.set(saleMessage.sale());
                       debitInventoryUseCase.execute(saleMessage.sale());
                       LOGGER.info(FIM_DA_SEPARACAO_DE_MERCADORIA);
                    }
                })
                .doOnError(throwable -> {
                    LOGGER.info(OPERACAO_ROLLBACK);
                    sendUpdateInventory.send(sale.get(), SaleEvent.INVENTORY_ERROR);
                })
                .onErrorResume(e -> {LOGGER.info(ERROR_MESSAGE,e); return Mono.empty();})
                .subscribe();
    }

}
