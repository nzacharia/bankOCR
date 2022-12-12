import org.example.AccountNumber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountNumberTest {
    @Test
    public void validate_account_when_modulo_checksum_is_divisible_by_11() {
        String accountNumber = "345882865";
        // When
        AccountNumber account = new AccountNumber(accountNumber);

        // Then
        assertEquals(account.isValid(),true);
    }

    @Test
    public void invalid_account_when_checksum_is_not_divisible_by_11() {

        // Given
        String accountNumber = "000000018";

        // When
        AccountNumber account = new AccountNumber(accountNumber);

        // Then
        assertEquals(account.isValid(),false);
    }

}
