package loadtest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import model.AccountRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//This CLass Generates Random Test Data for Load Testing
public class TestDataGenerator {
    private static final Logger logger = LoggerFactory.getLogger(TestDataGenerator.class);

    private static final List<String> FIRST_NAMES = List.of("MoreThanFiFtyMoreThanFiFtyMoreThanFiFty", "Alice", "Sam", "Eva", "Max", "Lily");
    private static final List<String> LAST_NAMES = List.of("MoreThanFiFtyMoreThanFiFtyMoreThanFiFty", "Brown", "Taylor", "Lee", "Davis", "White");

    public static AccountRequest generateRandomAccountRequest() {
        Random rand = new Random();
        String firstName = FIRST_NAMES.get(rand.nextInt(FIRST_NAMES.size()));
        String lastName = LAST_NAMES.get(rand.nextInt(LAST_NAMES.size()));

        // Random Date of Birth generator from age group 15–70 years. Deliberately including 15-17 years for generating wrong data
        String dob = LocalDate.now().minusYears(15 + rand.nextInt(55))
            .minusDays(rand.nextInt(365))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        //This line generates a random deposit amount between 0 and 10,000, rounded to 2 decimal places.
        double deposit = Math.round(rand.nextDouble() * 10000.0 * 100.0) / 100.0;

        //Creating Payload for Account Creation API
        AccountRequest request = new AccountRequest();
        request.setFirst_name(firstName);
        request.setLast_name(lastName);
        request.setDate_of_birth(dob);
        request.setInitial_deposit(deposit);
        return request;
    }
}

