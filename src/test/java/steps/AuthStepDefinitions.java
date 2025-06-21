package steps;

import api.AuthClient;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import utils.TestContext;

import static utils.Constants.USERNAME_KEYWORD;
import static utils.Constants.PASSWORD_KEYWORD;

public class AuthStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(AuthStepDefinitions.class);

    private final AuthClient authClient = new AuthClient();
    private Response response;

    @Given("the user is authenticated with username {string} and password {string} when credentials not passed through CLI")
    public void userLogsIn(String username, String password) {
        // Use injected values if passed; otherwise fallback
        String user = System.getProperty(USERNAME_KEYWORD, username);
        String pass = System.getProperty(PASSWORD_KEYWORD, password);

        // Store in context or use immediately
        TestContext.set(USERNAME_KEYWORD, user);
        TestContext.set(PASSWORD_KEYWORD, pass);

        String apiKey = authClient.generateApiKey(user, pass);
        logger.info("API Key generated for Username: {}", user);

        TestContext.setApiKey(apiKey);
    }

    //Using Regex: Username and Password can be of any length
    @Given("the user attempts authentication with username \"([a-zA-Z0-9_]+)\" and password \"([a-zA-Z0-9_!@#$%^&*]+)\" fails with status code (\\d+)$")
    public void attemptInvalidAuthentication(String username, String password, int statusCode) {
         String response = authClient.generateApiKey(username, password);
         Assert.assertEquals(response, Integer.toString(statusCode));
    }
}
