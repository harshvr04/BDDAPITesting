@regression
Feature: Bank Account creation Tests - Happy Path Scenarios

  Background:
    Given the user is authenticated with username Default Username and Default Password when credentials not passed through CLI

  @sanity
  Scenario Outline: Create New Bank account with valid data
    Given I have a client with first name <FirstName> and last name <LastName>
    And   the client was born on <DOB>
    And   the client makes an initial deposit of <InitialDeposit>
    When  I send a request to create an account
    Then  Assert the response status should be <ResponseCode>

    @Germany
    Examples:
      | FirstName | LastName      | DOB         | InitialDeposit| ResponseCode|
      | "Hans"    | "Olaf"        | "1993-02-01"| 100          | 200          |
      | "Erika"   | "Jung"        | "2002-01-31"| 0             | 200         |

    @Britain
    Examples:
      | FirstName | LastName     | DOB         | InitialDeposit| ResponseCode|
      | "John"    | "Doe"        | "1990-01-01"| 1000          | 200         |
      | "Xi"      | "KingPin"    | "2002-01-31"| 0             | 200         |

  @smoke
  Scenario Outline: Successfully create a New Bank account without initial deposit and validate account ID
    Given I have a client with first name <FirstName> and last name <LastName>
    And   the client was born on <DOB>
    When  I send a request to create an account
    Then  Assert the response status should be <ResponseCode>
    And   the response should contain a valid account ID

    @Germany
    Examples:
      | FirstName | LastName     | DOB         | ResponseCode|
      | "Alex"    | "Mercer"     | "1996-12-12"| 200         |

    @Britain
    Examples:
      | FirstName | LastName     | DOB         | ResponseCode|
      | "Alex"    | "Mercer"     | "1996-12-12"| 200         |
