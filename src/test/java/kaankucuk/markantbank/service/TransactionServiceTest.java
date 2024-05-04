package kaankucuk.markantbank.service;

import kaankucuk.markantbank.dto.*;
import kaankucuk.markantbank.exception.AccountNotFoundException;
import kaankucuk.markantbank.exception.GeneralExceptionAdvisor;
import kaankucuk.markantbank.exception.InsufficientFundsException;
import kaankucuk.markantbank.model.Account;
import kaankucuk.markantbank.model.Transaction;
import kaankucuk.markantbank.model.TransactionType;
import kaankucuk.markantbank.repository.TransactionRepository;
import kaankucuk.markantbank.utility.TransactionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private TransactionService transactionService;
    @Mock
    private DepositRequestConverter depositRequestConverter;
    @Mock
    private WithdrawRequestConverter withdrawRequestConverter;
    @Mock
    private TransferRequestConverter transferRequestConverter;
    @Mock
    private TransactionHistoryDtoConverter transactionHistoryDtoConverter;
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionFactory depositTransactionFactory;
    @Mock
    private TransactionFactory withdrawTransactionFactory;
    @Mock
    private TransactionFactory transferTransactionFactory;
    private Map<TransactionType, TransactionFactory> transactionStrategies;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionStrategies = new HashMap<>();
        setupTransactionFactory(depositTransactionFactory, TransactionType.DEPOSIT);
        setupTransactionFactory(withdrawTransactionFactory, TransactionType.WITHDRAW);
        setupTransactionFactory(transferTransactionFactory, TransactionType.TRANSFER);


        transactionStrategies.put(TransactionType.DEPOSIT, depositTransactionFactory);
        transactionStrategies.put(TransactionType.WITHDRAW, withdrawTransactionFactory);
        transactionStrategies.put(TransactionType.TRANSFER, transferTransactionFactory);


        transactionService = new TransactionService(
                depositRequestConverter,
                accountService,
                withdrawRequestConverter,
                transferRequestConverter,
                transactionHistoryDtoConverter,
                new ArrayList<>(transactionStrategies.values()),
                transactionRepository);
    }

    private void setupTransactionFactory(TransactionFactory factory, TransactionType type) {
        when(factory.getType()).thenReturn(type);
        when(factory.createTransaction(any(Account.class), any(BigDecimal.class))).thenAnswer(
                invocation -> {
                    Account account = invocation.getArgument(0);
                    BigDecimal amount = invocation.getArgument(1);
                    return new Transaction(account, amount, type);
                }
        );
    }

    @Test
    public void depositMoney_SuccessfulDeposit_IncreasesBalanceAndStoresTransaction() {
        BigDecimal depositAmount = new BigDecimal("100");
        String accountNumber = "123456";
        Account mockAccount = new Account();
        mockAccount.setBalance(new BigDecimal("200"));
        Transaction mockTransaction = new Transaction();

        when(accountService.findOrCreateAccount(accountNumber)).thenReturn(mockAccount);
        when(depositTransactionFactory.createTransaction(any(Account.class), any(BigDecimal.class))).thenReturn(mockTransaction);
        when(depositRequestConverter.convert(mockAccount)).thenReturn(new DepositRequestResponse());

        DepositRequestResponse response = transactionService.depositMoney(depositAmount, accountNumber);

        assertEquals(new BigDecimal("300"), mockAccount.getBalance());
        assertTrue(mockAccount.getTransactions().contains(mockTransaction));
        verify(accountService).saveAccount(mockAccount);
        assertNotNull(response);
    }

    @Test
    public void depositMoney_AccountDoesNotExist_CreatesNewAccount() {
        BigDecimal depositAmount = new BigDecimal("100");
        String accountNumber = "newAccount";
        Account newMockAccount = new Account();
        newMockAccount.setBalance(BigDecimal.ZERO);
        Transaction mockTransaction = new Transaction();

        when(accountService.findOrCreateAccount(accountNumber)).thenReturn(newMockAccount);
        when(depositTransactionFactory.createTransaction(any(Account.class), any(BigDecimal.class))).thenReturn(mockTransaction);
        when(depositRequestConverter.convert(newMockAccount)).thenReturn(new DepositRequestResponse());

        DepositRequestResponse response = transactionService.depositMoney(depositAmount, accountNumber);

        assertEquals(new BigDecimal("100"), newMockAccount.getBalance());
        assertTrue(newMockAccount.getTransactions().contains(mockTransaction));
        verify(accountService).saveAccount(newMockAccount);
        assertNotNull(response);
    }

    @Test
    public void withdrawMoney_SuccessfulWithdrawal_DecreasesBalanceAndStoresTransaction() {
        BigDecimal withdrawalAmount = new BigDecimal("50");
        String accountNumber = "123456";
        Account mockAccount = new Account();
        mockAccount.setAccountNumber(accountNumber);
        mockAccount.setBalance(new BigDecimal("100"));

        when(accountService.findOrThrowException(accountNumber, withdrawalAmount)).thenReturn(mockAccount);
        Transaction mockTransaction = new Transaction(mockAccount, withdrawalAmount.negate(), TransactionType.WITHDRAW);
        when(withdrawTransactionFactory.createTransaction(any(Account.class), eq(withdrawalAmount))).thenReturn(mockTransaction);

        WithdrawRequestResponse expectedResponse = new WithdrawRequestResponse(mockAccount.getBalance(), accountNumber);
        when(withdrawRequestConverter.convert(mockAccount)).thenReturn(expectedResponse);

        WithdrawRequestResponse actualResponse = transactionService.withdrawMoney(withdrawalAmount, accountNumber);

        assertEquals(new BigDecimal("50"), mockAccount.getBalance());
        verify(transactionRepository).save(mockTransaction);
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void withdrawMoney_AccountNotFound_ThrowsException() {
        BigDecimal withdrawalAmount = new BigDecimal("50");
        String accountNumber = "unknown";

        when(accountService.findOrThrowException(accountNumber, withdrawalAmount))
                .thenThrow(new AccountNotFoundException("Account not found"));

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.withdrawMoney(withdrawalAmount, accountNumber);
        });
    }

    @Test
    public void transferMoney_SenderBalanceDecreasesCorrectly() {
        BigDecimal transferAmount = new BigDecimal("100");
        String fromAccountNumber = "123456";
        String toAccountNumber = "654321";
        Account fromAccount = new Account();
        Account toAccount = new Account();

        fromAccount.setBalance(new BigDecimal("500"));
        toAccount.setBalance(new BigDecimal("300"));

        when(accountService.findAccount(fromAccountNumber)).thenReturn(fromAccount);
        when(accountService.findAccount(toAccountNumber)).thenReturn(toAccount);
        when(transferTransactionFactory.createTransaction(eq(fromAccount), eq(transferAmount.negate())))
                .thenReturn(new Transaction(fromAccount, transferAmount.negate(), TransactionType.TRANSFER));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transferRequestConverter.convert(any(Account.class), any(Account.class)))
                .thenReturn(new TransferRequestResponse());

        // When
        transactionService.transferMoney(fromAccountNumber, toAccountNumber, transferAmount);

        // Then
        assertEquals(new BigDecimal("400"), fromAccount.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }
    @Test
    public void transferMoney_ReceiverBalanceIncreasesCorrectly() {
        // Given
        BigDecimal transferAmount = new BigDecimal("100");
        String fromAccountNumber = "123456";
        String toAccountNumber = "654321";
        Account fromAccount = new Account();
        Account toAccount = new Account();

        fromAccount.setBalance(new BigDecimal("500"));
        toAccount.setBalance(new BigDecimal("300"));

        when(accountService.findAccount(fromAccountNumber)).thenReturn(fromAccount);
        when(accountService.findAccount(toAccountNumber)).thenReturn(toAccount);
        when(transferTransactionFactory.createTransaction(eq(fromAccount), eq(transferAmount.negate())))
                .thenReturn(new Transaction(fromAccount, transferAmount.negate(), TransactionType.TRANSFER));
        when(transferTransactionFactory.createTransaction(eq(toAccount), eq(transferAmount)))
                .thenReturn(new Transaction(toAccount, transferAmount, TransactionType.TRANSFER));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transferRequestConverter.convert(any(Account.class), any(Account.class)))
                .thenReturn(new TransferRequestResponse());

        // When
        transactionService.transferMoney(fromAccountNumber, toAccountNumber, transferAmount);

        // Then
        assertEquals(new BigDecimal("400"), toAccount.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }
    @Test
    public void transferMoney_InsufficientFunds_ThrowsException() {
        // Given
        BigDecimal transferAmount = new BigDecimal("600");
        String fromAccountNumber = "123456";
        Account fromAccount = new Account();

        fromAccount.setBalance(new BigDecimal("500"));

        when(accountService.findAccount(fromAccountNumber)).thenReturn(fromAccount);

        // When & Then
        assertThrows(InsufficientFundsException.class, () -> {
            transactionService.transferMoney(fromAccountNumber, "654321", transferAmount);
        });
    }

    }




