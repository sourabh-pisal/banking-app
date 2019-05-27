package sp.banking.util;

import lombok.NoArgsConstructor;
import sp.banking.domain.Account;
import sp.banking.domain.Customer;
import sp.banking.domain.Transaction;

import java.math.BigDecimal;

import static java.math.BigDecimal.TEN;
import static java.time.LocalDate.now;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@NoArgsConstructor(access = PRIVATE)
public class DomainUtil {

    public static Customer createCustomer() {
        String firstName = randomAlphanumeric(10);
        String lastName = randomAlphanumeric(10);
        return new Customer(firstName, lastName, now(), true);
    }

    public static Account createAccount(Customer customer, BigDecimal balance) {
        return new Account(customer, balance, true);
    }

    public static Transaction createTransaction(Account fromAccount,
                                                Account toAccount,
                                                Transaction.TransactionType transactionType) {
        return new Transaction(fromAccount, toAccount, transactionType, TEN);
    }
}
