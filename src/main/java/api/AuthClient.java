package api;

import config.ConfigurationManager;
import io.restassured.response.Response;
import model.AuthRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static constant.Constants.API_KEY;
import static constant.Constants.AUTH_URL;
import static constant.Constants.BASE_URL;
import static constant.Constants.CONTENT_TYPE;
import static constant.Constants.CONTENT_TYPE_JSON;
import static constant.HttpStatusCodes.OK;


public class AuthClient {
    private static final Logger logger = LoggerFactory.getLogger(AuthClient.class);

    private final String baseUrl = ConfigurationManager.get(BASE_URL);
    private final String loginEndpoint = ConfigurationManager.get(AUTH_URL); // e.g. /api/auth/login

    public Response login(AuthRequestModel authRequestModel) {
        logger.info("Login request sent to: {}", baseUrl+loginEndpoint);

        return given()
            .baseUri(baseUrl)
            .header(CONTENT_TYPE,CONTENT_TYPE_JSON)
            .body(authRequestModel)
            .post(loginEndpoint);

    }

    public String generateApiKey(String username, String password) {
        AuthRequestModel request = new AuthRequestModel(username, password);
        Response response = login(request);

        int statusCode = response.getStatusCode();
        if (statusCode == OK) {
            return response.jsonPath().getString(API_KEY);
        } else {
            logger.warn("Login failed with status code: {}", statusCode);
            return String.valueOf(statusCode);
        }
    }
}
