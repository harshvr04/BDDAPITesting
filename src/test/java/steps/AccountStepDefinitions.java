package steps;

import api.AccountApiClient;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import model.AccountRequestModel;

import model.builder.AccountRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import static constant.Constants.ACCOUNT_ID;
import static constant.Constants.MESSAGE;

public class AccountStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(AccountStepDefinitions.class);

    //Using Builder Pattern to build Account API Request
    private final AccountRequestBuilder builder = AccountRequestBuilder.builder();
    private AccountRequestModel request;
    private final AccountApiClient apiClient;
    private Response response;

    public AccountStepDefinitions() {
        apiClient = new AccountApiClient();
    }

    @Given("I have a client with first name {string} and last name {string}")
    public void setNames(String firstName, String lastName) {
        builder.withFirstName(firstName)
            .withLastName(lastName);
    }

    @And("the client was born on {string}")
    public void setDOB(String dobStr) {
        builder.withDob(dobStr);
    }

    @And("the client makes an initial deposit of {double}")
    public void setDeposit(Double deposit) {
        builder.withInitialDeposit(deposit);
    }

    @When("I send a request to create an account")
    public void sendRequest() {
        //To validate Parallel Execution. To change thread-count, edit thread-count in testng.xml file
        logger.info("Running thread: {}", Thread.currentThread().getId());
        request = builder.build();
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

    @Then("Assert the response should have message {string}")
    public void validateResponseMessage(String messageExpected) {
        String messageActual = response.jsonPath().getString(MESSAGE);
        Assert.assertNotNull(messageActual);
        Assert.assertFalse(messageActual.isEmpty());
        Assert.assertEquals(messageActual, messageExpected);
    }
}