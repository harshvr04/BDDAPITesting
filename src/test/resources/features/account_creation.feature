Feature: Account creation API

  Scenario: Create account with valid data
    Given I have a client with first name "John" and last name "Doe"
    And the client was born on "1990-01-01"
    And the client makes an initial deposit of 1000
    When I send a request to create an account
    Then the response status should be 201
