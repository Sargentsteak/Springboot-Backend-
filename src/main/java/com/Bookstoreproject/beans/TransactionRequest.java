package com.Bookstoreproject.beans;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public record TransactionRequest(
        Long walletId,
        BigDecimal amount,
        Long userId,
        String operationType,
        String referenceId
) {}
