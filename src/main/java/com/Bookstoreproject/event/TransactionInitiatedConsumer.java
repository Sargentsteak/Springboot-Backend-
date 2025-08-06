package com.Bookstoreproject.event;

import com.Bookstoreproject.entity.Wallet;
import com.Bookstoreproject.repository.jpa.WalletDao;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionInitiatedConsumer {

    private final WalletDao walletRepository;
    private final KafkaTemplate<String, WalletDebitedEvent> kafkaTemplate;

    public TransactionInitiatedConsumer(WalletDao walletRepository,
                                        KafkaTemplate<String, WalletDebitedEvent> kafkaTemplate) {
        this.walletRepository = walletRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
            topics = "transaction.initiated",
            groupId = "wallet-service-group",
            containerFactory = "transactionInitiatedKafkaListenerContainerFactory"
    )
    public void onTransactionInitiated(TransactionInitiatedEvent event) {
        Wallet wallet = walletRepository.findById(event.walletId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        boolean success = false;

        if (event.operationType().equalsIgnoreCase("DEBIT")) {
            BigDecimal newBalance = wallet.getBalance().subtract(event.amount());
            if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
                wallet.setBalance(newBalance);
                walletRepository.save(wallet);
                success = true;
            }
        } else if (event.operationType().equalsIgnoreCase("CREDIT")) {
            wallet.setBalance(wallet.getBalance().add(event.amount()));
            walletRepository.save(wallet);
            success = true;
        }

        WalletDebitedEvent responseEvent = new WalletDebitedEvent(
                event.sagaId(),
                event.walletId(),
                event.amount(),
                success ? "SUCCESS" : "FAILED"
        );

        kafkaTemplate.send("wallet.debited", event.walletId().toString(), responseEvent);
    }
}
