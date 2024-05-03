package kaankucuk.markantbank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawRequest {
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amountOfWithdraw;
    private String accountNumber;
}