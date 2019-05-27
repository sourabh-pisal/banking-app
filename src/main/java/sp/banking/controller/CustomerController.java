package sp.banking.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sp.banking.dto.CustomerDto;
import sp.banking.service.CustomerService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static sp.banking.command.CustomerCommand.from;
import static sp.banking.command.CustomerCommand.to;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        return new ResponseEntity<>(from(customerService.createCustomer(to(customerDto))), CREATED);
    }

    @GetMapping(value = "/{customer-id}/get", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("customer-id") Long customerId) {
        return new ResponseEntity<>(from(customerService.getCustomer(customerId)), OK);
    }
}