package com.Bookstoreproject.grpc;

import com.Bookstoreproject.beans.WalletRequest;
import com.Bookstoreproject.beans.WalletResponse;
import com.Bookstoreproject.beans.WalletUpdate;
import com.Bookstoreproject.service.WalletService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import com.Bookstoreproject.proto.wallet.WalletServiceGrpc;

import java.math.BigDecimal;

@GrpcService
public class WalletGrpcServer extends WalletServiceGrpc.WalletServiceImplBase {

    private final WalletService walletService;

    public WalletGrpcServer(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public void getUserWalletBalance(com.Bookstoreproject.proto.wallet.GetWalletBalanceRequest request,
                                     StreamObserver<com.Bookstoreproject.proto.wallet.GetWalletBalanceResponse> responseObserver) {
        try {
            // Convert String userId to Long walletId
            Long walletId = Long.parseLong(request.getUserId());
            WalletResponse wallet = walletService.getWalletById(walletId);
            
            com.Bookstoreproject.proto.wallet.GetWalletBalanceResponse response = com.Bookstoreproject.proto.wallet.GetWalletBalanceResponse.newBuilder()
                    .setWalletId(String.valueOf(wallet.getWalletId()))
                    .setBalance(String.valueOf(wallet.getBalance()))
                    .setStatus("OK")
                    .build();
            responseObserver.onNext(response);
        } catch (NumberFormatException e) {
            com.Bookstoreproject.proto.wallet.GetWalletBalanceResponse errorResponse = com.Bookstoreproject.proto.wallet.GetWalletBalanceResponse.newBuilder()
                    .setStatus("ERROR")
                    .setError("Invalid wallet ID format: " + request.getUserId())
                    .build();
            responseObserver.onNext(errorResponse);
        } catch (IllegalArgumentException e) {
            com.Bookstoreproject.proto.wallet.GetWalletBalanceResponse errorResponse = com.Bookstoreproject.proto.wallet.GetWalletBalanceResponse.newBuilder()
                    .setStatus("NOT_FOUND")
                    .setError("Wallet not found: " + e.getMessage())
                    .build();
            responseObserver.onNext(errorResponse);
        } catch (Exception e) {
            com.Bookstoreproject.proto.wallet.GetWalletBalanceResponse errorResponse = com.Bookstoreproject.proto.wallet.GetWalletBalanceResponse.newBuilder()
                    .setStatus("ERROR")
                    .setError("Internal server error: " + e.getMessage())
                    .build();
            responseObserver.onNext(errorResponse);
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void createWallet(com.Bookstoreproject.proto.wallet.CreateWalletRequest request,
                           StreamObserver<com.Bookstoreproject.proto.wallet.CreateWalletResponse> responseObserver) {
        try {
            Long userId = Long.parseLong(request.getUserId());
            BigDecimal initialBalance = request.getInitialBalance().isEmpty() ? 
                BigDecimal.ZERO : new BigDecimal(request.getInitialBalance());
            
            WalletRequest walletRequest = new WalletRequest();
            walletRequest.setUserId(userId);
            walletRequest.setInitialBalance(initialBalance);
            
            WalletResponse wallet = walletService.createWallet(walletRequest);
            
            com.Bookstoreproject.proto.wallet.CreateWalletResponse response = com.Bookstoreproject.proto.wallet.CreateWalletResponse.newBuilder()
                    .setWalletId(String.valueOf(wallet.getWalletId()))
                    .setUserId(String.valueOf(wallet.getUserId()))
                    .setBalance(String.valueOf(wallet.getBalance()))
                    .setStatus("OK")
                    .build();
            responseObserver.onNext(response);
        } catch (NumberFormatException e) {
            com.Bookstoreproject.proto.wallet.CreateWalletResponse errorResponse = com.Bookstoreproject.proto.wallet.CreateWalletResponse.newBuilder()
                    .setStatus("ERROR")
                    .setError("Invalid user ID format: " + request.getUserId())
                    .build();
            responseObserver.onNext(errorResponse);
        } catch (Exception e) {
            com.Bookstoreproject.proto.wallet.CreateWalletResponse errorResponse = com.Bookstoreproject.proto.wallet.CreateWalletResponse.newBuilder()
                    .setStatus("ERROR")
                    .setError("Failed to create wallet: " + e.getMessage())
                    .build();
            responseObserver.onNext(errorResponse);
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateWalletBalance(com.Bookstoreproject.proto.wallet.UpdateWalletBalanceRequest request,
                                  StreamObserver<com.Bookstoreproject.proto.wallet.UpdateWalletBalanceResponse> responseObserver) {
        try {
            Long walletId = Long.parseLong(request.getWalletId());
            BigDecimal amount = new BigDecimal(request.getAmount());
            String operationType = request.getOperationType();
            
            WalletUpdate walletUpdate = new WalletUpdate();
            walletUpdate.setWalletId(walletId);
            walletUpdate.setAmount(amount);
            walletUpdate.setOperationType(operationType);
            
            WalletResponse wallet = walletService.updateWalletBalance(walletUpdate);
            
            com.Bookstoreproject.proto.wallet.UpdateWalletBalanceResponse response = com.Bookstoreproject.proto.wallet.UpdateWalletBalanceResponse.newBuilder()
                    .setWalletId(String.valueOf(wallet.getWalletId()))
                    .setUserId(String.valueOf(wallet.getUserId()))
                    .setBalance(String.valueOf(wallet.getBalance()))
                    .setStatus("OK")
                    .build();
            responseObserver.onNext(response);
        } catch (NumberFormatException e) {
            com.Bookstoreproject.proto.wallet.UpdateWalletBalanceResponse errorResponse = com.Bookstoreproject.proto.wallet.UpdateWalletBalanceResponse.newBuilder()
                    .setStatus("ERROR")
                    .setError("Invalid wallet ID or amount format")
                    .build();
            responseObserver.onNext(errorResponse);
        } catch (IllegalArgumentException e) {
            String status = e.getMessage().contains("Insufficient balance") ? "INSUFFICIENT_BALANCE" : "ERROR";
            com.Bookstoreproject.proto.wallet.UpdateWalletBalanceResponse errorResponse = com.Bookstoreproject.proto.wallet.UpdateWalletBalanceResponse.newBuilder()
                    .setStatus(status)
                    .setError(e.getMessage())
                    .build();
            responseObserver.onNext(errorResponse);
        } catch (Exception e) {
            com.Bookstoreproject.proto.wallet.UpdateWalletBalanceResponse errorResponse = com.Bookstoreproject.proto.wallet.UpdateWalletBalanceResponse.newBuilder()
                    .setStatus("ERROR")
                    .setError("Failed to update wallet balance: " + e.getMessage())
                    .build();
            responseObserver.onNext(errorResponse);
        } finally {
            responseObserver.onCompleted();
        }
    }
}
