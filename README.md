# Bank OCR

## User Story 1
You work for a bank, which has recently purchased an ingenious machine to assist in reading letters and faxes sent in by branch offices. The machine scans the paper documents, and produces a file with a number of entries which each look like this:

```   
  _  _     _  _  _  _  _
| _| _||_||_ |_   ||_||_|
||_  _|  | _||_|  ||_| _|
```

Each entry is 4 lines long, and each line has 27 characters. The first 3 lines of each entry contain an account number written using pipes and underscores, and the fourth line is blank. Each account number should have 9 digits, all of which should be in the range 0-9. A normal file contains around 500 entries.

Your first task is to write a program that can take this file and parse it into actual account numbers.

 ### Implementation

 #### Unit Testing
#### Read single raw number [ `OcrTest.parse_single_raw_number` ]

- Initialize `List<String> numberDictionary` with raw numbers 1 -> 9 in Ocr class


- Call Method `Ocr.parseSingleRawNumber` with a raw number as parameter and check if it exists in ` numberDictionary` else add the `?` for the returned number


#### Read single raw account number (multiple raw numbers) [ `Ocr.parse_one_raw_account_number` ]

- Initialize `List<String> numberDictionary` with raw numbers 1 -> 9 in Ocr class
- Call Method `Ocr.parseRawNumbers` with a raw account number as parameter 
- This method will :
  - Split raw account number in 3 lines (each line has 27 chars)
  - From each line read the 3 chars based on digit index and append it to a string tha will contain the raw number 
    - e.g. 1 digit read from each line the chars with index 0-3
    - e.g  2 digit read from each line the chars with index 3-6
  - The string will be parsed to Ocr.parseSingleRawNumber which will transform the raw number to a char.
  - After 9 iteration the raw account number will be transformed to a String account number

#### Read  raw account number from a file  [ `Ocr.parse_raw_account_number_from_file` ]
- Initialize `List<String> numberDictionary` with raw numbers 1 -> 9 in Ocr class
- Call method `ocr.readAccountsFromFile` to read account from file and return a String[] with accounts
- Call Method `Ocr.parseRawNumbers` with a raw account number as parameter
- This method will :
    - Split raw account number in 3 lines (each line has 27 chars)
    - From each line read the 3 chars based on digit index and append it to a string tha will contain the raw number
        - e.g. 1 digit read from each line the chars with index 0-3
        - e.g  2 digit read from each line the chars with index 3-6
    - The string will be parsed to Ocr.parseSingleRawNumber which will transform the raw number to a char.
    - After 9 iteration the raw account number will be transformed to a String account number

## User Story 2

Having done that, you quickly realize that the ingenious machine is not in fact infallible. Sometimes it goes wrong in its scanning. The next step therefore is to validate that the numbers you read are in fact valid account numbers. A valid account number has a valid checksum. This can be calculated as follows:
```
account number:  3  4  5  8  8  2  8  6  5
position names:  d9 d8 d7 d6 d5 d4 d3 d2 d1

checksum calculation:
(d1+2*d2+3*d3+...+9*d9) mod 11 = 0
```

So now you should also write some code that calculates the checksum for a given number, and identifies if it is a valid account number.

### Implementation

#### Unit Testing
#### Validate account number that the checksum is divisible by 11 [ `AccountNumberTest.validate_account_when_modulo_checksum_is_divisible_by_11` ]

- Checking the checksum if is divisible by 11 (checksum mod 11 == 0)
- Checksum is the sum of ACCOUNT_NUMBER_LENGTH - i * ACCOUNT_NUMBER_AT_POSITION(i) [i : 0->8]

#### Validate account number that the checksum is not divisible by 11 [ `AccountNumberTest.invalid_account_when_checksum_is_not_divisible_by_11` ]
- Checking the checksum if is divisible by 11 (checksum mod 11 == 0)
- Checksum is the sum of ACCOUNT_NUMBER_LENGTH - i * ACCOUNT_NUMBER_AT_POSITION(i) [i : 0->8]

## User Story 3

Your boss is keen to see your results. He asks you to write out a file of your findings, one for each input file, in this format:

```
457508000
664371495 ERR
86110??36 ILL
```
ie the file has one account number per row. If some characters are illegible, they are replaced by a ?. In the case of a wrong checksum, or illegible number, this is noted in a second column indicating status.

### Implementation

#### Unit Testing
#### Generate report for different types of raw account numbers [ `BankTest.generate_report_for_different_types_of_raw_account_numbers` ]

- Call method `Bank.generateReport` to read each raw number and to repeat the same procedure as shown above


#### Generate report for different types of raw account numbers from a file [ `BankTest.generate_report_for_different_types_of_raw_account_numbers_from_file` ]


- Read raw numbers from a file and create a String[] with account numbers.
- Call method `Bank.generateReportFromFile` to parse the accounts numbers  and to repeat the same procedure as shown above

## User Story 4