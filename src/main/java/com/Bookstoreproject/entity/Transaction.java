package com.Bookstoreproject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long walletId;

    @Column
    private Long userId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String operationType; // CREDIT / DEBIT

    @Column(nullable = false)
    private String status; // PENDING / SUCCESS / FAILED

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private String referenceId; // optional external ref: order ID, correlation ID
}
