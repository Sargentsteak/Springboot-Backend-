package com.Bookstoreproject.beans;

import java.math.BigDecimal;

public record TransactionResponse(
        Long id,
        Long walletId,
        BigDecimal amount,
        String operationType,
        String status,
        Long userId,
        String createdAt,
        String referenceId
) {}
