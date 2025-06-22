package helper;

import api.AccountApiClient;
import io.restassured.response.Response;
import model.AccountRequestModel;
import util.TestContext;

import static constant.HttpStatusCodes.OK;
import static constant.Constants.ACCOUNT_ID;

public class AccountHelper {
    private final AccountApiClient accountApiClient;

    public AccountHelper() {
        this.accountApiClient = new AccountApiClient();  // dependency initialization
    }

    public String createAccount(AccountRequestModel request) {
        Response response = accountApiClient.createAccount(request);

        if (response.statusCode() != OK) {
            throw new RuntimeException("Account creation failed with status: " + response.statusCode());
        }

        String accountId = response.jsonPath().getString(ACCOUNT_ID);
        TestContext.set(ACCOUNT_ID, accountId);
        return accountId;
    }

    public Response getAccountById(String accountId) {
        Response response = accountApiClient.getAccountById(accountId);

        if (response.statusCode() != OK) {
            throw new RuntimeException(String.format("Account with ID %s not found %s", accountId, response.getStatusCode()));
        }
        return response;
    }
}