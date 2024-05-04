package kaankucuk.markantbank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    @NotBlank(message = "Sender account number cannot be blank")
    private String fromAccount;
    @NotBlank(message = "Receiver account number cannot be blank")
    private String toAccount;
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

}
