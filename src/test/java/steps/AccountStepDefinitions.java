package steps;

import api.AccountApiClient;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import model.AccountRequest;

import org.testng.Assert;

public class AccountStepDefinitions {

    private final AccountRequest request = new AccountRequest();
    private final AccountApiClient apiClient = new AccountApiClient();
    private Response response;

    @Given("I have a client with first name {string} and last name {string}")
    public void setNames(String firstName, String lastName) {
        request.setFirst_name(firstName);
        request.setLast_name(lastName);
    }

    @And("the client was born on {string}")
    public void setDOB(String dob) {
        request.setDate_of_birth(dob);
    }

    @And("the client makes an initial deposit of {double}")
    public void setDeposit(Double deposit) {
        request.setInitial_deposit(deposit);
    }

    @When("I send a request to create an account")
    public void sendRequest() {
        response = apiClient.createAccount(request);
    }

    @Then("the response status should be {int}")
    public void validateStatusCode(Integer statusCode) {
        Assert.assertEquals(statusCode.intValue(), response.getStatusCode());
    }
}