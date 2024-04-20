package com.msbank.inventory.adapter.output.service.inventory.kafka.consumer;

import com.msbank.inventory.adapter.output.service.inventory.kafka.SaleMessage;
import com.msbank.inventory.core.domain.enums.SaleEvent;
import com.msbank.inventory.core.usecase.CreditInventoryUseCase;
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

import static com.msbank.inventory.core.constants.Constants.Iventory.INICIO_DA_DEVOLUCAO_DA_MERCADORIA;
import static com.msbank.inventory.core.constants.Constants.Iventory.FIM_DA_DEVOLUCAO_DA_MERCADORIA;
import static com.msbank.inventory.core.constants.Constants.Iventory.OPERACAO_R_SUCESSO;
import static com.msbank.inventory.core.constants.Constants.Kafka.SUCCESSFULLY_CONSUMED;

@Component
public class ReceiveSaleToCreditInventoryConsumer {

    Logger LOGGER = LogManager.getLogger("Log4Core");
    @Autowired
    private  CreditInventoryUseCase creditInventoryUseCase;
    @Autowired
    private  FindInventoryByProductIdUseCase findInventoryByProductIdUseCase;

    @Autowired
    @Qualifier("${spring.kafka.consumer.bean-credit}")
    private  ReactiveKafkaConsumerTemplate<String, SaleMessage> reactiveKafkaConsumerTemplate;
    @EventListener(ApplicationStartedEvent.class)
    public void startKafkaCreditConsumer() {
         reactiveKafkaConsumerTemplate
                .receiveAutoAck()
                .map(ConsumerRecord<String, SaleMessage>::value)
                 .doOnNext(saleMessage -> {
                     if(SaleEvent.EXECUTE_ROLLBACK.equals(saleMessage.getSaleEvent())) {
                        LOGGER.info(INICIO_DA_DEVOLUCAO_DA_MERCADORIA);
                        LOGGER.info(SUCCESSFULLY_CONSUMED, SaleMessage.class.getSimpleName(), saleMessage);
                        findInventoryByProductIdUseCase.find(saleMessage.getSale().getProductId())
                                .doOnSuccess(inventory -> {
                                    creditInventoryUseCase.credit(inventory);

                                    LOGGER.info(OPERACAO_R_SUCESSO, inventory);
                                }).block();

                        LOGGER.info(FIM_DA_DEVOLUCAO_DA_MERCADORIA);
                    }

                })
                 .subscribe();
    }
}
