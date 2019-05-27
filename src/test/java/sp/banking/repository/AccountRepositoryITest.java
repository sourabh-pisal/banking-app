package sp.banking.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import sp.banking.domain.Account;
import sp.banking.domain.Customer;

import java.util.List;

import static java.math.BigDecimal.valueOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static sp.banking.util.DomainUtil.createAccount;
import static sp.banking.util.DomainUtil.createCustomer;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryITest {

    @Autowired
    protected TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void whenAccountsPresentThenShouldReturnAccountsBasedOnIds() {
        Customer customerOne = entityManager.persistAndFlush(createCustomer());
        Customer customerTwo = entityManager.persistAndFlush(createCustomer());

        Account fromAccount = entityManager.persistAndFlush(createAccount(customerOne, valueOf(5000L)));
        Account toAccount = entityManager.persistAndFlush(createAccount(customerTwo, valueOf(4000L)));

        List<Account> accounts =
                accountRepository.getAccountsByIdsAndLockRow(fromAccount.getAccountId(), toAccount.getAccountId());

        assertThat(accounts.size(), is(2));
        assertThat(accounts.get(0).getCustomer(), is(customerOne));
        assertThat(accounts.get(1).getCustomer(), is(customerTwo));
    }

}