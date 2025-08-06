package com.Bookstoreproject.config;


import com.Bookstoreproject.event.TransactionCreatedEvent;
import com.Bookstoreproject.event.WalletDebitedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, TransactionCreatedEvent> transactionEventConsumerFactory() {
        JsonDeserializer<TransactionCreatedEvent> deserializer =
                new JsonDeserializer<>(TransactionCreatedEvent.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "analytics-service-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionCreatedEvent>
    transactionEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, WalletDebitedEvent> walletDebitedEventConsumerFactory() {
        JsonDeserializer<WalletDebitedEvent> deserializer =
                new JsonDeserializer<>(WalletDebitedEvent.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "transaction-service-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WalletDebitedEvent>
    walletDebitedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, WalletDebitedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(walletDebitedEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, com.Bookstoreproject.event.TransactionInitiatedEvent> transactionInitiatedConsumerFactory() {
        JsonDeserializer<com.Bookstoreproject.event.TransactionInitiatedEvent> deserializer =
                new JsonDeserializer<>(com.Bookstoreproject.event.TransactionInitiatedEvent.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "wallet-service-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean(name = "transactionInitiatedKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, com.Bookstoreproject.event.TransactionInitiatedEvent>
    transactionInitiatedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, com.Bookstoreproject.event.TransactionInitiatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionInitiatedConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, TransactionCreatedEvent> transactionCreatedEventConsumerFactory() {
        JsonDeserializer<TransactionCreatedEvent> deserializer =
                new JsonDeserializer<>(TransactionCreatedEvent.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "orchestrator-service-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionCreatedEvent>
    transactionCreatedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionCreatedEventConsumerFactory());
        return factory;
    }
}
