@regression
Feature: Bank Account creation Failure Tests - Sad Path Scenarios

  Background:
    Given the user is authenticated with username "alice" and password "s3cr3t" when credentials not passed through CLI

  @smoke @local
  Scenario Outline: Creating New Bank account with Name length more than threshold should FAIL
    Given I have a client with first name <FirstName> and last name <LastName>
    And   the client was born on <DOB>
    And   the client makes an initial deposit of <InitialDeposit>
    When  I send a request to create an account
    Then  Assert the response status should be <ResponseCode>
    Then  Assert the response should have message <Message>

    Examples:
      | FirstName                               | LastName                         | DOB         | InitialDeposit| ResponseCode| Message                                    |
      | "MoreThanMoreThanMoreThanMoreThan"      | "FiftyFiftyFiftyFiftyFifty"      | "2002-01-31"| 0             | 411         | "Full name must be less than 50 characters"|

  @smoke  @local
  Scenario Outline: Creating New Bank account with No Initial deposit and Age of the customer is less than 18 years should FAIL
    Given I have a client with first name <FirstName> and last name <LastName>
    And   the client was born on <DOB>
    When  I send a request to create an account
    Then  Assert the response status should be <ResponseCode>
    Then  Assert the response should have message <Message>

    Examples:
      | FirstName | LastName     | DOB         | ResponseCode| Message                                       |
      | "Alex"    | "Mercer"     | "2025-01-01"| 400         | "Account holder must be at least 18 years old"|

