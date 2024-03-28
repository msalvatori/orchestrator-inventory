package com.msbank.inventory.config.kafka;


import com.msbank.inventory.adapter.output.service.inventory.kafka.SaleMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

import static com.msbank.inventory.core.constants.Constants.Kafka.INVENTORY;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

@EnableKafka
@Configuration
public class SaleKafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrap;


    @Bean
    public SenderOptions<String, SaleMessage> producerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(GROUP_ID_CONFIG, INVENTORY);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomSerializer.class.getName());
        return SenderOptions.create(props);
    }

    @Bean
    public ReactiveKafkaProducerTemplate<String, SaleMessage> reactiveKafkaProducerTemplate(
            SenderOptions<String, SaleMessage> producerProps) {
        return new ReactiveKafkaProducerTemplate<>(producerProps);
    }
}
