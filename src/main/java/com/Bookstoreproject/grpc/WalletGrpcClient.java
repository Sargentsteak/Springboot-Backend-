package com.Bookstoreproject.grpc;

import com.Bookstoreproject.proto.wallet.GetWalletBalanceRequest;
import com.Bookstoreproject.proto.wallet.CreateWalletRequest;
import com.Bookstoreproject.proto.wallet.UpdateWalletBalanceRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WalletGrpcClient {

    @GrpcClient("wallet-service")
    private com.Bookstoreproject.proto.wallet.WalletServiceGrpc.WalletServiceBlockingStub blockingStub;

    public String fetchBalance(String userId) {
        com.Bookstoreproject.proto.wallet.GetWalletBalanceRequest req = GetWalletBalanceRequest.newBuilder()
                .setUserId(userId)
                .build();
        com.Bookstoreproject.proto.wallet.GetWalletBalanceResponse resp = blockingStub.getUserWalletBalance(req);
        if (resp.getStatus().equals("OK")) {
            return resp.getBalance();
        }
        throw new RuntimeException("gRPC wallet error: " + resp.getStatus() + " - " + resp.getError());
    }

    public com.Bookstoreproject.proto.wallet.CreateWalletResponse createWallet(String userId, BigDecimal initialBalance) {
        CreateWalletRequest req = CreateWalletRequest.newBuilder()
                .setUserId(userId)
                .setInitialBalance(initialBalance.toString())
                .build();
        return blockingStub.createWallet(req);
    }

    public com.Bookstoreproject.proto.wallet.UpdateWalletBalanceResponse updateWalletBalance(String walletId, BigDecimal amount, String operationType) {
        UpdateWalletBalanceRequest req = UpdateWalletBalanceRequest.newBuilder()
                .setWalletId(walletId)
                .setAmount(amount.toString())
                .setOperationType(operationType)
                .build();
        return blockingStub.updateWalletBalance(req);
    }

    public String creditWallet(String walletId, BigDecimal amount) {
        com.Bookstoreproject.proto.wallet.UpdateWalletBalanceResponse resp = updateWalletBalance(walletId, amount, "CREDIT");
        if (resp.getStatus().equals("OK")) {
            return resp.getBalance();
        }
        throw new RuntimeException("gRPC wallet credit error: " + resp.getStatus() + " - " + resp.getError());
    }

    public String debitWallet(String walletId, BigDecimal amount) {
        com.Bookstoreproject.proto.wallet.UpdateWalletBalanceResponse resp = updateWalletBalance(walletId, amount, "DEBIT");
        if (resp.getStatus().equals("OK")) {
            return resp.getBalance();
        }
        throw new RuntimeException("gRPC wallet debit error: " + resp.getStatus() + " - " + resp.getError());
    }
}

