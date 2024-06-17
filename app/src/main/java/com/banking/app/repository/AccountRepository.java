package com.banking.app.repository;

import com.banking.app.dto.Filter;
import com.banking.app.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {

    Optional<Account> findByIdAndUserId(UUID accountId, UUID userId);

    Optional<Account> findByNumber(String accountNumber);
    Optional<Account> findByNumberAndUserId(String accountNumber, UUID userId);

    void deleteByIdAndUserId(UUID accountId, UUID userId);
}
