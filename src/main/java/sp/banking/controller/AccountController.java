package sp.banking.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sp.banking.command.TransactionReportCommand;
import sp.banking.dto.AccountDto;
import sp.banking.dto.TransactionReportDto;
import sp.banking.service.AccountService;

import java.math.BigDecimal;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static sp.banking.command.AccountCommand.from;
import static sp.banking.command.AccountCommand.to;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(from(accountService.createAccount(to(accountDto))), CREATED);
    }

    @PutMapping("/transfer")
    public void transfer(@RequestParam(value = "from-account-id") Long fromAccountId,
                         @RequestParam(value = "to-account-id") Long toAccountId,
                         @RequestParam(value = "balance") BigDecimal balance) {
        accountService.transferFunds(fromAccountId, toAccountId, balance);
    }

    @GetMapping("/{account-id}/get-transaction-details")
    public ResponseEntity<TransactionReportDto> getTransaction(@PathVariable("account-id") Long accountId,
                                                               @RequestParam(value = "page-number") Integer pageNumber) {
        PageRequest pageRequest = of(pageNumber, 10, by("transactionTime").descending());
        return new ResponseEntity<>(TransactionReportCommand.from(accountService.getTransactions(accountId, pageRequest)), OK);
    }
}
