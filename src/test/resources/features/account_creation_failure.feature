@regression
Feature: Account creation Failure Tests - Sad Path Scenarios

  Background:
    Given the user is authenticated with username "alice" and password "s3cr3t" when credentials not passed through CLI

  @smoke @local
  Scenario Outline: Creating account with Name length more than threshold should FAIL
    Given I have a client with first name <FirstName> and last name <LastName>
    And   the client was born on <DOB>
    And   the client makes an initial deposit of <InitialDeposit>
    When  I send a request to create an account
    Then  Assert the response status should be <ResponseCode>

    Examples:
      | FirstName                               | LastName                         | DOB         | InitialDeposit| ResponseCode|
      | "MoreThanMoreThanMoreThanMoreThan"      | "FiftyFiftyFiftyFiftyFifty"      | "2002-01-31"| 0             | 411         |

  @smoke  @local
  Scenario Outline: Creating account with Age of the customer is less than 18 years should FAIL
    Given I have a client with first name <FirstName> and last name <LastName>
    And   the client was born on <DOB>
    When  I send a request to create an account
    Then  Assert the response status should be <ResponseCode>

    Examples:
      | FirstName | LastName     | DOB         | ResponseCode|
      | "Alex"    | "Mercer"     | "2025-01-01"| 400         |
