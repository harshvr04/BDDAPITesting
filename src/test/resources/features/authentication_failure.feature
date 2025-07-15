@regression
Feature: Authentication Failure Feature - Sad Path Scenarios

  @sanity
  Scenario: Authentication fails with invalid credentials
    Given the user attempts authentication with username "invalidUser" and password "wrongPass" fails with status code 401