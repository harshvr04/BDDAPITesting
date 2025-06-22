@regression
Feature: Bank Account Retrieval Feature - Happy Path Scenarios

  Background:
    Given the user is authenticated with username Default Username and Default Password when credentials not passed through CLI

  @sanity
  Scenario Outline: Retrieve Newly Created Bank account by ID
    Given the user has created an account with first name <FirstName>, last name <LastName>, date of birth <DOB> and no initial deposit
    When the user retrieves the account by ID
    Then the response should contain first name <FirstName> and last name <LastName>

    @Germany
    Examples:
      | FirstName       | LastName        | DOB          |
      | "Mercedes"      | "Benz"          | "2002-01-31" |