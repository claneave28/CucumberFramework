@Search @excel

Feature: User is using an excel spreadsheet with cucumber driving it

  Scenario Outline: Data Driven with excel and data sets

   When I am on the amps mainscreen
    Then I input username and passwords with excel row"<row_index>" dataset

    Examples:
    | row_index  |
    | 1          |
    | 2          |
    | 3          |
    | 4          |



@ExcelTest
Scenario Outline:  Data Driven based on Column header

 When I goto the mainscreen for excel
 Then I input the column "<header>" that I am searching

 Examples:
 |header|
 |UserName |
 |Password |
 |Result1  |
 |Result2 |
 |fail    |






