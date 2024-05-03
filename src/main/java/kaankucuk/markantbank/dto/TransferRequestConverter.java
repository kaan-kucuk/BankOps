package kaankucuk.markantbank.dto;

import kaankucuk.markantbank.model.Account;
import org.springframework.stereotype.Component;

@Component
public class TransferRequestConverter {

    public TransferRequestResponse convert(Account toAccount, Account fromAccount) {
        return new TransferRequestResponse(
                toAccount.getBalance(),
                fromAccount.getBalance()
        );
    }
}
