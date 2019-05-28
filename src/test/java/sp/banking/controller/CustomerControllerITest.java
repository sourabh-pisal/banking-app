package sp.banking.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import sp.banking.domain.Customer;
import sp.banking.repository.AccountRepository;
import sp.banking.repository.CustomerRepository;
import sp.banking.repository.TransactionRepository;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sp.banking.util.DomainUtil.createCustomer;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerITest {

    private static final String CUSTOMER_REQUEST = "{  \n" +
            "   \"firstName\":\"Curtis\",\n" +
            "   \"lastName\":\"Sheridan\",\n" +
            "   \"dateOfBirth\":\"2000-08-15\",\n" +
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
    public void createCustomerApiTest() throws Exception {
        mockMvc.perform(post("/customer/create")
                .content(CUSTOMER_REQUEST)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").exists());
    }

    @Test
    public void getCustomerApiTest() throws Exception {
        Customer customer = customerRepository.save(createCustomer());
        mockMvc.perform(get(format("/customer/%d/get", customer.getCustomerId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").exists());
    }
}