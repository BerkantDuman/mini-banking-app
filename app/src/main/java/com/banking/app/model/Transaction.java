package com.banking.app.model;

import com.banking.app.enums.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    @NotNull
    private Account from;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    @NotNull
    private Account to;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionStatus status;

}
