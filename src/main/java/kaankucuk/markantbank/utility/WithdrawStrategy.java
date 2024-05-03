package kaankucuk.markantbank.utility;

import kaankucuk.markantbank.model.Account;
import kaankucuk.markantbank.model.Transaction;
import kaankucuk.markantbank.model.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class WithdrawStrategy implements TransactionFactory {
    @Override
    public Transaction createTransaction(Account account, BigDecimal amount) {
        // Withdraw specific logic
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setAmount(amount.negate());
        transaction.setTimestamp(Instant.now());
        return transaction;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.WITHDRAW;
    }
}
