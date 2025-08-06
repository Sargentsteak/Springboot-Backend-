package com.Bookstoreproject.beans;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class WalletUpdate {
    private Long walletId;
    private BigDecimal amount;
    private String operationType; // CREDIT or DEBIT
}
