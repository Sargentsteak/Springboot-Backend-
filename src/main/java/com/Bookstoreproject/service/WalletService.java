package com.Bookstoreproject.service;
import com.Bookstoreproject.beans.WalletRequest;
import com.Bookstoreproject.beans.WalletResponse;
import com.Bookstoreproject.beans.WalletUpdate;

public interface WalletService {
    WalletResponse createWallet(WalletRequest request);
    WalletResponse getWalletById(Long walletId);
    WalletResponse updateWalletBalance(WalletUpdate update);
}
