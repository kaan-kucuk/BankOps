package kaankucuk.markantbank.dto;

import kaankucuk.markantbank.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoConverter {

    public AccountDto convert(Account account){
        return AccountDto.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }
}
