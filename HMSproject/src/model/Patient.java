package model;

import java.io.Serializable;

public class Patient extends User implements Serializable {
    private static final long serialVersionUID = 1L; // Added for serialization
    private final String userID;

    // Constructor
    public Patient(String userID, String name, String dateOfBirth, String gender, String phoneNumber, String emailAddress, String password) {
        super(name, dateOfBirth, gender, phoneNumber, emailAddress, password);
        this.userID = userID;
    }

    // Getter for userID
    public String getUserId() {
        return userID;
    }

    // Getter for password (added for PatientDAO integration)
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    // Method to update personal information
    public void updatePersonalInformation(String newPhoneNumber, String newEmailAddress) {
        try {
            updateContactInformation(newPhoneNumber, newEmailAddress);
            System.out.println("Contact information updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid phone number or email address format. Please try again.");
        }
    }

    // Method to get a summary of patient information
    @Override
    public String getSummary() {
        return super.getSummary() + ", User ID: " + userID;
    }


}
