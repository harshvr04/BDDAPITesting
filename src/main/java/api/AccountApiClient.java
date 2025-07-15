package api;

import config.ConfigurationManager;
import io.restassured.response.Response;
import model.AccountRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.TestContext;

import static io.restassured.RestAssured.given;
import static constant.Constants.ACCOUNTS_URL;
import static constant.Constants.API_KEY_HEADER;
import static constant.Constants.BASE_URL;
import static constant.Constants.CONTENT_TYPE;
import static constant.Constants.CONTENT_TYPE_JSON;

public class AccountApiClient {
    private static final Logger logger = LoggerFactory.getLogger(AccountApiClient.class);

    private final String baseUrl = ConfigurationManager.get(BASE_URL);
    private final String accountsUrl = ConfigurationManager.get(ACCOUNTS_URL);

    public Response createAccount(AccountRequestModel request) {
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