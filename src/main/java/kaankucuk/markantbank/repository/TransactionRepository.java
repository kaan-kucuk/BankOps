package kaankucuk.markantbank.repository;

import kaankucuk.markantbank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount_AccountNumber(String accountNumber);
}
