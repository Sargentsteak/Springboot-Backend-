package com.Bookstoreproject.entity;


import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;


import java.math.BigDecimal;

@Data
@Entity
@Document(indexName = "transaction_analytics")
//@Table(name = "transaction_analytics")
public class TransactionAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private String id;
    @Column(name ="tran_id")
    private Long transactionId;
    @Column(name ="wallet_id")
    private Long walletId;
    @Column(name ="user_id")
    private Long userId;
    @Column(name ="amount")
    private BigDecimal amount;
    @Column(name ="operation_type")
    private String operationType;
    @Column(name ="status")
    private String status;
    @Column(name ="reference_id")
    private String referenceId;
    @Column(name ="created_at")
    private String createdAt;
}