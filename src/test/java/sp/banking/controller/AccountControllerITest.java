package sp.banking.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import sp.banking.domain.Account;
import sp.banking.domain.Customer;
import sp.banking.repository.AccountRepository;
import sp.banking.repository.CustomerRepository;
import sp.banking.repository.TransactionRepository;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sp.banking.domain.Transaction.TransactionType.DEBIT;
import static sp.banking.util.DomainUtil.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerITest {

    private static final String ACCOUNT_REQUEST = "{  \n" +
            "   \"customer\":{  \n" +
            "      \"customerId\":%d\n" +
            "   },\n" +
            "   \"currentBalance\":\"5000\",\n" +
            "   \"active\":true\n" +
            "}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }


    @Test
    public void createAccountApiTest() throws Exception {
        Customer customer = customerRepository.save(createCustomer());
        mockMvc.perform(post("/account/create")
                .content(format(ACCOUNT_REQUEST, customer.getCustomerId()))
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").exists());
    }

    @Test
    public void transferApiTest() throws Exception {
        Account fromAccount = accountRepository.save(createAccount(customerRepository.save(createCustomer()), valueOf(5000L)));
        Account toAccount = accountRepository.save(createAccount(customerRepository.save(createCustomer()), valueOf(1000L)));

        String uri = format("/account/transfer?from-account-id=%d&to-account-id=%d&balance=1000", fromAccount.getAccountId(), toAccount.getAccountId());
        mockMvc.perform(put(uri))
                .andDo(print())
                .andExpect(status().isOk());

        Account account = accountRepository.findById(fromAccount.getAccountId()).orElseThrow(EntityNotFoundException::new);
        assertThat(account.getCurrentBalance(), closeTo(valueOf(4000), ZERO));
    }

    @Ignore
    @Test
    public void getTransactionApiTest() throws Exception {
        Account fromAccount = accountRepository.save(createAccount(customerRepository.save(createCustomer()), valueOf(5000L)));
        Account toAccount = accountRepository.save(createAccount(customerRepository.save(createCustomer()), valueOf(1000L)));
        transactionRepository.save(createTransaction(fromAccount, toAccount, DEBIT));

        mockMvc.perform(get("/account/1/get-transaction-details?page-number=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionDtos").exists());
    }
}