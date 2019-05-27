package sp.banking.util;

import lombok.NoArgsConstructor;
import sp.banking.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static lombok.AccessLevel.PRIVATE;
import static sp.banking.domain.Transaction.TransactionType.CREDIT;
import static sp.banking.domain.Transaction.TransactionType.DEBIT;
import static sp.banking.util.AccountUtils.createAccount;

@NoArgsConstructor(access = PRIVATE)
public class TransactionUtil {

    public static List<Transaction> createTransactions(Long fromAccount, Long toAccountId, BigDecimal amount) {
        Transaction fromTransaction = new Transaction(createAccount(fromAccount), createAccount(toAccountId), DEBIT, amount);
        Transaction toTransaction = new Transaction(createAccount(toAccountId), createAccount(fromAccount), CREDIT, amount);
        return asList(fromTransaction, toTransaction);
    }
}
