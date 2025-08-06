package com.Bookstoreproject.event;

import com.Bookstoreproject.entity.Transaction;
import com.Bookstoreproject.repository.jpa.TransactionDao;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Component
public class WalletDebitedConsumer {

    private final TransactionDao transactionRepository;
    private final KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate;

    public WalletDebitedConsumer(TransactionDao transactionRepository,
                                 KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate) {
        this.transactionRepository = transactionRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
            topics = "wallet.debited",
            groupId = "transaction-service-group",
            containerFactory = "walletDebitedKafkaListenerContainerFactory"
    )
    public void onWalletDebited(WalletDebitedEvent event) {
        if (!event.status().equals("SUCCESS")) {
            return; // terminate saga on failure
        }

        // Create & persist transaction
        Transaction txn = new Transaction();
        txn.setUserId(0L); // Optional: track userId if required
        txn.setWalletId(event.walletId());
        txn.setAmount(event.amount());
        txn.setOperationType("DEBIT");
        txn.setStatus("SUCCESS");
        txn.setReferenceId(UUID.randomUUID().toString());
        txn.setCreatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime());

        Transaction saved = transactionRepository.save(txn);

        // Emit transaction.created
        TransactionCreatedEvent createdEvent = new TransactionCreatedEvent(
                saved.getId(),
                saved.getWalletId(),
                saved.getUserId(),
                saved.getAmount(),
                saved.getOperationType(),
                saved.getStatus(),
                saved.getReferenceId(),
                saved.getCreatedAt().toString()
        );

        kafkaTemplate.send("transaction.created", saved.getId().toString(), createdEvent);
    }
}
