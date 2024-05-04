package kaankucuk.markantbank.service;

import kaankucuk.markantbank.dto.AccountDto;
import kaankucuk.markantbank.dto.AccountDtoConverter;
import kaankucuk.markantbank.exception.AccountNotFoundException;
import kaankucuk.markantbank.model.Account;
import kaankucuk.markantbank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountDtoConverter accountDtoConverter;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountService(accountRepository, accountDtoConverter);
    }

    @Test
    public void getAllAccountsDto_ReturnsAllAccounts() {
        List<Account> accounts = Arrays.asList(new Account(), new Account());
        when(accountRepository.findAll()).thenReturn(accounts);
        when(accountDtoConverter.convert(any(Account.class))).thenReturn(new AccountDto());

        List<AccountDto> result = accountService.getAllAccountsDto();

        assertEquals(2, result.size());
        verify(accountRepository).findAll();
        verify(accountDtoConverter, times(2)).convert(any(Account.class));
    }

    @Test
    public void findBalanceByAccountNumber_ReturnsCorrectAccountDto() {
        String accountNumber = "123456";
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(accountDtoConverter.convert(account)).thenReturn(new AccountDto());

        AccountDto result = accountService.findBalanceByAccountNumber(accountNumber);

        assertNotNull(result);
        verify(accountRepository).findByAccountNumber(accountNumber);
        verify(accountDtoConverter).convert(account);
    }

    @Test
    public void findBalanceByAccountNumber_ThrowsAccountNotFoundException() {
        String accountNumber = "unknown";
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.findBalanceByAccountNumber(accountNumber));
    }

    @Test
    public void findOrCreateAccount_CreatesNewAccountWhenNotFound() {
        String accountNumber = "newAccount";
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account result = accountService.findOrCreateAccount(accountNumber);

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getBalance());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void findOrCreateAccount_FindsExistingAccount() {
        String accountNumber = "existingAccount";
        Account existingAccount = new Account();
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(existingAccount));

        Account result = accountService.findOrCreateAccount(accountNumber);

        assertSame(existingAccount, result);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void saveAccount_SavesAccountSuccessfully() {
        Account account = new Account();

        accountService.saveAccount(account);

        verify(accountRepository).save(account);
    }
}
