package com.banking.app.controller;

import com.banking.app.dto.TransferRequest;
import com.banking.app.model.Transaction;
import com.banking.app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferMoney(@RequestBody TransferRequest request, Authentication authentication) {
        return
                ResponseEntity.ok().body(
                        transactionService.transferMoney(request, authentication)
                );
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable UUID accountId, Authentication authentication) {
        return ResponseEntity.ok(transactionService.fetchAccountTransactionHistoryForUser(accountId, authentication));
    }
}