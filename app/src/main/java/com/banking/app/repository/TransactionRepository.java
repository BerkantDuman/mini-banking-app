package com.banking.app.repository;

import com.banking.app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT t FROM transactions t "
            +"WHERE (t.from.id = :accountId AND t.from.user.id = :userId) "
            +"OR (t.to.id = :accountId AND t.to.user.id = :userId) "
            +"ORDER BY t.transactionDate DESC")
    List<Transaction> findTransactionHistoryByAccountId(@Param("accountId") UUID accountId, @Param("userId") UUID userId);
}
