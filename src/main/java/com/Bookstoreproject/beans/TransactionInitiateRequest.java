package com.Bookstoreproject.beans;

import java.math.BigDecimal;

public record TransactionInitiateRequest(
        Long userId,
        Long walletId,
        BigDecimal amount,
        String operationType, // DEBIT / CREDIT
        String referenceId
) {}
