package kaankucuk.markantbank;

import kaankucuk.markantbank.dto.AccountDto;
import java.math.BigDecimal;

public class TestSupport {
    public static final String ACCOUNT_API = "/api/accounts";
    public static final String TRANSACTION_API = "/api/operations";

    public AccountDto createAccountDto(String accountNumber, BigDecimal balance) {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber(accountNumber);
        accountDto.setBalance(balance);
        return accountDto;
    }
}