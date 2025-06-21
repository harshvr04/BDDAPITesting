package api;

import config.ConfigurationManager;
import io.restassured.response.Response;
import model.AccountRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TestContext;

import static io.restassured.RestAssured.given;
import static utils.Constants.ACCOUNTS_URL;
import static utils.Constants.API_KEY_HEADER;
import static utils.Constants.BASE_URL;
import static utils.Constants.CONTENT_TYPE;
import static utils.Constants.CONTENT_TYPE_JSON;

public class AccountApiClient {
    private static final Logger logger = LoggerFactory.getLogger(AccountApiClient.class);

    private final String baseUrl = ConfigurationManager.get(BASE_URL);
    private final String accountsUrl = ConfigurationManager.get(ACCOUNTS_URL);

    public Response createAccount(AccountRequest request) {
        logger.info("Login request sent to: {}", baseUrl+accountsUrl);

        return given()
            .baseUri(baseUrl)
            .header(API_KEY_HEADER, TestContext.getApiKey())
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
            .body(request)
            .post(accountsUrl);
    }

    public Response getAccountById(String accountId) {
        return given()
            .baseUri(baseUrl)
            .header(API_KEY_HEADER, TestContext.getApiKey())
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
            .get(accountsUrl + "/" + accountId);
    }

}