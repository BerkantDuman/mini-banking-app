package com.banking.app.service;

import com.banking.app.dto.TransferRequest;
import com.banking.app.enums.TransactionStatus;
import com.banking.app.exception.AccountNotFoundException;
import com.banking.app.exception.InsufficientFundsException;
import com.banking.app.factory.TransactionFactory;
import com.banking.app.model.Account;
import com.banking.app.model.Transaction;
import com.banking.app.model.User;
import com.banking.app.repository.TransactionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final TransactionFactory transactionFactory;
    private final UserService userService;

    @Override
    @Transactional(noRollbackFor = InsufficientFundsException.class, isolation = Isolation.SERIALIZABLE)
    public Transaction transferMoney(TransferRequest transferRequest) {

        Account fromAccount = accountService.getAccountByNumber(transferRequest.getFromAccountNumber())
                .orElseThrow(() ->
                        new AccountNotFoundException("Sender account not found: " + transferRequest.getFromAccountNumber() ));

        Account toAccount = accountService.getAccountByNumber(transferRequest.getToAccountNumber())
                .orElseThrow(() ->
                        new AccountNotFoundException("Target account not found: " + transferRequest.getToAccountNumber()));


        if (insufficientFunds(fromAccount, transferRequest.getAmount())) {
            transactionFactory.createTransactionAndSave(fromAccount, toAccount, transferRequest.getAmount(), TransactionStatus.FAILED);
            throw new InsufficientFundsException("There aren't enough funds in the sender's account");
        }


        performTransfer(fromAccount, toAccount, transferRequest.getAmount());

        return transactionFactory.createTransactionAndSave(fromAccount, toAccount, transferRequest.getAmount(), TransactionStatus.SUCCESS);
    }

    @Override
    public List<Transaction> getAccountTransactionHistory(UUID accountId, Authentication authentication) {
        User user = userService.getAuthenticatedUser(authentication);
        return this.transactionRepository.findTransactionHistoryByAccountId(accountId, user.getId());
    }

    private void performTransfer(Account fromAccount, Account toAccount, BigDecimal amount) {
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountService.updateAccount(fromAccount, fromAccount.getId());
        accountService.updateAccount(toAccount, toAccount.getId());
    }

    private boolean insufficientFunds(Account fromAccount, BigDecimal amount) {
        return fromAccount.getBalance().compareTo(amount) < 0;
    }
}