package steps;

import io.cucumber.java.After;
import io.cucumber.java.BeforeAll;
import loadtest.LoadTestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;
import util.TestContext;

//All cross-cutting concerns — like retries, context cleanup, reporting setup, screenshot capturing (for UI tests), etc. — should live in a single place.
public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    //For Load Testing
    @BeforeAll(order = 0)
    public static void beforeAll() throws Exception {
        String loadFlag = System.getProperty("loadTesting", "false");

        if (loadFlag.equalsIgnoreCase("true")) {
            logger.info("Load Testing mode enabled — running LoadTestRunner...");
            LoadTestRunner.run();

            // Gracefully skip test execution instead of System.exit
            throw new SkipException("Skipping Cucumber tests — Load test completed.");        }
    }

    @After
    public void tearDown() {
        TestContext.clear();
        logger.debug("TestContext cleared after scenario execution");
    }
}