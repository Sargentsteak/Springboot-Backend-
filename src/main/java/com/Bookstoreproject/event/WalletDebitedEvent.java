package com.Bookstoreproject.event;

import java.math.BigDecimal;

public record WalletDebitedEvent(
        String sagaId,
        Long walletId,
        BigDecimal amount,
        String status // SUCCESS / FAILED
) {}
