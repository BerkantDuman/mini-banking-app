package com.banking.app.service;

import com.banking.app.dto.TransferRequest;
import com.banking.app.model.Transaction;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    Transaction transferMoney(TransferRequest transferRequest, Authentication authentication);

    List<Transaction> fetchAccountTransactionHistoryForUser(UUID accountId, Authentication authentication);

}
