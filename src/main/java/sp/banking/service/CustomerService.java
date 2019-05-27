package sp.banking.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sp.banking.domain.Customer;
import sp.banking.repository.CustomerRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import static java.lang.String.format;
import static sp.banking.util.CustomerUtils.validateCustomer;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CustomerService {

    private static final String CUSTOMER_NOT_FOUND_ERROR = "Customer With Id %d Not Found";

    private final CustomerRepository customerRepository;

    @Transactional
    public Customer createCustomer(Customer customer) {
        validateCustomer(customer);
        return customerRepository.save(customer);
    }

    public Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(format(CUSTOMER_NOT_FOUND_ERROR, customerId)));
    }
}
