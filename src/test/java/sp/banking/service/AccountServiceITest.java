package sp.banking.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import sp.banking.domain.Account;
import sp.banking.domain.Customer;
import sp.banking.domain.Transaction;
import sp.banking.repository.AccountRepository;
import sp.banking.repository.CustomerRepository;
import sp.banking.repository.TransactionRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Long.MAX_VALUE;
import static java.math.BigDecimal.*;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;
import static sp.banking.util.DomainUtil.createAccount;
import static sp.banking.util.DomainUtil.createCustomer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceITest {

    private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10, Sort.by("transactionTime").descending());
    private static final int NUMBER_OF_THREADS = 4;

    private final Customer fromCustomer = createCustomer();
    private final Customer toCustomer = createCustomer();
    private final Account fromAccount = createAccount(fromCustomer, valueOf(5000L));
    private final Account toAccount = createAccount(toCustomer, valueOf(1000L));
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        customerRepository.saveAll(asList(fromCustomer, toCustomer));
        accountRepository.saveAll(asList(fromAccount, toAccount));
    }

    @Test
    public void whenAccountsExitsAndValidThenShouldTransferBalanceBetweenAccounts() {
        BigDecimal amount = valueOf(3000L);
        accountService.transferFunds(fromAccount.getAccountId(), toAccount.getAccountId(), amount);

        Account updatedFromAccount = accountRepository.findById(fromAccount.getAccountId()).orElseGet(Account::new);
        Account updatedToAccount = accountRepository.findById(toAccount.getAccountId()).orElseGet(Account::new);

        BigDecimal expectedFromBalance = fromAccount.getCurrentBalance().subtract(amount);
        assertThat(updatedFromAccount.getCurrentBalance(), closeTo(expectedFromBalance, ZERO));

        BigDecimal expectedToBalance = toAccount.getCurrentBalance().add(amount);
        assertThat(updatedToAccount.getCurrentBalance(), closeTo(expectedToBalance, ZERO));

        List<Transaction> transactionsFromAccount = transactionRepository.findAllByFromAccount(fromAccount.getAccountId(), PAGE_REQUEST);
        assertThat(transactionsFromAccount.size(), is(1));
    }

    @Test
    public void whenAccountExitsAndValidThenShouldTransferBalanceUsingPessimisticWrite() throws InterruptedException {
        BigDecimal amount = valueOf(100L);
        BigDecimal finalAmountExcepted = amount.multiply(valueOf(4L));

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            executor.execute(() ->
                    accountService.transferFunds(fromAccount.getAccountId(), toAccount.getAccountId(), amount));
        }
        executor.shutdown();
        executor.awaitTermination(1, MINUTES);

        Account updatedFromAccount = accountRepository.findById(fromAccount.getAccountId()).orElseGet(Account::new);
        Account updatedToAccount = accountRepository.findById(toAccount.getAccountId()).orElseGet(Account::new);

        BigDecimal expectedFromBalance = fromAccount.getCurrentBalance().subtract(finalAmountExcepted);
        assertThat(updatedFromAccount.getCurrentBalance(), closeTo(expectedFromBalance, ZERO));

        BigDecimal expectedToBalance = toAccount.getCurrentBalance().add(finalAmountExcepted);
        assertThat(updatedToAccount.getCurrentBalance(), closeTo(expectedToBalance, ZERO));

        List<Transaction> transactionsFromAccount = transactionRepository.findAllByFromAccount(fromAccount.getAccountId(), PAGE_REQUEST);
        assertThat(transactionsFromAccount.size(), is(4));
    }

    @Test
    public void givenInvalidToOrFromAccountThenShouldThrowEntityNotFoundExceptionWithProperMsg() {
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("Account Id Not Found");
        accountService.transferFunds(fromAccount.getAccountId(), MAX_VALUE, ONE);
    }

    @Test
    public void givenInvalidBalanceWhenTransferFundsThenShouldThrowIllegalArgumentExceptionWithProperMsg() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Balance Amount Should Be Greater Than Zero");

        accountService.transferFunds(fromAccount.getAccountId(), toAccount.getAccountId(), ZERO);
    }

    @Test
    public void givenInactiveAccountWhenTransferFundsThenShouldThrowIllegalArgumentExceptionWithProperMsg() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Account Not Active");

        fromAccount.setActive(false);
        accountRepository.save(fromAccount);

        accountService.transferFunds(fromAccount.getAccountId(), toAccount.getAccountId(), ONE);
    }

    @Test
    public void givenInactiveCustomerWhenTransferFundsThenShouldThrowThrowIllegalArgumentExceptionWithProperMsg() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Customer Is Not Active");

        fromCustomer.setActive(false);
        customerRepository.save(fromCustomer);

        accountService.transferFunds(fromAccount.getAccountId(), toAccount.getAccountId(), ONE);
    }

    @Test
    public void givenAccountWhenValidThenShouldCreateAccount() {
        Account account = createAccount(fromCustomer, ONE);

        Account savedAccount = accountService.createAccount(account);

        assertThat(savedAccount.getCustomer(), is(account.getCustomer()));
        assertThat(savedAccount.getCurrentBalance(), closeTo(account.getCurrentBalance(), ZERO));
    }
}