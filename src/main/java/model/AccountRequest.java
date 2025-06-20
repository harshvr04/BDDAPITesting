package model;

public class AccountRequest {
    private String first_name;
    private String last_name;
    private String date_of_birth;
    private double initial_deposit;

    // Getters and setters
    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }

    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }

    public String getDate_of_birth() { return date_of_birth; }
    public void setDate_of_birth(String date_of_birth) { this.date_of_birth = date_of_birth; }

    public double getInitial_deposit() { return initial_deposit; }
    public void setInitial_deposit(double initial_deposit) { this.initial_deposit = initial_deposit; }
}