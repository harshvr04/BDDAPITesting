package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class AccountRequestModel {
    private static final Logger logger = LoggerFactory.getLogger(AccountRequestModel.class);

    // Getters and setters
    private String first_name;
    private String last_name;
    private String date_of_birth;
    @Setter
    private double initial_deposit;

    public void setFirst_name(String first_name) {
        if (!first_name.matches("^[A-Za-z]+$")) {
            throw new IllegalArgumentException("Invalid First name: only letters allowed");
        }
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        if (!last_name.matches("^[A-Za-z]+$")) {
            throw new IllegalArgumentException("Invalid Last name: only letters allowed");
        }
        this.last_name = last_name;
    }

    public void setDate_of_birth(String date_of_birth) {
        try {
            LocalDate dob = LocalDate.parse(date_of_birth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            logger.info("Valid DOB: {}", dob);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("DOB must be in yyyy-MM-dd format and a valid date. Got: " + date_of_birth);
        }
        this.date_of_birth = date_of_birth;
    }

}