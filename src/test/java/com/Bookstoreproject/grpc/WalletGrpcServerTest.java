package com.Bookstoreproject.grpc;

import com.Bookstoreproject.beans.WalletRequest;
import com.Bookstoreproject.beans.WalletResponse;
import com.Bookstoreproject.beans.WalletUpdate;
import com.Bookstoreproject.service.WalletService;
import com.Bookstoreproject.proto.wallet.*;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletGrpcServerTest {

    @Mock
    private WalletService walletService;

    @Mock
    private StreamObserver<GetWalletBalanceResponse> balanceResponseObserver;

    @Mock
    private StreamObserver<CreateWalletResponse> createResponseObserver;

    @Mock
    private StreamObserver<UpdateWalletBalanceResponse> updateResponseObserver;

    private WalletGrpcServer walletGrpcServer;

    @BeforeEach
    void setUp() {
        walletGrpcServer = new WalletGrpcServer(walletService);
    }

    @Test
    void testGetUserWalletBalance_Success() {
        // Given
        GetWalletBalanceRequest request = GetWalletBalanceRequest.newBuilder()
                .setUserId("123")
                .build();

        WalletResponse mockWallet = new WalletResponse(123L, 456L, new BigDecimal("100.50"));
        when(walletService.getWalletById(123L)).thenReturn(mockWallet);

        // When
        walletGrpcServer.getUserWalletBalance(request, balanceResponseObserver);

        // Then
        verify(balanceResponseObserver).onNext(argThat(response ->
                response.getStatus().equals("OK") &&
                response.getWalletId().equals("123") &&
                response.getBalance().equals("100.50")
        ));
        verify(balanceResponseObserver).onCompleted();
        verify(balanceResponseObserver, never()).onError(any());
    }

    @Test
    void testGetUserWalletBalance_InvalidUserId() {
        // Given
        GetWalletBalanceRequest request = GetWalletBalanceRequest.newBuilder()
                .setUserId("invalid")
                .build();

        // When
        walletGrpcServer.getUserWalletBalance(request, balanceResponseObserver);

        // Then
        verify(balanceResponseObserver).onNext(argThat(response ->
                response.getStatus().equals("ERROR") &&
                response.getError().contains("Invalid wallet ID format")
        ));
        verify(balanceResponseObserver).onCompleted();
        verify(balanceResponseObserver, never()).onError(any());
    }

    @Test
    void testGetUserWalletBalance_WalletNotFound() {
        // Given
        GetWalletBalanceRequest request = GetWalletBalanceRequest.newBuilder()
                .setUserId("123")
                .build();

        when(walletService.getWalletById(123L)).thenThrow(new IllegalArgumentException("Wallet not found"));

        // When
        walletGrpcServer.getUserWalletBalance(request, balanceResponseObserver);

        // Then
        verify(balanceResponseObserver).onNext(argThat(response ->
                response.getStatus().equals("NOT_FOUND") &&
                response.getError().contains("Wallet not found")
        ));
        verify(balanceResponseObserver).onCompleted();
        verify(balanceResponseObserver, never()).onError(any());
    }

    @Test
    void testCreateWallet_Success() {
        // Given
        CreateWalletRequest request = CreateWalletRequest.newBuilder()
                .setUserId("456")
                .setInitialBalance("50.00")
                .build();

        WalletResponse mockWallet = new WalletResponse(789L, 456L, new BigDecimal("50.00"));
        when(walletService.createWallet(any(WalletRequest.class))).thenReturn(mockWallet);

        // When
        walletGrpcServer.createWallet(request, createResponseObserver);

        // Then
        verify(walletService).createWallet(argThat(walletRequest ->
                walletRequest.getUserId().equals(456L) &&
                walletRequest.getInitialBalance().equals(new BigDecimal("50.00"))
        ));
        verify(createResponseObserver).onNext(argThat(response ->
                response.getStatus().equals("OK") &&
                response.getWalletId().equals("789") &&
                response.getUserId().equals("456") &&
                response.getBalance().equals("50.00")
        ));
        verify(createResponseObserver).onCompleted();
        verify(createResponseObserver, never()).onError(any());
    }

    @Test
    void testCreateWallet_EmptyInitialBalance() {
        // Given
        CreateWalletRequest request = CreateWalletRequest.newBuilder()
                .setUserId("456")
                .setInitialBalance("")
                .build();

        WalletResponse mockWallet = new WalletResponse(789L, 456L, BigDecimal.ZERO);
        when(walletService.createWallet(any(WalletRequest.class))).thenReturn(mockWallet);

        // When
        walletGrpcServer.createWallet(request, createResponseObserver);

        // Then
        verify(walletService).createWallet(argThat(walletRequest ->
                walletRequest.getUserId().equals(456L) &&
                walletRequest.getInitialBalance().equals(BigDecimal.ZERO)
        ));
        verify(createResponseObserver).onNext(argThat(response ->
                response.getStatus().equals("OK") &&
                response.getBalance().equals("0")
        ));
        verify(createResponseObserver).onCompleted();
    }

    @Test
    void testUpdateWalletBalance_CreditSuccess() {
        // Given
        UpdateWalletBalanceRequest request = UpdateWalletBalanceRequest.newBuilder()
                .setWalletId("123")
                .setAmount("25.00")
                .setOperationType("CREDIT")
                .build();

        WalletResponse mockWallet = new WalletResponse(123L, 456L, new BigDecimal("125.50"));
        when(walletService.updateWalletBalance(any(WalletUpdate.class))).thenReturn(mockWallet);

        // When
        walletGrpcServer.updateWalletBalance(request, updateResponseObserver);

        // Then
        verify(walletService).updateWalletBalance(argThat(walletUpdate ->
                walletUpdate.getWalletId().equals(123L) &&
                walletUpdate.getAmount().equals(new BigDecimal("25.00")) &&
                walletUpdate.getOperationType().equals("CREDIT")
        ));
        verify(updateResponseObserver).onNext(argThat(response ->
                response.getStatus().equals("OK") &&
                response.getWalletId().equals("123") &&
                response.getBalance().equals("125.50")
        ));
        verify(updateResponseObserver).onCompleted();
    }

    @Test
    void testUpdateWalletBalance_DebitSuccess() {
        // Given
        UpdateWalletBalanceRequest request = UpdateWalletBalanceRequest.newBuilder()
                .setWalletId("123")
                .setAmount("25.00")
                .setOperationType("DEBIT")
                .build();

        WalletResponse mockWallet = new WalletResponse(123L, 456L, new BigDecimal("75.50"));
        when(walletService.updateWalletBalance(any(WalletUpdate.class))).thenReturn(mockWallet);

        // When
        walletGrpcServer.updateWalletBalance(request, updateResponseObserver);

        // Then
        verify(walletService).updateWalletBalance(argThat(walletUpdate ->
                walletUpdate.getWalletId().equals(123L) &&
                walletUpdate.getAmount().equals(new BigDecimal("25.00")) &&
                walletUpdate.getOperationType().equals("DEBIT")
        ));
        verify(updateResponseObserver).onNext(argThat(response ->
                response.getStatus().equals("OK") &&
                response.getBalance().equals("75.50")
        ));
        verify(updateResponseObserver).onCompleted();
    }

    @Test
    void testUpdateWalletBalance_InsufficientBalance() {
        // Given
        UpdateWalletBalanceRequest request = UpdateWalletBalanceRequest.newBuilder()
                .setWalletId("123")
                .setAmount("200.00")
                .setOperationType("DEBIT")
                .build();

        when(walletService.updateWalletBalance(any(WalletUpdate.class)))
                .thenThrow(new IllegalArgumentException("Insufficient balance"));

        // When
        walletGrpcServer.updateWalletBalance(request, updateResponseObserver);

        // Then
        verify(updateResponseObserver).onNext(argThat(response ->
                response.getStatus().equals("INSUFFICIENT_BALANCE") &&
                response.getError().contains("Insufficient balance")
        ));
        verify(updateResponseObserver).onCompleted();
    }

    @Test
    void testUpdateWalletBalance_InvalidAmount() {
        // Given
        UpdateWalletBalanceRequest request = UpdateWalletBalanceRequest.newBuilder()
                .setWalletId("123")
                .setAmount("invalid")
                .setOperationType("CREDIT")
                .build();

        // When
        walletGrpcServer.updateWalletBalance(request, updateResponseObserver);

        // Then
        verify(updateResponseObserver).onNext(argThat(response ->
                response.getStatus().equals("ERROR") &&
                response.getError().contains("Invalid wallet ID or amount format")
        ));
        verify(updateResponseObserver).onCompleted();
    }
} 