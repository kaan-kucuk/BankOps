package kaankucuk.markantbank.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    private String accountNumber;
    private BigDecimal balance;
}
