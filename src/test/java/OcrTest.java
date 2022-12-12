import org.example.Ocr;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class OcrTest {

    @Test
    public void parse_single_raw_number() {
        // Given
        Ocr ocr = new Ocr();
        String rawSingleNumber = "" +
                " _ " +
                "| |" +
                "|_|";


        // When
        String singleNumber = ocr.parseSingleRawNumber(rawSingleNumber);

        // Then

        assertEquals(singleNumber, "0");
    }


    @Test
    public void parse_one_raw_account_number() {
        // Given
        Ocr ocr = new Ocr();
        String rawAccountNumber = "" +
                " _     _  _     _  _  _  _ " +
                "|_|  | _| _||_||_ |_   ||_|" +
                " _|  ||_  _|  | _||_|  ||_|";

        // When
        String accountNumber = ocr.parseRawNumbers(rawAccountNumber);


        // Then
        assertEquals(accountNumber, "912345678");
    }


@Test
    public void parse_raw_account_number_from_file() throws FileNotFoundException {
        // Given
        Ocr ocr = new Ocr();
    String[] accountFromFile;
    String accountNumber = null;
        // When
        accountFromFile =ocr.readAccountsFromFile("./src/test/resources/usecase1_1accountnumber.txt");

         accountNumber = ocr.parseRawNumbers(accountFromFile[0]);


        // Then
        assertEquals(accountNumber, "000000000");
    }

}
