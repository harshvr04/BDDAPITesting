package api;

import config.ConfigurationManager;
import io.restassured.response.Response;
import model.AccountRequest;

import static io.restassured.RestAssured.given;

public class AccountApiClient {
    private final String baseUrl = ConfigurationManager.get("base.url");
    private final String apiKey = ConfigurationManager.get("api.key");

    public Response createAccount(AccountRequest request) {
        return given()
            .baseUri(baseUrl)
            .header("x-api-key", apiKey)
            .header("Content-Type", "application/json")
            .body(request)
            .post("/api/accounts");
    }
}