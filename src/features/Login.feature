@Regression
Feature: Login Functionality Feature

 @GTS-371
  Scenario: Login Success

    Given user navigates to the AMPS login screen
    When user logs in with correct credentials
    Then login should be successful

 @GTS-222
   Scenario: Login Failure Invalid Login and Password

     Given user navigates to the AMPS login screen
     When user logs in using Username as "User1" and Password "Password1"
     Then error message should display

  @GTS-241
   Scenario: Login Failure with missing UserName

    Given user navigates to the AMPS login screen
    When user logs in using Username as "User1" and Password "Password1"
    Then error message should display for missing username after username is deleted

  @GTS-244 @smoke
  Scenario: Login Failure with missing Password

    Given user navigates to the AMPS login screen
    When user logs in using Username as "User1" and Password "Password1"
    Then error message should display for missing username after password is deleted

    @Demo1
    Scenario: This is a demo step

      Given this is a demo step
      When I run this demo step
      Then you should see code snippets



