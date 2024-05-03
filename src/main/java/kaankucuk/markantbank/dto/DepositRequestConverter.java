package kaankucuk.markantbank.dto;

import kaankucuk.markantbank.model.Account;
import org.springframework.stereotype.Component;

@Component
public class DepositRequestConverter {
    public DepositRequestResponse convert(Account account) {
        return new DepositRequestResponse(
                account.getBalance(),
                account.getAccountNumber()
        );
    }
}
