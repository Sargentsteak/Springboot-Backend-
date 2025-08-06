package com.Bookstoreproject.beans;

import java.math.BigDecimal;


public record TransactionAnalyticsRecord(
        Long transactionId,
        Long id,
        Long walletId,
        BigDecimal amount,
        String operationType,
        String status,
        String referenceId,
        String createdAt
) {}
