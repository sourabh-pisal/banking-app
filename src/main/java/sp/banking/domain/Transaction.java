package sp.banking.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @Enumerated(STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "transaction_date", updatable = false)
    private LocalDateTime transactionTime;

    public Transaction() {
        this.transactionTime = LocalDateTime.now();
    }

    public Transaction(Account fromAccount,
                       Account toAccount,
                       TransactionType transactionType,
                       BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionTime = LocalDateTime.now();
    }

    public enum TransactionType {
        CREDIT, DEBIT
    }
}
