package kaankucuk.markantbank.controller;

import kaankucuk.markantbank.IntegrationTestSupport;
import kaankucuk.markantbank.TestSupport;
import kaankucuk.markantbank.dto.DepositRequest;
import kaankucuk.markantbank.dto.TransferRequest;
import kaankucuk.markantbank.dto.WithdrawRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static kaankucuk.markantbank.TestSupport.TRANSACTION_API;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionControllerIntegrationTest extends IntegrationTestSupport {

    @Test
    public void depositMoney_ShouldReturnOK() throws Exception {
        DepositRequest depositRequest = TestSupport.createDepositRequest("123456", new BigDecimal("100.00"));
        String jsonRequest = objectMapper.writeValueAsString(depositRequest);

        mockMvc.perform(post(TRANSACTION_API + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

    }
    @Test
    public void depositMoney_ShouldThrowMethodArgumentNotValid_WhenAmountIsTooSmall() throws Exception {
        DepositRequest depositRequest = TestSupport.createDepositRequest("123456", new BigDecimal("0.00"));
        String jsonRequest = objectMapper.writeValueAsString(depositRequest);

        mockMvc.perform(post(TRANSACTION_API + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.amountOfDeposit").value("Amount must be greater than zero"));
    }

    @Test
    public void depositMoney_ShouldThrowMethodArgumentNotValid_WhenAccountNumberIsBlank() throws Exception {
        DepositRequest depositRequest = TestSupport.createDepositRequest("", new BigDecimal("100.00"));
        String jsonRequest = objectMapper.writeValueAsString(depositRequest);

        mockMvc.perform(post(TRANSACTION_API + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.accountNumber").value("Account number cannot be blank"));
    }

    @Test
    public void withdrawMoney_ShouldReturnOK() throws Exception {
        WithdrawRequest withdrawRequest = TestSupport.createWithdrawRequest("123456", new BigDecimal("100.00"));
        String jsonRequest = objectMapper.writeValueAsString(withdrawRequest);

        mockMvc.perform(post(TRANSACTION_API + "/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }


    @Test
    public void withdrawMoney_ShouldThrowMethodArgumentNotValid_WhenAccountNumberIsBlank() throws Exception {
        WithdrawRequest withdrawRequest = TestSupport.createWithdrawRequest("", new BigDecimal("100.00"));
        String jsonRequest = objectMapper.writeValueAsString(withdrawRequest);

        mockMvc.perform(post(TRANSACTION_API + "/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.accountNumber").value("Account number cannot be blank"));
    }

    @Test
    public void withdrawMoney_ShouldThrowMethodArgumentNotValid_WhenAmountIsTooSmall() throws Exception {
        WithdrawRequest withdrawRequest = TestSupport.createWithdrawRequest("123456", new BigDecimal("0.00"));
        String jsonRequest = objectMapper.writeValueAsString(withdrawRequest);

        mockMvc.perform(post(TRANSACTION_API + "/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.amountOfWithdraw").value("Amount must be greater than zero"));
    }

    @Test
    public void transferMoney_ShouldReturnOK() throws Exception {
        TransferRequest transferRequest = TestSupport.createTransferRequest("123456", "654321", new BigDecimal("100.00"));
        String jsonRequest = objectMapper.writeValueAsString(transferRequest);

        mockMvc.perform(put(TRANSACTION_API + "/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void transferMoney_ShouldThrowMethodArgumentNotValid_WhenToAccountIsBlank() throws Exception {
        TransferRequest transferRequest =TestSupport.createTransferRequest("123456", "", new BigDecimal("100.00"));
        String jsonRequest = objectMapper.writeValueAsString(transferRequest);

        mockMvc.perform(put(TRANSACTION_API + "/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.toAccount").value("Receiver account number cannot be blank"));
    }

    @Test
    public void transferMoney_ShouldThrowMethodArgumentNotValid_WhenFromAccountIsBlank() throws Exception {
        TransferRequest transferRequest = TestSupport.createTransferRequest("", "654321", new BigDecimal("100.00"));
        String jsonRequest = objectMapper.writeValueAsString(transferRequest);

        mockMvc.perform(put(TRANSACTION_API + "/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fromAccount").value("Sender account number cannot be blank"));
    }

    @Test
    public void transferMoney_ShouldThrowMethodArgumentNotValid_WhenAmountIsTooSmall() throws Exception {
        TransferRequest transferRequest = TestSupport.createTransferRequest("123456", "654321", new BigDecimal("0.00"));
        String jsonRequest = objectMapper.writeValueAsString(transferRequest);

        mockMvc.perform(put(TRANSACTION_API + "/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.amount").value("Amount must be greater than zero"));
    }


}