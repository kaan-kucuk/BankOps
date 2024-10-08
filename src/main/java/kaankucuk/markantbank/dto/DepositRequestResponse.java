package kaankucuk.markantbank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequestResponse {
    private BigDecimal newBalanceOfAccount;
    private String accountNumber;

}
