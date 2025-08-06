package com.Bookstoreproject.service;

import com.Bookstoreproject.beans.TransactionRequest;
import com.Bookstoreproject.beans.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest request);
    TransactionResponse getTransactionById(Long id);
    List<TransactionResponse> getAllTransactionsForWallet(Long walletId);
}
