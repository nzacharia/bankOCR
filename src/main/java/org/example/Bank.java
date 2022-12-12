package org.example;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Bank {
    private static final int RAW_ACCOUNT_NUMBER_LENGTH = Ocr.TOTAL_DIGITS_IN_ACCOUNT_NUMBER * Ocr.SINGLE_RAW_NUMBER_WIDTH * Ocr.SINGLE_RAW_NUMBER_HEIGHT;
    private final AccountNumber account;
    private final Ocr ocr;
    private  String originalRawAccountNumbers;
    private Map<String, Set<Character>> possibleCorrectionRepository = new HashMap<>();
    public Bank(String originalRawAccountNumbers) {
        this.originalRawAccountNumbers = originalRawAccountNumbers;
        this.ocr = new Ocr();
        this.account = new AccountNumber();
    }

    public Bank() {

        this.ocr = new Ocr();
        this.account = new AccountNumber();
    }


    public String generateReport() {
        int length = originalRawAccountNumbers.length() / RAW_ACCOUNT_NUMBER_LENGTH; // number of account numbers in file
        String rawAccountNumbers = originalRawAccountNumbers;
        StringBuilder report = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            generateReport(report, ocr.parseRawNumbers(rawAccountNumbers.substring(0, RAW_ACCOUNT_NUMBER_LENGTH)));
            rawAccountNumbers = rawAccountNumbers.substring(RAW_ACCOUNT_NUMBER_LENGTH);
        }
        report.deleteCharAt(report.length() - 1);
        return report.toString();
    }

    public String generateReportFromFile(String[] accounts) {

        StringBuilder report = new StringBuilder();
        for (int i = 0; i < accounts.length; ++i) {
            generateReport(report, ocr.parseRawNumbers(accounts[i]));
        }
        report.deleteCharAt(report.length() - 1);
        return report.toString();
    }
    private void generateReport(StringBuilder report, String accountNumber) {
        account.setAccountNumber(accountNumber);
        report.append(accountNumber).append(checkStatus(account)).append("\n");
    }
    private String checkStatus(AccountNumber account) {
        if (account.isIllegal()) return " ILL";
        if (!account.isValid()) return " ERR";
        return "";
    }


    public String generateReportWithCorrection() {
        int length = originalRawAccountNumbers.length() / RAW_ACCOUNT_NUMBER_LENGTH;
        StringBuilder report = new StringBuilder();
        String rawAccountNumbers = originalRawAccountNumbers;

        for (int j = 0; j < length; ++j) {
            generateReportWithCorrection(report, rawAccountNumbers.substring(0, RAW_ACCOUNT_NUMBER_LENGTH));
            rawAccountNumbers = rawAccountNumbers.substring(RAW_ACCOUNT_NUMBER_LENGTH);
        }
        report.deleteCharAt(report.length() - 1);
        return report.toString();
    }
    public String generateReportWithCorrectionFromFile(String[] accounts) {

        StringBuilder report = new StringBuilder();

        for (int j = 0; j < accounts.length; ++j) {
            generateReportWithCorrection(report, accounts[j]);
        }
        report.deleteCharAt(report.length() - 1);
        return report.toString();
    }

    private void generateReportWithCorrection(StringBuilder report, String rawAccountNumber) {
        String accountNumber = ocr.parseRawNumbers(rawAccountNumber);
        account.setAccountNumber(accountNumber);

        if (account.isValid()) {
            report.append(accountNumber);
        } else if (notCorrectable(accountNumber)) {
            report.append(accountNumber).append(" ILL");
        } else if (isIllegal(accountNumber)) {
            List<String> corrections = guessPosibleCorrectionsForIllegalAccountNumber(rawAccountNumber);
            if (corrections.isEmpty()) {
                report.append(accountNumber).append(" ILL");
            } else {
                report.append(corrections.get(0));
                if (corrections.size() > 1) report.append(" AMB");
            }

        } else {
            List<String> corrections = guessPossibleCorrectionsForInvalidAccountNumber(rawAccountNumber);
            if (corrections.isEmpty()) {
                report.append(accountNumber).append(" ERR");
            } else {
                report.append(corrections.get(0));
                if (corrections.size() > 1) report.append(" AMB");
            }
        }

        report.append('\n');
    }

    private boolean notCorrectable(String accountNumber) {
        return StringUtils.countMatches(accountNumber, "?") > 1;
    }
    private boolean isIllegal(String accountNumber) {
        return accountNumber.contains("?");
    }


    private List<String> guessPosibleCorrectionsForIllegalAccountNumber(String rawAccountNumber) {
        String accountNumber = ocr.parseRawNumbers(rawAccountNumber);
        List<String> corrections = new ArrayList<>();
        char[] chars = accountNumber.toCharArray();
        int index = accountNumber.indexOf("?");
        String singleRawNumber = ocr.extractSingleRawNumber(rawAccountNumber, index);

        final char temp = chars[index];
        Set<Character> possibleSingleNumbers = generatePossibleSingleNumbers(singleRawNumber);
        for (char digit : possibleSingleNumbers) {
            chars[index] = digit;
            String correctedAccountNumber = new String(chars);
            account.setAccountNumber(correctedAccountNumber);
            if (account.isValid()) {
                corrections.add(correctedAccountNumber);
            }
        }
        chars[index] = temp;

        return corrections;
    }

    private Set<Character> generatePossibleSingleNumbers(String singleRawNumber) {
        if (possibleCorrectionRepository.containsKey(singleRawNumber))
            return possibleCorrectionRepository.get(singleRawNumber);

        List<String> possibleSingleRawNumbers = guessPossibleSingleRawNumbers(singleRawNumber);
        Set<Character> possibleSingleNumbers = new HashSet<>();
        for (String possibleSingleRawNumber : possibleSingleRawNumbers) {
            char possibleSingleNumber = ocr.parseSingleRawNumber(possibleSingleRawNumber).charAt(0);
            if (possibleSingleNumber != '?') {
                possibleSingleNumbers.add(possibleSingleNumber);
            }
        }
        possibleCorrectionRepository.putIfAbsent(singleRawNumber, possibleSingleNumbers);

        return possibleCorrectionRepository.get(singleRawNumber);
    }
    private List<String> guessPossibleSingleRawNumbers(String singleRawNumber) {
        List<String> possibleSingleRawNumbers = new ArrayList<>();

        char[] chars = singleRawNumber.toCharArray();
        for (int i = 0; i < singleRawNumber.length(); ++i) {
            final char temp = chars[i];
            chars[i] = isUnderscoreOrPipe(temp) ? ' ' : canOnlySetUnderscore(i) ? '_' : '|';
            possibleSingleRawNumbers.add(new String(chars));
            chars[i] = temp;
        }
        return possibleSingleRawNumbers;

    }
    private boolean isUnderscoreOrPipe(char temp) {
        return (temp == '_' || temp == '|');
    }

    private boolean canOnlySetUnderscore(int i) {
        return i % Ocr.SINGLE_RAW_NUMBER_WIDTH == 1;
    }
    private List<String> guessPossibleCorrectionsForInvalidAccountNumber(String rawAccountNumber) {
        String accountNumber = ocr.parseRawNumbers(rawAccountNumber);
        List<String> corrections = new ArrayList<>();
        char[] chars = accountNumber.toCharArray();
        List<String> singleRawNumbers = ocr.splitRawNumberIntoSingleRawNumbers(rawAccountNumber);

        for (int i = 0; i < Ocr.TOTAL_DIGITS_IN_ACCOUNT_NUMBER; ++i) {

            final char temp = chars[i];
            String singleRawNumber = singleRawNumbers.get(i);
            Set<Character> possibleSingleNumbers = generatePossibleSingleNumbers(singleRawNumber);

            for (char digit : possibleSingleNumbers) {
                chars[i] = digit;
                String correctedAccountNumber = new String(chars);
                account.setAccountNumber(correctedAccountNumber);
                if (account.isValid()) {
                    corrections.add(correctedAccountNumber);
                }
            }
            chars[i] = temp;
        }
        return corrections;
    }
}
