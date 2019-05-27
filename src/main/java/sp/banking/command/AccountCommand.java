package sp.banking.command;

import lombok.NoArgsConstructor;
import sp.banking.domain.Account;
import sp.banking.dto.AccountDto;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class AccountCommand {

    public static AccountDto from(Account domain) {
        AccountDto dto = new AccountDto();
        dto.setAccountId(domain.getAccountId());
        dto.setCurrentBalance(domain.getCurrentBalance());
        dto.setCustomer(CustomerCommand.from(domain.getCustomer()));
        dto.setActive(domain.getActive());
        return dto;
    }

    public static Account to(AccountDto dto) {
        Account domain = new Account();
        domain.setAccountId(dto.getAccountId());
        domain.setCurrentBalance(dto.getCurrentBalance());
        domain.setCustomer(CustomerCommand.to(dto.getCustomer()));
        domain.setActive(dto.getActive());
        return domain;
    }
}
