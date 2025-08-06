package com.Bookstoreproject.event;

import java.math.BigDecimal;

public record TransactionInitiatedEvent(
        String sagaId,
        Long userId,
        Long walletId,
        BigDecimal amount,
        String operationType, // CREDIT / DEBIT
        String referenceId
) {}
