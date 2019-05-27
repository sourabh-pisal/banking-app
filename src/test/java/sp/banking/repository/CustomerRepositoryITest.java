package sp.banking.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import sp.banking.domain.Customer;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static sp.banking.util.DomainUtil.createCustomer;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryITest {

    @Autowired
    protected TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void whenPresentThenShouldFetchCustomerById() {
        Customer customer = entityManager.persistAndFlush(createCustomer());

        Optional<Customer> optionalSavedCustomer = customerRepository.findById(customer.getCustomerId());

        assertTrue(optionalSavedCustomer.isPresent());
        assertThat(optionalSavedCustomer.get(), is(customer));
    }
}