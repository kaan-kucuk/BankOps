package kaankucuk.markantbank.service;

import kaankucuk.markantbank.dto.AccountDto;
import kaankucuk.markantbank.dto.AccountDtoConverter;
import kaankucuk.markantbank.exception.AccountNotFoundException;
import kaankucuk.markantbank.exception.InsufficientFundsException;
import kaankucuk.markantbank.model.Account;
import kaankucuk.markantbank.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountDtoConverter accountDtoConverter;


    public AccountService(AccountRepository accountRepository, AccountDtoConverter accountDtoConverter) {
        this.accountRepository = accountRepository;
        this.accountDtoConverter = accountDtoConverter;

    }

    public List<AccountDto> getAllAccountsDto() {
        List<Account> accountList = accountRepository.findAll();

        return accountList.stream().map(accountDtoConverter::convert).collect(Collectors.toList());
    }

    public AccountDto findBalanceByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account with accountNumber: " + accountNumber + " not found"));
        return accountDtoConverter.convert(account);
    }

    public Account findOrCreateAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseGet(() -> {
            Account newAccount = new Account();
            newAccount.setAccountNumber(accountNumber);
            newAccount.setBalance(BigDecimal.ZERO);
            newAccount.setTransactions(new ArrayList<>());
            accountRepository.save(newAccount);
            return newAccount;
        });
    }


    public Account findAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        return account;
    }

    public void saveAccount(Account account) {

        accountRepository.save(account);
    }


}