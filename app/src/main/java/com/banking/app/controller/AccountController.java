package com.banking.app.controller;

import com.banking.app.dto.Filter;
import com.banking.app.model.Account;
import com.banking.app.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity createAccount(@RequestBody Account account) {
        return ResponseEntity.ok().body(this.accountService.createAccount(account));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Account>> searchAccounts(@RequestBody List<Filter> accountFilters, Pageable pageable, Authentication authentication) {
        return ResponseEntity.ok().body(this.accountService.getAccounts(accountFilters, authentication, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAccount(@PathVariable UUID id,
                                        @RequestBody Account account) {
        return ResponseEntity.ok().body(this.accountService.updateAccount(account, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity getAccountDetails(Authentication authentication,
                                            @PathVariable UUID id) {
        return ResponseEntity.ok().body(this.accountService.getAccountDetails(id, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAccount(@PathVariable String id) {
        this.accountService.deleteAccount(id);
        return ResponseEntity.ok().build();

    }


}