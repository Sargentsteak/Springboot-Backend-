package com.Bookstoreproject.event;

import java.math.BigDecimal;

public record TransactionCreatedEvent(
        Long transactionId,
        Long walletId,
        Long userId,
        BigDecimal amount,
        String operationType, // CREDIT or DEBIT
        String status,        // PENDING / SUCCESS / FAILED
        String referenceId,
        String createdAt      // ISO timestamp
) {}