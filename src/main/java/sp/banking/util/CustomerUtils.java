package sp.banking.util;

import lombok.NoArgsConstructor;
import sp.banking.domain.Customer;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.util.StringUtils.isEmpty;

@NoArgsConstructor(access = PRIVATE)
public class CustomerUtils {

    private static final String CUSTOMER_FIRST_NAME_EMPTY_ERROR = "First Name Should Not Be Empty";
    private static final String CUSTOMER_LAST_NAME_EMPTY_ERROR = "Last Name Should Not Be Empty";
    private static final String DATE_OF_BIRTH_VALIDATION_ERROR = "Date Of Birth Should Be Less Than Or Equal To Today's Date";
    private static final String CUSTOMER_NOT_ACTIVE_ERROR = "Customer Is Not Active";

    public static void validateCustomer(Customer customer) {
        validateBirthDate(customer);

        if (isEmpty(customer.getFirstName())) {
            throw new IllegalArgumentException(CUSTOMER_FIRST_NAME_EMPTY_ERROR);
        } else if (isEmpty(customer.getLastName())) {
            throw new IllegalArgumentException(CUSTOMER_LAST_NAME_EMPTY_ERROR);
        } else if (!customer.getActive()) {
            throw new IllegalArgumentException(CUSTOMER_NOT_ACTIVE_ERROR);
        }
    }

    public static void validateBirthDate(Customer customer) {
        LocalDate dateOfBirth = customer.getDateOfBirth();
        if (LocalDate.now().compareTo(dateOfBirth) < 0) {
            throw new IllegalArgumentException(DATE_OF_BIRTH_VALIDATION_ERROR);
        }
    }

}
