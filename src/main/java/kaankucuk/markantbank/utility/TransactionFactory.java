package kaankucuk.markantbank.utility;

import kaankucuk.markantbank.model.Account;
import kaankucuk.markantbank.model.Transaction;
import kaankucuk.markantbank.model.TransactionType;

import java.math.BigDecimal;

public interface TransactionFactory {
    Transaction createTransaction(Account account, BigDecimal amount);

    TransactionType getType();

}
