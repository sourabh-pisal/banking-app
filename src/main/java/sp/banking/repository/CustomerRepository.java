package sp.banking.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sp.banking.domain.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
