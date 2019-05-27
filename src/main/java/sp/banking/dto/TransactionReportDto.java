package sp.banking.dto;

import lombok.*;

import java.util.List;

@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReportDto {
    private AccountDto accountDto;
    private List<TransactionDto> transactionDtos;
}
