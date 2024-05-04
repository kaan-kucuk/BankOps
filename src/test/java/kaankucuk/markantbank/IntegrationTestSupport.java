package kaankucuk.markantbank;

import com.fasterxml.jackson.databind.ObjectMapper;
import kaankucuk.markantbank.service.AccountService;
import kaankucuk.markantbank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class IntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected AccountService accountService;

    @MockBean
    protected TransactionService transactionService;

    @Autowired
    protected ObjectMapper objectMapper;

}
