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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    private TransactionServiceImpl transactionService;

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionFactory transactionFactory;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        transactionService = new TransactionServiceImpl(accountService, transactionRepository, transactionFactory, userService);
    }

    @Test
    void transferMoney() {
        // Given
        User user = new User();
        Account fromAccount = new Account();
        fromAccount.setBalance(BigDecimal.valueOf(1000));
        Account toAccount = new Account();
        toAccount.setBalance(BigDecimal.valueOf(2000));
        BigDecimal transferAmount = BigDecimal.valueOf(500);

        given(userService.getAuthenticatedUser(authentication)).willReturn(user);
        given(accountService.getAccountByNumberAndUser("1234", user)).willReturn(java.util.Optional.ofNullable(fromAccount));
        given(accountService.getAccountByNumber("5678")).willReturn(java.util.Optional.ofNullable(toAccount));

        // When
        transactionService.transferMoney(new TransferRequest("1234", "5678", transferAmount), authentication);

        // Then
        verify(accountService).updateAccount(fromAccount, fromAccount.getId());
        verify(accountService).updateAccount(toAccount, toAccount.getId());
    }

    @Test
    void getAccountTransactionHistory() {
        // Given
        User user = new User();
        UUID accountId = UUID.randomUUID();

        given(userService.getAuthenticatedUser(authentication)).willReturn(user);

        // When
        transactionService.fetchAccountTransactionHistoryForUser(accountId, authentication);

        // Then
        verify(transactionRepository).findTransactionHistoryByAccountId(accountId, user.getId());
    }


    @Test
    void transferMoneyWithInsufficientFundsShouldThrowException() {
        // Given
        User user = new User();
        Account fromAccount = new Account();
        fromAccount.setBalance(BigDecimal.valueOf(100));
        Account toAccount = new Account();
        toAccount.setBalance(BigDecimal.valueOf(2000));
        BigDecimal transferAmount = BigDecimal.valueOf(1000);

        given(userService.getAuthenticatedUser(authentication)).willReturn(user);
        given(accountService.getAccountByNumberAndUser("1234", user)).willReturn(java.util.Optional.ofNullable(fromAccount));
        given(accountService.getAccountByNumber("5678")).willReturn(java.util.Optional.ofNullable(toAccount));

        // When & Then
        assertThrows(InsufficientFundsException.class, () -> {
            transactionService.transferMoney(new TransferRequest("1234", "5678", transferAmount), authentication);
        });
    }


    @Test
    void getAccountTransactionHistoryWhenNoTransactions() {
        // Given
        User user = new User();
        UUID accountId = UUID.randomUUID();

        given(userService.getAuthenticatedUser(authentication)).willReturn(user);
        given(transactionRepository.findTransactionHistoryByAccountId(accountId, user.getId())).willReturn(new ArrayList<>());

        // When
        List<Transaction> transactions = transactionService.fetchAccountTransactionHistoryForUser(accountId, authentication);

        // Then
        assertTrue(transactions.isEmpty());
    }

}
