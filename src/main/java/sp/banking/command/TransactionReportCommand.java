package sp.banking.command;

import lombok.NoArgsConstructor;
import sp.banking.domain.Transaction;
import sp.banking.dto.TransactionDto;
import sp.banking.dto.TransactionReportDto;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class TransactionReportCommand {

    public static TransactionReportDto from(List<Transaction> domains) {
        TransactionReportDto dto = new TransactionReportDto();
        dto.setAccountDto(AccountCommand.from(domains.get(0).getFromAccount()));
        List<TransactionDto> transactionDtos =
                domains.stream()
                        .map(TransactionCommand::from).collect(toList());
        dto.setTransactionDtos(transactionDtos);
        return dto;
    }

}
