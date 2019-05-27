package sp.banking.dto;

import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long accountId;
    private CustomerDto customer;
    private BigDecimal currentBalance;
    private Boolean active;
}
