package com.Bookstoreproject.beans;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class WalletResponse {
    private Long walletId;
    private Long userId;
    private BigDecimal balance;

    public WalletResponse() {
        // No-args constructor needed for Redis/Jackson deserialization
    }

    public WalletResponse(Long walletId, Long userId, BigDecimal balance) {
        this.walletId = walletId;
        this.userId = userId;
        this.balance = balance;
    }
}
