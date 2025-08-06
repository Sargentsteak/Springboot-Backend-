package com.Bookstoreproject.serviceImpl;



import com.Bookstoreproject.beans.WalletRequest;
import com.Bookstoreproject.beans.WalletResponse;
import com.Bookstoreproject.beans.WalletUpdate;
import com.Bookstoreproject.entity.Wallet;
import com.Bookstoreproject.exceptions.WalletNotFoundException;
import com.Bookstoreproject.repository.jpa.WalletDao;
import com.Bookstoreproject.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {


    private final WalletDao walletRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public WalletServiceImpl(WalletDao walletRepository,
                             RedisTemplate<String, Object> redisTemplate) {
        this.walletRepository = walletRepository;
        this.redisTemplate = redisTemplate;
    }

        @Transactional
    @Override
    public WalletResponse createWallet(WalletRequest request) {
        Wallet wallet = new Wallet();
        wallet.setUserId(request.getUserId());
        wallet.setBalance(request.getInitialBalance() != null ? request.getInitialBalance() : BigDecimal.ZERO);
        wallet.setCreatedAt(LocalDateTime.now());

        Wallet saved = walletRepository.save(wallet);
        return new WalletResponse(saved.getId(), saved.getUserId(), saved.getBalance());
    }

    @Override
    public WalletResponse getWalletById(Long walletId) {
        String redisKey = "wallet:" + walletId;

        // Try cache
        Object cached = redisTemplate.opsForValue().get(redisKey);
        if (cached instanceof WalletResponse cachedWallet) {
            return cachedWallet;
        }

        // Fallback to DB
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        WalletResponse response = new WalletResponse(wallet.getId(), wallet.getUserId(), wallet.getBalance());

        // Cache for 5 mins
        redisTemplate.opsForValue().set(redisKey, response, java.time.Duration.ofMinutes(5));

        return response;
    }

    @Transactional
    @Override
    public WalletResponse updateWalletBalance(WalletUpdate update) {
        Wallet wallet = walletRepository.findById(update.getWalletId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        BigDecimal updatedBalance;
        switch (update.getOperationType().toUpperCase()) {
            case "CREDIT" -> updatedBalance = wallet.getBalance().add(update.getAmount());
            case "DEBIT" -> {
                if (wallet.getBalance().compareTo(update.getAmount()) < 0) {
                    throw new IllegalArgumentException("Insufficient balance");
                }
                updatedBalance = wallet.getBalance().subtract(update.getAmount());
            }
            default -> throw new IllegalArgumentException("Invalid operation type");
        }

        wallet.setBalance(updatedBalance);
        Wallet saved = walletRepository.save(wallet);
        return new WalletResponse(saved.getId(), saved.getUserId(), saved.getBalance());
    }



}

