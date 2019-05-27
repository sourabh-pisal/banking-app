package sp.banking.util;

import lombok.NoArgsConstructor;
import sp.banking.domain.Account;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;
import static sp.banking.util.CustomerUtils.validateCustomer;

@NoArgsConstructor(access = PRIVATE)
public class AccountUtils {
    private static final String INVALID_BALANCE_ERROR = "Balance Amount Should Be Greater Than Zero";
    private static final String ACCOUNT_NOT_ACTIVE_ERROR = "Account Not Active";

    public static Account createAccount(Long accountId) {
        Account account = new Account();
        account.setAccountId(accountId);
        return account;
    }

    public static void validateAccount(Account account) {
        if (!account.getActive()) {
            throw new IllegalArgumentException(ACCOUNT_NOT_ACTIVE_ERROR);
        }
    }

    public static void validateAccount(Account account, BigDecimal balance) {
        validateBalance(balance);
        validateAccount(account);
    }

    public static void validateBalance(BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new IllegalArgumentException(INVALID_BALANCE_ERROR);
        }
    }

    public static void updateAccountBalance(Account fromAccount, Account toAccount, BigDecimal amount) {
        validateCustomer(fromAccount.getCustomer());
        validateCustomer(toAccount.getCustomer());
        validateBalance(amount);
        validateAccount(fromAccount, amount);
        validateAccount(toAccount);
        fromAccount.setCurrentBalance(fromAccount.getCurrentBalance().subtract(amount));
        toAccount.setCurrentBalance(toAccount.getCurrentBalance().add(amount));
    }
}
