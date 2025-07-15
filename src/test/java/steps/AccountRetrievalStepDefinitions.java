package steps;

import static constant.Constants.ACCOUNT_ID;
import static constant.Constants.FIRST_NAME;
import static constant.Constants.LAST_NAME;
import static constant.HttpStatusCodes.OK;

import helper.AccountHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import model.AccountRequestModel;
import model.builder.AccountRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import util.TestContext;

public class AccountRetrievalStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(AccountRetrievalStepDefinitions.class);

    //Using Builder Pattern to build Account API Request
    private final AccountRequestBuilder builder = AccountRequestBuilder.builder();
    private AccountRequestModel request = new AccountRequestModel();
    private final AccountHelper accountHelper = new AccountHelper();
    private Response response;

    @Given("the user has created an account with first name {string}, last name {string}, date of birth {string} and no initial deposit")
    public void userCreatesAccount(String firstName, String lastName, String dob) {
        request = builder.withFirstName(firstName)
            .withLastName(lastName)
            .withDob(dob)
            .build();
        accountHelper.createAccount(request);
    }

    @When("the user retrieves the account by ID")
    public void retrieveAccountById() {
        String accountId = TestContext.get(ACCOUNT_ID).toString();
        Response response = accountHelper.getAccountById(accountId);
        TestContext.setResponse(response);
    }

    @Then("the response should contain first name {string} and last name {string}")
    public void validateAccountData(String expectedFirstName, String expectedLastName) {
        Response response = TestContext.getResponse();
        Assert.assertEquals(response.getStatusCode(), OK);
        Assert.assertEquals (response.jsonPath().getString(FIRST_NAME), expectedFirstName);
        Assert.assertEquals (response.jsonPath().getString(LAST_NAME), expectedLastName);
    }

}