package kaankucuk.markantbank.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequest {
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amountOfDeposit;
    @NotBlank(message = "Account number cannot be blank")
    private String accountNumber;
}
