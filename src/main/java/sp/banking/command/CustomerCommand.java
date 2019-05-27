package sp.banking.command;

import lombok.NoArgsConstructor;
import sp.banking.domain.Customer;
import sp.banking.dto.CustomerDto;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CustomerCommand {

    public static CustomerDto from(Customer domain) {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(domain.getCustomerId());
        dto.setActive(domain.getActive());
        dto.setDateOfBirth(domain.getDateOfBirth());
        dto.setFirstName(domain.getFirstName());
        dto.setLastName(domain.getLastName());
        return dto;
    }

    public static Customer to(CustomerDto dto) {
        Customer domain = new Customer();
        domain.setCustomerId(dto.getCustomerId());
        domain.setActive(dto.getActive());
        domain.setDateOfBirth(dto.getDateOfBirth());
        domain.setFirstName(dto.getFirstName());
        domain.setLastName(dto.getLastName());
        return domain;
    }
}
