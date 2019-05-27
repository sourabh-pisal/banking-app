package sp.banking.dto;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Long customerId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Boolean active;
}
