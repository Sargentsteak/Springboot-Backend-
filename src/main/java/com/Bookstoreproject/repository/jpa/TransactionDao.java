package com.Bookstoreproject.repository.jpa;

import com.Bookstoreproject.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionDao extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletId(Long walletId);
}
