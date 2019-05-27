package sp.banking.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sp.banking.domain.Account;
import sp.banking.domain.Customer;
import sp.banking.domain.Transaction;
import sp.banking.repository.AccountRepository;
import sp.banking.repository.TransactionRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static sp.banking.util.AccountUtils.updateAccountBalance;
import static sp.banking.util.AccountUtils.validateAccount;
import static sp.banking.util.CustomerUtils.validateCustomer;
import static sp.banking.util.TransactionUtil.createTransactions;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class AccountService {

    private static final String FROM = "FROM";
    private static final String TO = "TO";
    private static final String ACCOUNT_NOT_FOUND_ERROR = "Account Id Not Found";

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;

    @Transactional
    public void transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        List<Account> accounts = accountRepository.getAccountsByIdsAndLockRow(fromAccountId, toAccountId);
        if (accounts.size() != 2) {
            throw new EntityNotFoundException(ACCOUNT_NOT_FOUND_ERROR);
        }

        Map<String, Account> accountMap = accounts.stream()
                .collect(toMap(account -> account.getAccountId().equals(fromAccountId) ? FROM : TO, identity()));

        updateAccountBalance(accountMap.get(FROM), accountMap.get(TO), amount);
        transactionRepository.saveAll(createTransactions(fromAccountId, toAccountId, amount));
        accountRepository.saveAll(accounts);
    }

    @Transactional
    public Account createAccount(Account account) {
        validateAccount(account);

        Customer customer = customerService.getCustomer(account.getCustomer().getCustomerId());
        validateCustomer(customer);

        return accountRepository.save(account);
    }

    public List<Transaction> getTransactions(Long accountId, Pageable pageable) {
        return transactionRepository.findAllByFromAccount(accountId, pageable);
    }
}
