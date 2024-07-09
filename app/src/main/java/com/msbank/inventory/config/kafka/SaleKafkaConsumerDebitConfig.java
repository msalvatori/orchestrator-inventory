package com.msbank.inventory.config.kafka;

import com.msbank.inventory.adapter.output.service.inventory.kafka.SaleMessage;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.msbank.inventory.core.constants.Constants.Kafka.INVENTORY_DEBIT;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;

@Configuration
public class SaleKafkaConsumerDebitConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrap;

    @Bean(name = "receiverBeanInventoryDebit")
    public ReceiverOptions<String, SaleMessage> kafkaReceiverOptions(@Value(value = "${spring.kafka.consumer.topic}") String topic) {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(GROUP_ID_CONFIG, INVENTORY_DEBIT);
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, CustomDeserializer.class.getName());
        props.put(CustomDeserializer.CONFIG_VALUE_CLASS, SaleMessage.class.getName());
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        ReceiverOptions<String, SaleMessage> basicReceiverOptions = ReceiverOptions.create(props);
       // basicReceiverOptions.assignment(List.of(new TopicPartition("inventory-debit", 0)));
        return basicReceiverOptions.subscription(Collections.singletonList(topic));
    }
    @Bean(name = "beanInventoryDebit")
    public ReactiveKafkaConsumerTemplate<String, SaleMessage> reactiveKafkaConsumerTemplate(@Qualifier("receiverBeanInventoryDebit") ReceiverOptions<String, SaleMessage> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<String, SaleMessage>(kafkaReceiverOptions);
    }
}
