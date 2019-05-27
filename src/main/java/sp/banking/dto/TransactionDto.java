package sp.banking.dto;

import lombok.*;
import sp.banking.domain.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long transactionId;
    private AccountDto fromAccount;
    private AccountDto toAccount;
    private Transaction.TransactionType transactionType;
    private LocalDateTime transactionTime;
    private BigDecimal amount;
}
