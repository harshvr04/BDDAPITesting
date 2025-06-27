package model.builder;

import model.AccountRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountRequestBuilder {
    private static final Logger logger = LoggerFactory.getLogger(AccountRequestBuilder.class);
    private final AccountRequestModel request = new AccountRequestModel();

    public static AccountRequestBuilder builder() {
        return new AccountRequestBuilder();
    }

    public AccountRequestBuilder withFirstName(String firstName) {
        request.setFirst_name(firstName);
        return this;
    }

    public AccountRequestBuilder withLastName(String lastName) {
        request.setLast_name(lastName);
        return this;
    }

    public AccountRequestBuilder withDob(String dob) {
        request.setDate_of_birth(dob);
        return this;
    }

    public AccountRequestBuilder withInitialDeposit(double amount) {
        request.setInitial_deposit(amount);
        return this;
    }

    public AccountRequestModel build() {
        return request;
    }
}
