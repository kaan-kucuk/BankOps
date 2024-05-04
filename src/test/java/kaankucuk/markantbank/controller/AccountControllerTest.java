package kaankucuk.markantbank.controller;

import kaankucuk.markantbank.IntegrationTestSupport;
import kaankucuk.markantbank.dto.AccountDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static kaankucuk.markantbank.TestSupport.ACCOUNT_API;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerIntegrationTest extends IntegrationTestSupport {

    @Test
    public void getAllAccounts_ShouldReturnAccounts() throws Exception {
        mockMvc.perform(get(ACCOUNT_API + "/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void findAccountDtoByAccountNumber_ShouldReturnAccountDto() throws Exception {
        AccountDto accountDto = new AccountDto("123456", new BigDecimal("1000"));
        when(accountService.findBalanceByAccountNumber("123456")).thenReturn(accountDto);

        mockMvc.perform(get(ACCOUNT_API + "/getBalance/" + accountDto.getAccountNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}