package com.msbank.inventory.adapter.output.service.inventory.kafka.producer;

import com.msbank.inventory.adapter.output.service.inventory.kafka.SaleMessage;
import com.msbank.inventory.core.domain.Sale;
import com.msbank.inventory.core.domain.enums.SaleEvent;
import com.msbank.inventory.core.output.service.inventory.dto.response.DataResponseDto;
import com.msbank.inventory.core.output.service.inventory.producer.SendUpdateInventory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendUpdateIventoryAdapter implements SendUpdateInventory {

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    private final ReactiveKafkaProducerTemplate<String, SaleMessage> reactiveKafkaProducerTemplate;

    @Override
    public Mono<DataResponseDto> send(Sale sale, SaleEvent event) {

        var saleMessage = new SaleMessage(sale, event);
        log.info("send to topic={}, saleMessage{},", topic, saleMessage);

        reactiveKafkaProducerTemplate.send(topic, saleMessage)
                .doOnSuccess(senderResult -> log.info("sent {} offset : {}",
                        sale,
                        senderResult.recordMetadata().offset()))
                .subscribe();
        return Mono.just(new DataResponseDto(sale));
    }
}
