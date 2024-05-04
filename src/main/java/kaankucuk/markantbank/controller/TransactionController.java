package kaankucuk.markantbank.controller;

import jakarta.validation.Valid;
import kaankucuk.markantbank.dto.DepositRequest;
import kaankucuk.markantbank.dto.TransactionHistoryDto;
import kaankucuk.markantbank.dto.TransferRequest;
import kaankucuk.markantbank.dto.WithdrawRequest;
import kaankucuk.markantbank.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Object> depositMoney(@Valid @RequestBody DepositRequest depositRequest) {
        return ResponseEntity.ok(transactionService.depositMoney(depositRequest.getAmountOfDeposit(), depositRequest.getAccountNumber()));

    }

    @PostMapping("/withdraw")
    public ResponseEntity<Object> withdrawMoney(@Valid @RequestBody WithdrawRequest withdrawRequest) {
        return ResponseEntity.ok(transactionService.withdrawMoney(withdrawRequest.getAmountOfWithdraw(), withdrawRequest.getAccountNumber()));

    }

    @PutMapping("/transfer")
    public ResponseEntity<String> transferMoney(@Valid @RequestBody TransferRequest transferRequest) {
        transactionService.transferMoney(transferRequest.getFromAccount(), transferRequest.getToAccount(), transferRequest.getAmount());
        return ResponseEntity.ok("Transfer Completed");
    }

    @GetMapping("/transactions/{accountNumber}")
    public ResponseEntity<List<TransactionHistoryDto>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        List<TransactionHistoryDto> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
        return ResponseEntity.ok(transactions);
    }

}
