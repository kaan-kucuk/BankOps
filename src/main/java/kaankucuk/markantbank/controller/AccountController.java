package kaankucuk.markantbank.controller;

import kaankucuk.markantbank.dto.AccountDto;
import kaankucuk.markantbank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {

        this.accountService = accountService;

    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccountsDto());
    }

    @GetMapping("/getBalance/{accountNumber}")
    public ResponseEntity<AccountDto> findAccountDtoByAccountNumber(@PathVariable String accountNumber) {
        AccountDto result = accountService.findBalanceByAccountNumber(accountNumber);
        return ResponseEntity.ok(result);
    }

}

