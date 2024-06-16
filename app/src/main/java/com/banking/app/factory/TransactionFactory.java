package com.banking.app.factory;

import com.banking.app.enums.TransactionStatus;
import com.banking.app.model.Account;
import com.banking.app.model.Transaction;
import com.banking.app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionFactory {
    private final TransactionRepository transactionRepository;

    @Transactional
    public Transaction createTransactionAndSave(Account fromAccount,
                                                Account toAccount,
                                                BigDecimal amount,
                                                TransactionStatus status) {
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .from(fromAccount)
                .to(toAccount)
                .transactionDate(LocalDateTime.now())
                .status(status)
                .build();

        return transactionRepository.save(transaction);
    }
}