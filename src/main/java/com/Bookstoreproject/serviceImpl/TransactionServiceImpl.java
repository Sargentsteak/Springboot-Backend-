package com.Bookstoreproject.serviceImpl;

import com.Bookstoreproject.beans.TransactionRequest;
import com.Bookstoreproject.beans.TransactionResponse;
import com.Bookstoreproject.entity.Transaction;
import com.Bookstoreproject.event.TransactionCreatedEvent;
import com.Bookstoreproject.repository.jpa.TransactionDao;
import com.Bookstoreproject.service.TransactionService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionRepository;
    private final KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate;

    public TransactionServiceImpl(TransactionDao transactionRepository,
                                  KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate) {
        this.transactionRepository = transactionRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        Transaction txn = new Transaction();
        txn.setWalletId(request.walletId());
        txn.setId(request.userId());
        txn.setAmount(request.amount());
        txn.setOperationType(request.operationType().toUpperCase());
        txn.setStatus("SUCCESS"); // Assume success for now
        txn.setCreatedAt(LocalDateTime.now());
        txn.setReferenceId(request.referenceId());

        Transaction saved = transactionRepository.save(txn);

        TransactionCreatedEvent event = new TransactionCreatedEvent(
                saved.getId(),
                saved.getWalletId(),
                request.userId(), // must be passed from request
                saved.getAmount(),
                saved.getOperationType(),
                saved.getStatus(),
                saved.getReferenceId(),
                saved.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)
        );

        kafkaTemplate.send("transaction.created", saved.getWalletId().toString(), event);

        return new TransactionResponse(
                saved.getId(),
                saved.getWalletId(),
                saved.getAmount(),
                saved.getOperationType(),
                saved.getStatus(),
                saved.getUserId(),
                saved.getCreatedAt().toString(),
                saved.getReferenceId()
        );
    }

    @Override
    public TransactionResponse getTransactionById(Long id) {
        Transaction txn = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        return new TransactionResponse(
                txn.getId(),
                txn.getWalletId(),
                txn.getAmount(),
                txn.getOperationType(),
                txn.getStatus(),
                txn.getUserId(),
                txn.getCreatedAt().toString(),
                txn.getReferenceId()
        );
    }

    @Override
    public List<TransactionResponse> getAllTransactionsForWallet(Long walletId) {
        return transactionRepository.findByWalletId(walletId)
                .stream()
                .map(txn -> new TransactionResponse (
                        txn.getId(),
                        txn.getWalletId(),
                        txn.getAmount(),
                        txn.getOperationType(),
                        txn.getStatus(),
                        txn.getUserId(),
                        txn.getCreatedAt().toString(),
                        txn.getReferenceId()
                ))
                .collect(Collectors.toList());
    }
}
