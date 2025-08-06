package com.Bookstoreproject.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionCreatedConsumer {

    private static final Logger logger = LogManager.getLogger(TransactionCreatedConsumer.class);

    @KafkaListener(
            topics = "transaction.created",
            groupId = "orchestrator-service-group",
            containerFactory = "transactionCreatedKafkaListenerContainerFactory"
    )
    public void onTransactionCreated(TransactionCreatedEvent event) {
        logger.info("Saga COMPLETED for Transaction ID: {} | Wallet: {} | Amount: {}",
                event.transactionId(), event.walletId(), event.amount());
    }
}