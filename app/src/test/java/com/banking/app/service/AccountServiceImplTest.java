package com.banking.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.banking.app.exception.AccountNotFoundException;
import com.banking.app.model.Account;
import com.banking.app.model.User;
import com.banking.app.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private AccountServiceImpl accountService;
    private UUID uuid = UUID.randomUUID();
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("testUser")
                .id(UUID.fromString("3f24b505-b004-454f-be23-317370d2ccf7"))
                .build();
        Account account = Account.builder()
                .id(uuid)
                .name("testAccount")
                .user(user)
                .build();
        Account updatedAccount = Account.builder()
                .id(uuid)
                .name("updatedTestAccount")
                .build();
        lenient().when(accountRepository.findById(uuid)).thenReturn(Optional.of(account));
        lenient().when(accountRepository.save(updatedAccount)).thenReturn(updatedAccount);
    }

    @Test
    void shouldCreateAccountSuccessfully() {
        Account account = new Account();
        accountService.createAccount(account);
        verify(accountRepository).save(account);
    }

    @Test
    void shouldGetAccountByIdSuccessfully() {
        Account result = accountService.getAccountById(uuid).orElse(null);
        assertNotNull(result);
        assertEquals("testAccount", result.getName());
    }

    @Test
    void shouldUpdateAccountSuccessfully() {
        Account updatedAccount = new Account();
        updatedAccount.setId(uuid);
        updatedAccount.setName("Updated Account");
        updatedAccount.setBalance(new BigDecimal(2000));

        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        Account result = accountService.updateAccount(updatedAccount, uuid);

        assertEquals("Updated Account", result.getName());
        assertEquals(0, new BigDecimal(2000).compareTo(result.getBalance()));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldDeleteAccountSuccessfully() {
        accountService.deleteAccount(uuid.toString());
        verify(accountRepository).deleteById(uuid);
    }

    @Test
    void shouldThrowAccountNotFoundExceptionWhenAccountNonExistent() {
        UUID invalidUuid = UUID.randomUUID();
        assertThrows(AccountNotFoundException.class,
                () -> accountService.updateAccount(new Account(), invalidUuid));
    }
}
