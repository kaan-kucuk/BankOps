package kaankucuk.markantbank.service;

import jakarta.transaction.Transactional;
import kaankucuk.markantbank.dto.*;
import kaankucuk.markantbank.exception.InsufficientFundsException;
import kaankucuk.markantbank.model.Account;
import kaankucuk.markantbank.model.Transaction;
import kaankucuk.markantbank.model.TransactionType;
import kaankucuk.markantbank.repository.TransactionRepository;
import kaankucuk.markantbank.utility.TransactionFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final DepositRequestConverter depositRequestConverter;
    private final WithdrawRequestConverter withdrawRequestConverter;
    private final TransferRequestConverter transferRequestConverter;
    private final TransactionHistoryDtoConverter transactionHistoryDtoConverter;
    private final AccountService accountService;
    private final Map<TransactionType, TransactionFactory> transactionStrategies;
    private final TransactionRepository transactionRepository;


    public TransactionService(DepositRequestConverter depositRequestConverter, AccountService accountService, WithdrawRequestConverter withdrawRequestConverter, TransferRequestConverter transferRequestConverter, TransactionHistoryDtoConverter transactionHistoryDtoConverter, List<TransactionFactory> strategies, TransactionRepository transactionRepository) {
        this.depositRequestConverter = depositRequestConverter;
        this.accountService = accountService;
        this.withdrawRequestConverter = withdrawRequestConverter;
        this.transferRequestConverter = transferRequestConverter;
        this.transactionHistoryDtoConverter = transactionHistoryDtoConverter;
        this.transactionStrategies = strategies.stream().collect(Collectors.toMap(TransactionFactory::getType, s -> s));
        this.transactionRepository = transactionRepository;
    }

    public DepositRequestResponse depositMoney(BigDecimal amountOfDeposit, String accountNumber) {
        Account account = accountService.findOrCreateAccount(accountNumber);
        account.setBalance(account.getBalance().add(amountOfDeposit));
        Transaction transaction = transactionStrategies.get(TransactionType.DEPOSIT).createTransaction(account, amountOfDeposit);
        account.getTransactions().add(transaction);
        accountService.saveAccount(account);
        return depositRequestConverter.convert(account);
    }


    public WithdrawRequestResponse withdrawMoney(BigDecimal amountOfWithdraw, String accountNumber) {
        Account account = accountService.findOrThrowException(accountNumber, amountOfWithdraw);
        account.setBalance(account.getBalance().subtract(amountOfWithdraw));
        Transaction transaction = transactionStrategies.get(TransactionType.WITHDRAW).createTransaction(account, amountOfWithdraw);
        account.getTransactions().add(transaction);
        accountService.saveAccount(account);
        return withdrawRequestConverter.convert(account);
    }

    @Transactional
    public TransferRequestResponse transferMoney(String fromAccount, String toAccount, BigDecimal amountOfMoney) {
        Account fromAccounts = accountService.findAccount(fromAccount);
        Account toAccounts = accountService.findAccount(toAccount);
        if (fromAccounts.getBalance().compareTo(amountOfMoney) < 0) {
            throw new InsufficientFundsException("Insufficient funds, money is bigger than current balance");
        } else {
            fromAccounts.setBalance(fromAccounts.getBalance().subtract(amountOfMoney));
            toAccounts.setBalance(toAccounts.getBalance().add(amountOfMoney));
        }
        Transaction sendTransaction = transactionStrategies.get(TransactionType.TRANSFER).createTransaction(fromAccounts, amountOfMoney.negate());
        fromAccounts.getTransactions().add(sendTransaction);

        Transaction recieveTransaction = transactionStrategies.get(TransactionType.TRANSFER).createTransaction(toAccounts, amountOfMoney);
        toAccounts.getTransactions().add(recieveTransaction);

        accountService.saveAccount(fromAccounts);
        accountService.saveAccount(toAccounts);
        return transferRequestConverter.convert(fromAccounts, toAccounts);
    }

    public List<TransactionHistoryDto> getTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions = transactionRepository.findByAccount_AccountNumber(accountNumber);
        return transactions.stream().map(transactionHistoryDtoConverter::convert).collect(Collectors.toList());
    }


}
