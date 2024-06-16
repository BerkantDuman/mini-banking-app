package com.banking.app.service;

import com.banking.app.dto.Filter;
import com.banking.app.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {

    Account createAccount(Account account);

    Optional<Account> getAccountByNumber(String accountNumber);

    Optional<Account> getAccountById(UUID accountId);
    Page<Account> getAccounts(List<Filter> accountFilters, Pageable pageable);

    Account updateAccount(Account account, UUID accountId);

    void deleteAccount(String accountId);

    Account getAccountDetails(UUID accountId, Authentication authentication);

}
