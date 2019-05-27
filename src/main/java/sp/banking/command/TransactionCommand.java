package sp.banking.command;

import lombok.NoArgsConstructor;
import sp.banking.domain.Transaction;
import sp.banking.dto.TransactionDto;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class TransactionCommand {

    public static TransactionDto from(Transaction domain) {
        TransactionDto dto = new TransactionDto();
        dto.setFromAccount(AccountCommand.from(domain.getFromAccount()));
        dto.setToAccount(AccountCommand.from(domain.getToAccount()));
        dto.setTransactionId(domain.getTransactionId());
        dto.setTransactionTime(domain.getTransactionTime());
        dto.setTransactionType(domain.getTransactionType());
        dto.setAmount(domain.getAmount());
        return dto;
    }
}
