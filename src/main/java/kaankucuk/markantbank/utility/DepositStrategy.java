package kaankucuk.markantbank.utility;

import kaankucuk.markantbank.model.Account;
import kaankucuk.markantbank.model.Transaction;
import kaankucuk.markantbank.model.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class DepositStrategy implements TransactionFactory {
    @Override
    public Transaction createTransaction(Account account, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setTimestamp(Instant.now());
        return transaction;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.DEPOSIT;
    }
}