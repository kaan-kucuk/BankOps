package kaankucuk.markantbank;

import kaankucuk.markantbank.dto.AccountDto;
import kaankucuk.markantbank.dto.DepositRequest;
import kaankucuk.markantbank.dto.TransferRequest;
import kaankucuk.markantbank.dto.WithdrawRequest;

import java.math.BigDecimal;

public class TestSupport {
    public static final String ACCOUNT_API = "/api/accounts";
    public static final String TRANSACTION_API = "/api/operations";

    public static AccountDto createAccountDto(String accountNumber, BigDecimal balance) {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber(accountNumber);
        accountDto.setBalance(balance);
        return accountDto;
    }
    public static DepositRequest createDepositRequest(String accountNumber, BigDecimal amount) {
        return new DepositRequest(amount, accountNumber);
    }

    public static WithdrawRequest createWithdrawRequest(String accountNumber, BigDecimal amount) {
        return new WithdrawRequest(amount, accountNumber);
    }

    public static TransferRequest createTransferRequest(String fromAccount, String toAccount, BigDecimal amount) {
        return new TransferRequest(fromAccount, toAccount, amount);
    }
}