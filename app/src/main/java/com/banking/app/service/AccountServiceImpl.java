package com.banking.app.service;

import com.banking.app.dto.Filter;
import com.banking.app.exception.AccountNotFoundException;
import com.banking.app.exception.InsufficientFundsException;
import com.banking.app.model.Account;
import com.banking.app.model.User;
import com.banking.app.repository.AccountRepository;
import com.banking.app.specification.AccountSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Account createAccount(Account account) {
        account.setCreatedAt(LocalDateTime.now());
        return this.accountRepository.save(account);
    }

    @Override
    public Optional<Account> getAccountByNumber(String accountNumber) {
        return this.accountRepository.findByNumber(accountNumber);
    }

    @Override
    public Optional<Account> getAccountByNumberAndUser(String accountNumber, User user) {
        return this.accountRepository.findByNumberAndUserId(accountNumber, user.getId());
    }

    @Override
    public Optional<Account> getAccountById(UUID accountId) {
        return this.accountRepository.findById(accountId);
    }

    @Override
    public Page<Account> getAccounts(List<Filter> accountFilters, Authentication authentication, Pageable pageable) {
        User user = userService.getAuthenticatedUser(authentication);
        return this.accountRepository.findAll(AccountSpecification.columnEqual(accountFilters, user), pageable);
    }

    @Override
    @Transactional
    public Account updateAccount(Account account, UUID accountId) {

        Account previousAccount = accountRepository.findById(
                accountId).orElseThrow(() ->
                new AccountNotFoundException("Given account could not be found" + accountId));

        previousAccount.setBalance(account.getBalance());
        previousAccount.setNumber(account.getNumber());
        previousAccount.setName(account.getName());
        previousAccount.setUpdatedAt(LocalDateTime.now());
        previousAccount.setUser(account.getUser());

        return this.accountRepository.save(previousAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(String accountId) {
        this.accountRepository.deleteById(UUID.fromString(accountId));
    }

    @Override
    public Account getAccountDetails(UUID accountId, Authentication authentication) {
        User user = userService.getAuthenticatedUser(authentication);
        return this.accountRepository.findByIdAndUserId(accountId, user.getId()).orElseThrow(() ->
                new AccountNotFoundException("Given account could not be found for the authenticated user"));
    }
}
