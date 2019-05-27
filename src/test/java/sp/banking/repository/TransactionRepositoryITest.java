package sp.banking.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import sp.banking.domain.Account;
import sp.banking.domain.Customer;
import sp.banking.domain.Transaction;

import java.util.List;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static sp.banking.domain.Transaction.TransactionType.CREDIT;
import static sp.banking.domain.Transaction.TransactionType.DEBIT;
import static sp.banking.util.DomainUtil.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionRepositoryITest {

    @Autowired
    protected TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void whenTransactionPresentThenReturnTransactionsRespectToAccountAndPageable() {
        Customer customer = entityManager.persistAndFlush(createCustomer());
        Account fromAccount = entityManager.persistAndFlush(createAccount(customer, TEN));
        Account toAccount = entityManager.persistAndFlush(createAccount(customer, ZERO));
        entityManager.persistAndFlush(createTransaction(fromAccount, toAccount, CREDIT));
        entityManager.persistAndFlush(createTransaction(fromAccount, toAccount, DEBIT));

        final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("transactionTime").descending());

        List<Transaction> transactions = transactionRepository.findAllByFromAccount(fromAccount.getAccountId(), pageRequest);

        assertThat(transactions.size(), is(2));
        assertThat(transactions.get(0).getTransactionType(), is(DEBIT));
        assertThat(transactions.get(1).getTransactionType(), is(CREDIT));
    }
}