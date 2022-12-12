import org.example.Bank;
import org.example.Ocr;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankTest {


    @Test
    public void generate_report_for_different_types_of_raw_account_numbers() {

        // Given
        final String rawAccountNumbers = "" +
                "    _  _  _  _  _  _  _  _ " +
                "|_||_   ||_ | ||_|| || || |" +
                "  | _|  | _||_||_||_||_||_|" +
                " _  _     _  _        _  _ " +
                "|_ |_ |_| _|  |  ||_||_||_ " +
                "|_||_|  | _|  |  |  | _| _|" +
                " _  _        _  _  _  _  _ " +
                "|_||_   |  || |  ||_  _||_ " +
                "|_||_|  |  ||_|    _  _||_|";
        Bank bank = new Bank(rawAccountNumbers);

        // When
        String report = bank.generateReport();

        // Then
        assertEquals(report,"457508000\n664371495 ERR\n86110??36 ILL");
    }
    @Test
    public void generate_report_for_different_types_of_raw_account_numbers_from_file() throws FileNotFoundException {
        // Given

        Ocr ocr = new Ocr();
        String[] accounts = ocr.readAccountsFromFile("./src/test/resources/usecase3.txt");
        Bank bank = new Bank();

        // When
        String report = bank.generateReportFromFile(accounts);

        // Then
        assertEquals(report,"000000051\n49006771? ILL\n1234?678? ILL\n664371495 ERR");
    }


    @Test
    public void generate_report_with_correction_when_raw_account_numbers_are_invalid() {

        // Given
        final String rawAccountNumbers = "" +
                "    _  _  _  _  _  _     _ " +
                "|_||_|| || ||_   |  |  ||_ " +
                "  | _||_||_||_|  |  |  | _|" +
                "    _  _  _  _  _  _  _  _ " +
                "|_||_   ||_ | ||_|| || || |" +
                "  | _|  | _||_||_||_||_||_|" +
                " _  _     _  _        _  _ " +
                "|_ |_ |_| _|  |  ||_||_||_ " +
                "|_||_|  | _|  |  |  | _| _|";
        Bank bank = new Bank(rawAccountNumbers);

        // When
        String report = bank.generateReportWithCorrection();

        // Then
        assertEquals(report,"490867715 AMB\n457508000\n664371485");
    }


    @Test
    public void generate_report_with_correction_when_raw_account_numbers_are_illegal() {

        // Given
        final String rawAccountNumbers = "" +
                " _  _        _  _  _  _  _ " +
                "|_||_   |  || |  ||_  _||_ " +
                "|_||_|  |  ||_|    _  _||_|" +
                " _  _     _  _        _  _ " +
                "|_ |_ |_| _|  |  ||_| _||_ " +
                "|_||_|  | _|  |  |  ||_| _|";
        Bank bank = new Bank(rawAccountNumbers);

        // When
        String report = bank.generateReportWithCorrection();

        // Then
        assertEquals(report,"86110??36 ILL\n664371485");
    }



    ////




    @Test
    public void generate_report_with_correction_when_raw_account_numbers_are_invalid_or_illegal_from_file() throws FileNotFoundException {


        // Given

        Ocr ocr = new Ocr();
        String[] accounts = ocr.readAccountsFromFile("./src/test/resources/usecase4.txt");
        Bank bank = new Bank();

        // When
        String report = bank.generateReportWithCorrectionFromFile(accounts);

        // Then
        assertEquals(report,"711111111\n777777177\n200800000\n333393333\n888886888 AMB\n559555555 AMB\n686666666 AMB\n899999999 AMB\n490867715 AMB\n123456789\n000000051\n490867715");
    }


    @Test
    public void generate_report_with_correction_when_raw_account_numbers_are_illegal_from_file() {




        // Given
        final String rawAccountNumbers = "" +
                " _  _        _  _  _  _  _ " +
                "|_||_   |  || |  ||_  _||_ " +
                "|_||_|  |  ||_|    _  _||_|" +
                " _  _     _  _        _  _ " +
                "|_ |_ |_| _|  |  ||_| _||_ " +
                "|_||_|  | _|  |  |  ||_| _|";
        Bank bank = new Bank(rawAccountNumbers);

        // When
        String report = bank.generateReportWithCorrection();

        // Then
        assertEquals(report,"86110??36 ILL\n664371485");
    }


}
