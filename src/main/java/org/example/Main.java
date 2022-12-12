package org.example;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        Ocr bOcr = new Ocr();
       String[] accounts = bOcr.readAccountsFromFile("./src/test/resources/usecase1_multipleaccounts.txt");
       // System.out.printf(Arrays.toString(accounts));
        for (int i = 0; i < accounts.length; i++) {
            System.out.println(bOcr.parseRawNumbers(accounts[i]));
        }



        String[] accountsForReport = bOcr.readAccountsFromFile("./src/test/resources/usecase4.txt");;
        Bank bank = new Bank();


        String report = bank.generateReportFromFile(accountsForReport);
        System.out.println(report);


    }
}