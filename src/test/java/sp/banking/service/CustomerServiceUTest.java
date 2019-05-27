package sp.banking.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sp.banking.domain.Customer;
import sp.banking.repository.CustomerRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

import static java.lang.Long.MAX_VALUE;
import static java.lang.String.format;
import static java.util.Optional.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static sp.banking.util.DomainUtil.createCustomer;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceUTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerService customerService;

    @Test
    public void givenValidCustomerWhenSavedShouldPersistsIntoDatabase() {
        Customer customerToBeSaved = createCustomer();
        when(customerRepository.save(customerToBeSaved)).thenReturn(customerToBeSaved);

        Customer savedCustomer = customerService.createCustomer(customerToBeSaved);

        assertThat(savedCustomer, is(customerToBeSaved));
    }

    @Test
    public void givenCustomerWithInvalidDateShouldThrowIllegalArgumentExceptionWithProperMsg() {
        Customer customer = createCustomer();
        customer.setDateOfBirth(LocalDate.now().plusDays(1L));

        expectedException.expect(IllegalArgumentException.class);

        customerService.createCustomer(customer);
    }

    @Test
    public void givenCustomerIdWhenExistsThenShouldReturnCustomer() {
        Customer customer = createCustomer();
        when(customerRepository.findById(customer.getCustomerId())).thenReturn(of(customer));

        Customer actualCustomer = customerService.getCustomer(customer.getCustomerId());

        assertThat(actualCustomer, is(customer));
    }

    @Test
    public void givenCustomerIdWhenNotExistsThenShouldThrowEntityNotFoundExceptionWithProperMsg() {
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage(format("Customer With Id %d Not Found", MAX_VALUE));

        customerService.getCustomer(MAX_VALUE);
    }

    @Test
    public void givenCustomerWithNullOrEmptyFirstNameThenShouldThrowIllegalArgumentExceptionWhileSaving() {
        Customer customer = createCustomer();
        customer.setFirstName(null);

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("First Name Should Not Be Empty");

        customerService.createCustomer(customer);
    }

    @Test
    public void givenCustomerWithNullOrEmptyLastNameThenShouldThrowIllegalArgumentExceptionWhileSaving() {
        Customer customer = createCustomer();
        customer.setLastName(null);

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Last Name Should Not Be Empty");

        customerService.createCustomer(customer);
    }
}