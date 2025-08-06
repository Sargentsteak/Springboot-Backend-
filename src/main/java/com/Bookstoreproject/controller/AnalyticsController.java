package com.Bookstoreproject.controller;

import com.Bookstoreproject.beans.TransactionAnalyticsRecord;
import com.Bookstoreproject.repository.elasticSearch.TransactionAnalyticsElasticDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final TransactionAnalyticsElasticDao analyticsRepository;

    public AnalyticsController(TransactionAnalyticsElasticDao analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    private TransactionAnalyticsRecord toRecord(TransactionAnalyticsRecord doc) {
        return new TransactionAnalyticsRecord(
                doc.transactionId(),
                doc.walletId(),
                doc.id(),
                doc.amount(),
                doc.operationType(),
                doc.status(),
                doc.referenceId(),
                doc.createdAt()
        );
    }

    @GetMapping("/wallet/{walletId}")
    public List<TransactionAnalyticsRecord> getByWallet(@PathVariable Long walletId) {
        return analyticsRepository.findByWalletId(walletId)
                .stream()
                .map(doc -> new TransactionAnalyticsRecord(
                        doc.getTransactionId(),
                        doc.getWalletId(),
                        doc.getUserId(),
                        doc.getAmount(),
                        doc.getOperationType(),
                        doc.getStatus(),
                        doc.getReferenceId(),
                        doc.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{userId}")
    public List<TransactionAnalyticsRecord> getByUser(@PathVariable Long userId) {
        return analyticsRepository.findByUserId(userId)
                .stream()
                .map(doc -> new TransactionAnalyticsRecord(
                        doc.getTransactionId(),
                        doc.getWalletId(),
                        doc.getUserId(),
                        doc.getAmount(),
                        doc.getOperationType(),
                        doc.getStatus(),
                        doc.getReferenceId(),
                        doc.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }



}