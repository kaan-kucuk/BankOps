package kaankucuk.markantbank.dto;

import kaankucuk.markantbank.model.Account;
import kaankucuk.markantbank.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class TransactionHistoryDtoConverter {
    public TransactionHistoryDto convert(Transaction transaction) {
        return new TransactionHistoryDto(transaction.getType(), transaction.getAmount(), Date.from(transaction.getTimestamp()));
    }
}