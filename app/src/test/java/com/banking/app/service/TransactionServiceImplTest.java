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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

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

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private UUID uuid = UUID.randomUUID();

    private TransferRequest transferRequest = new TransferRequest(
            "from-123'",
            "to-123",
            BigDecimal.valueOf(10));

    @Test
    void shouldTransferMoneySuccessfully() {
        Account fromAccount = new Account();
        fromAccount.setBalance(BigDecimal.valueOf(1000));
        Account toAccount = new Account();
        toAccount.setBalance(BigDecimal.valueOf(500));
        BigDecimal amount = BigDecimal.valueOf(100);

        Transaction transaction = new Transaction();
        transaction.setStatus(TransactionStatus.SUCCESS);

        when(accountService.getAccountById(any(UUID.class))).thenReturn(Optional.of(fromAccount), Optional.of(toAccount));
        when(transactionFactory.createTransactionAndSave(any(Account.class), any(Account.class), any(BigDecimal.class), any(TransactionStatus.class))).thenReturn(transaction);

        Transaction result = transactionService.transferMoney(transferRequest);

        assertEquals(transaction.getStatus(), result.getStatus());
        verify(transactionFactory).createTransactionAndSave(any(Account.class), any(Account.class), any(BigDecimal.class), any(TransactionStatus.class));
    }

    @Test
    void shouldGetAccountTransactionHistorySuccessfully() {
        User user = new User();
        user.setUsername("testUser");
        user.setId(UUID.randomUUID());

        List<Transaction> transactionList = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setStatus(TransactionStatus.SUCCESS);
        transactionList.add(transaction);

        when(userService.getAuthenticatedUser(any())).thenReturn(user);
        when(transactionRepository.findTransactionHistoryByAccountId(any(UUID.class), any(UUID.class))).thenReturn(transactionList);

        List<Transaction> result = transactionService.getAccountTransactionHistory(uuid, authentication);

        assertEquals(1, result.size());
        verify(transactionRepository).findTransactionHistoryByAccountId(any(UUID.class), any(UUID.class));
        verify(userService).getAuthenticatedUser(any());
    }

    @Test
    void shouldThrowAccountNotFoundException_whenFromAccountDoesNotExist() {
        BigDecimal amount = BigDecimal.valueOf(100);

        when(accountService.getAccountById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transactionService.transferMoney(transferRequest));
    }

    @Test
    void shouldThrowAccountNotFoundException_whenToAccountDoesNotExist() {
        Account fromAccount = new Account();
        fromAccount.setBalance(BigDecimal.valueOf(1000));

        BigDecimal amount = BigDecimal.valueOf(100);

        when(accountService.getAccountById(any(UUID.class))).thenReturn(Optional.of(fromAccount), Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transactionService.transferMoney(transferRequest));
    }

    @Test
    void shouldThrowInsufficientFundsException_whenNotEnoughFunds() {
        Account fromAccount = new Account();
        fromAccount.setBalance(BigDecimal.valueOf(50));
        Account toAccount = new Account();
        toAccount.setBalance(BigDecimal.valueOf(500));
        BigDecimal amount = BigDecimal.valueOf(100);

        when(accountService.getAccountById(any(UUID.class))).thenReturn(Optional.of(fromAccount), Optional.of(toAccount));

        assertThrows(InsufficientFundsException.class, () -> transactionService.transferMoney(transferRequest));
    }

    @Test
    void shouldThrowException_whenTransactionHistoryNotFound() {
        User user = new User();
        user.setUsername("testUser");
        user.setId(UUID.randomUUID());

        when(userService.getAuthenticatedUser(any())).thenReturn(user);
        when(transactionRepository.findTransactionHistoryByAccountId(any(UUID.class), any(UUID.class))).thenReturn(Collections.emptyList());

        List<Transaction> result = transactionService.getAccountTransactionHistory(uuid, authentication);

        assertEquals(result.size(), 0);
    }
}
