package steps;

import io.cucumber.java.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.TestContext;

//All cross-cutting concerns — like retries, context cleanup, reporting setup, screenshot capturing (for UI tests), etc. — should live in a single place.
public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    @After
    public void tearDown() {
        TestContext.clear();
        logger.debug("TestContext cleared after scenario execution");
    }
}