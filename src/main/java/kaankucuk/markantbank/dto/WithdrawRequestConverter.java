package kaankucuk.markantbank.dto;

import kaankucuk.markantbank.model.Account;
import kaankucuk.markantbank.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class WithdrawRequestConverter {

    public WithdrawRequestResponse convert(Account account) {
        return new WithdrawRequestResponse(
                account.getBalance(),
                account.getAccountNumber()
        );
    }
}
