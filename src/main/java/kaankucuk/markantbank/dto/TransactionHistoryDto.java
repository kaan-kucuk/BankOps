package kaankucuk.markantbank.dto;

import kaankucuk.markantbank.model.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryDto {
    private TransactionType type;
    private BigDecimal amount;
    private Date timestamp;

}