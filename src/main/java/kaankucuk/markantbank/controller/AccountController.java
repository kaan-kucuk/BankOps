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

    @GetMapping("/getBalance")
    public ResponseEntity<AccountDto> findAccountDtoByAccountNumber(@RequestBody AccountDto accountDto) {
        AccountDto result = accountService.findBalanceByAccountNumber(accountDto.getAccountNumber());
        return ResponseEntity.ok(result);
    }

}

