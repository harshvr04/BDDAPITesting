package steps;

import api.AccountApiClient;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import model.AccountRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import static utils.Constants.ACCOUNT_ID;

public class AccountStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(AccountStepDefinitions.class);

    private final AccountRequest request = new AccountRequest();
    private final AccountApiClient apiClient = new AccountApiClient();
    private Response response;

    @Given("I have a client with first name {string} and last name {string}")
    public void setNames(String firstName, String lastName) {
        request.setFirst_name(firstName);
        request.setLast_name(lastName);
    }

    @And("the client was born on {string}")
    public void setDOB(String dobStr) {
        request.setDate_of_birth(dobStr);
    }

    @And("the client makes an initial deposit of {double}")
    public void setDeposit(Double deposit) {
        request.setInitial_deposit(deposit);
    }

    @When("I send a request to create an account")
    public void sendRequest() {
        //To validate Parallel Execution. To change thread-count, edit thread-count in testng.xml file
        logger.info("Running thread: {}", Thread.currentThread().getId());
        response = apiClient.createAccount(request);
    }

    @Then("Assert the response status should be {int}")
    public void validateStatusCode(Integer statusCode) {
        Assert.assertEquals(statusCode.intValue(), response.getStatusCode());
    }

    @Then("the response should contain a valid account ID")
    public void validateAccountId() {
        String id = response.jsonPath().getString(ACCOUNT_ID);
        Assert.assertNotNull(id);
        Assert.assertFalse(id.isEmpty());
    }
}