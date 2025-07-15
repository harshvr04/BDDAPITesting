package loadtest;

import api.AccountApiClient;
import api.AuthClient;
import io.restassured.response.Response;
import model.AccountRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.TestContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static constant.Constants.ACCOUNT_ID;
import static constant.Constants.DEFAULT_PASSWORD;
import static constant.Constants.DEFAULT_USERNAME;
import static constant.Constants.IBAN_KEYWORD;
import static constant.Constants.PASSWORD_KEYWORD;
import static constant.Constants.USERNAME_KEYWORD;

public class LoadTestRunner {
    private static final Logger logger = LoggerFactory.getLogger(LoadTestRunner.class);

    //THREAD_COUNT * ITERATIONS_PER_THREAD = Number of API requests generated
    private static final int THREAD_COUNT = 2;
    private static final int ITERATIONS_PER_THREAD = 50; // 2 * 50 = 100 (100 tests running in parallel)

    public static void run() throws InterruptedException {
        String username = System.getProperty(USERNAME_KEYWORD, DEFAULT_USERNAME);
        String password = System.getProperty(PASSWORD_KEYWORD, DEFAULT_PASSWORD);

        List<Thread> threads = new ArrayList<>();
        List<Metrics> results = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(() -> {
                Metrics metrics = new Metrics();
                try {
                    //Generate API Key for each Thread
                    AuthClient authClient = new AuthClient();
                    String apiKey = authClient.generateApiKey(username, password);
                    if (apiKey == null || apiKey.isEmpty()) {
                        throw new RuntimeException("API key generation failed.");
                    }
                    TestContext.setApiKey(apiKey);

                    //Generate Account API Client for each thread
                    AccountApiClient client = new AccountApiClient();

                    //Sequential call in each thread
                    for (int j = 0; j < ITERATIONS_PER_THREAD; j++) {
                        try {
                            AccountRequestModel req = TestDataGenerator.generateRandomAccountRequest();

                            long start = System.currentTimeMillis();

                            //Create Account - POST
                            Response postResp = client.createAccount(req);
                            long end = System.currentTimeMillis();

                            metrics.requestCount++;
                            metrics.totalLatencyMillis += (end - start);
                            metrics.totalRequestBytes += req.toString().getBytes(StandardCharsets.UTF_8).length;
                            metrics.totalResponseBytes += postResp.asByteArray().length;

                            if (postResp.statusCode() < 200 || postResp.statusCode() >= 300) {
                                logger.error("Create failed: {}", postResp.statusCode());
                                metrics.failureCount++;
                                continue;
                            }

                            metrics.successCount++;
                            String id = postResp.jsonPath().getString(ACCOUNT_ID);
                            if (id != null) {
                                //Retrieve Account - GET
                                Response getResp = client.getAccountById(id);

                                logger.info(String.format("Thread %s - Account: %s | IBAN: %s%n",
                                    Thread.currentThread().getName(),
                                    id,
                                    getResp.jsonPath().getString(IBAN_KEYWORD)));
                            }
                        } catch (Exception e) {
                            logger.error("Error: {}", e.getMessage());
                            metrics.failureCount++;
                        }
                    }
                } catch (Exception e) {
                    logger.error("Thread auth error: {}", e.getMessage());
                }
                results.add(metrics);
            });

            threads.add(thread);
            thread.start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            t.join();
        }

        // Combine metrics
        Metrics total = new Metrics();
        for (Metrics m : results) {
            total.add(m);
        }

        printSummary(total);
    }

    private static void printSummary(Metrics metrics) {
        System.out.println("\n=======  LOAD TEST SUMMARY =======");
        System.out.println("Total Requests       : " + metrics.requestCount);
        System.out.println("Successful Requests  : " + metrics.successCount);
        System.out.println("Failed Requests      : " + metrics.failureCount);
        System.out.println("Average Latency (ms) : " + (metrics.requestCount == 0 ? 0 : metrics.totalLatencyMillis / metrics.requestCount));
        System.out.println("Total Request Size   : " + metrics.totalRequestBytes + " bytes");
        System.out.println("Total Response Size  : " + metrics.totalResponseBytes + " bytes");
        System.out.println("====================================\n");
    }

    static class Metrics {
        long requestCount = 0;
        long successCount = 0;
        long failureCount = 0;
        long totalLatencyMillis = 0;
        long totalRequestBytes = 0;
        long totalResponseBytes = 0;

        synchronized void add(Metrics other) {
            this.requestCount += other.requestCount;
            this.successCount += other.successCount;
            this.failureCount += other.failureCount;
            this.totalLatencyMillis += other.totalLatencyMillis;
            this.totalRequestBytes += other.totalRequestBytes;
            this.totalResponseBytes += other.totalResponseBytes;
        }
    }
}
