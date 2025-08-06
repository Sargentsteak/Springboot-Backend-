package com.Bookstoreproject.controller;

import com.Bookstoreproject.grpc.WalletGrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/grpc/wallets")
public class GrpcWalletController {

    private final WalletGrpcClient walletGrpcClient;

    public GrpcWalletController(WalletGrpcClient walletGrpcClient) {
        this.walletGrpcClient = walletGrpcClient;
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<Map<String, Object>> getWalletBalance(@PathVariable String userId) {
        try {
            String balance = walletGrpcClient.fetchBalance(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("balance", balance);
            response.put("status", "OK");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            response.put("status", "ERROR");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createWallet(@RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            BigDecimal initialBalance = request.containsKey("initialBalance") ? 
                new BigDecimal(request.get("initialBalance")) : BigDecimal.ZERO;
            
            var response = walletGrpcClient.createWallet(userId, initialBalance);
            
            Map<String, Object> result = new HashMap<>();
            result.put("walletId", response.getWalletId());
            result.put("userId", response.getUserId());
            result.put("balance", response.getBalance());
            result.put("status", response.getStatus());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            response.put("status", "ERROR");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/credit")
    public ResponseEntity<Map<String, Object>> creditWallet(@RequestBody Map<String, String> request) {
        try {
            String walletId = request.get("walletId");
            BigDecimal amount = new BigDecimal(request.get("amount"));
            
            String newBalance = walletGrpcClient.creditWallet(walletId, amount);
            
            Map<String, Object> result = new HashMap<>();
            result.put("walletId", walletId);
            result.put("newBalance", newBalance);
            result.put("creditedAmount", amount);
            result.put("status", "OK");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            response.put("status", "ERROR");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/debit")
    public ResponseEntity<Map<String, Object>> debitWallet(@RequestBody Map<String, String> request) {
        try {
            String walletId = request.get("walletId");
            BigDecimal amount = new BigDecimal(request.get("amount"));
            
            String newBalance = walletGrpcClient.debitWallet(walletId, amount);
            
            Map<String, Object> result = new HashMap<>();
            result.put("walletId", walletId);
            result.put("newBalance", newBalance);
            result.put("debitedAmount", amount);
            result.put("status", "OK");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            response.put("status", "ERROR");
            return ResponseEntity.badRequest().body(response);
        }
    }
} 