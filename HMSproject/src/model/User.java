package model;

public abstract class User {
    private String name;
    private final String dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String emailAddress;
    private String password;

    // Constructor with default password
    public User(String name, String dateOfBirth, String gender, String phoneNumber, String emailAddress) {
        this(name, dateOfBirth, gender, phoneNumber, emailAddress, "password");
    }

    // Constructor with custom password
    public User(String name, String dateOfBirth, String gender, String phoneNumber, String emailAddress, String password) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (validatePhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        if (validateEmailAddress(emailAddress)) {
            this.emailAddress = emailAddress;
        } else {
            throw new IllegalArgumentException("Invalid email address format.");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password; // In real use, hash the password before saving
    }

    // Method for staff or patients to change their password
    public void changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            if (!newPassword.equals("password")) {
                setPassword(newPassword);
            } else {
                throw new IllegalArgumentException("New password cannot be the default password.");
            }
        } else {
            throw new IllegalArgumentException("Old password is incorrect.");
        }
    }

    // Method to update contact information
    public void updateContactInformation(String newPhoneNumber, String newEmailAddress) {
        setPhoneNumber(newPhoneNumber);
        setEmailAddress(newEmailAddress);
    }

    // Method to get a summary of user information
    public String getSummary() {
        return "Name: " + name + ", Date of Birth: " + dateOfBirth + ", Gender: " + gender + ", Phone Number: " + phoneNumber + ", Email Address: " + emailAddress;
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^[0-9]{10,15}$"); // Allow only digits, between 10 to 15 characters
    }
    

    public static boolean validateEmailAddress(String emailAddress) {
        return emailAddress.matches("^[A-Za-z0-9+_.-]+@(.+)$"); // Basic email format validation
    }

}