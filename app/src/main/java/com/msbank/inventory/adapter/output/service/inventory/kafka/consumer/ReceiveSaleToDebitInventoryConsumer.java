package com.msbank.inventory.adapter.output.service.inventory.kafka.consumer;

import com.msbank.inventory.adapter.output.service.inventory.kafka.SaleMessage;
import com.msbank.inventory.core.domain.Sale;
import com.msbank.inventory.core.domain.enums.SaleEvent;
import com.msbank.inventory.core.output.service.inventory.producer.SendUpdateInventory;
import com.msbank.inventory.core.usecase.DebitInventoryUseCase;
import com.msbank.inventory.core.usecase.FindInventoryByProductIdUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static com.msbank.inventory.core.constants.Constants.Iventory.OPERACAO_R_SUCESSO;
import static com.msbank.inventory.core.constants.Constants.Iventory.ESTOQUE_INSUFICIENTE;
import static com.msbank.inventory.core.constants.Constants.Iventory.INICIO_DA_SEPARACAO_DE_MERCADORIA;
import static com.msbank.inventory.core.constants.Constants.Iventory.FIM_DA_SEPARACAO_DE_MERCADORIA;
import static com.msbank.inventory.core.constants.Constants.Iventory.OPERACAO_ROLLBACK;
import static com.msbank.inventory.core.constants.Constants.Iventory.ERROR_MESSAGE;
import static com.msbank.inventory.core.constants.Constants.Kafka.SUCCESSFULLY_CONSUMED;


@Component
public class ReceiveSaleToDebitInventoryConsumer {
    Logger LOGGER = LogManager.getLogger("Log4Core");
    @Autowired
    private DebitInventoryUseCase debitInventoryUseCase;
    @Autowired
    private FindInventoryByProductIdUseCase findInventoryByProductIdUseCase;
    @Autowired
    private  SendUpdateInventory sendUpdateInventory;
    @Autowired
    @Qualifier("${spring.kafka.consumer.bean-debit}")
    private ReactiveKafkaConsumerTemplate<String, SaleMessage> reactiveKafkaConsumerTemplate;
    private Sale sale;
    @EventListener(ApplicationStartedEvent.class)
    public void startKafkaDebitConsumer() {
        AtomicReference<Sale> sale;
        sale = new AtomicReference<>(Sale.builder().build());
        reactiveKafkaConsumerTemplate
               .receiveAutoAck()
               .map(ConsumerRecord<String, SaleMessage>::value)
               .doOnNext(saleMessage -> {
                   if(SaleEvent.CREATED_SALE.equals(saleMessage.getSaleEvent())) {
                       LOGGER.info(SUCCESSFULLY_CONSUMED, SaleMessage.class.getSimpleName(), saleMessage.toString());
                       LOGGER.info(INICIO_DA_SEPARACAO_DE_MERCADORIA);
                       sale.set(saleMessage.getSale());
                       debitInventoryUseCase.debit(saleMessage.getSale())
                               .doOnSuccess(inventory1 -> LOGGER.info(OPERACAO_R_SUCESSO,inventory1));
                       LOGGER.info(FIM_DA_SEPARACAO_DE_MERCADORIA);
                    }
                })
                .doOnError(throwable -> {
                    LOGGER.info(OPERACAO_ROLLBACK);
                    sendUpdateInventory.sendInventory(sale.get(), SaleEvent.ROLLBACK_INVENTORY);
                })
                .onErrorResume(e -> {LOGGER.info(ERROR_MESSAGE+e); return Mono.empty();})
                .subscribe();
    }

}
