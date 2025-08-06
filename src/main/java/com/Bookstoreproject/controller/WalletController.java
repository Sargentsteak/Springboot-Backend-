package com.Bookstoreproject.controller;




import com.Bookstoreproject.beans.WalletRequest;
import com.Bookstoreproject.beans.WalletResponse;
import com.Bookstoreproject.beans.WalletUpdate;
import com.Bookstoreproject.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/create")
    public ResponseEntity<WalletResponse> createWallet(@RequestBody WalletRequest requestDao) {
        return ResponseEntity.ok(walletService.createWallet(requestDao));
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable Long walletId) {
        return ResponseEntity.ok(walletService.getWalletById(walletId));
    }

    @PostMapping("/updateBalance")
    public ResponseEntity<WalletResponse> updateBalance(@RequestBody WalletUpdate updateDao) {
        return ResponseEntity.ok(walletService.updateWalletBalance(updateDao));
    }
}

