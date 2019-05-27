package sp.banking.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    @Column(name = "active")
    private Boolean active;

    public Account(Customer customer, BigDecimal currentBalance, Boolean active) {
        this.customer = customer;
        this.currentBalance = currentBalance;
        this.active = active;
    }
}
