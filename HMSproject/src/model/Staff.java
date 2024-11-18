package model;

public class Staff extends User {
    private final String userID;
    private final String role;
    private final String department;
    private final String address;
    private final int age;
    private final String shift;
    private final String emergencyContact;

    // Constructor
    public Staff(String userID, String name, String dateOfBirth, String gender, int age, String phoneNumber, String emailAddress, String role, String department, String address, String shift, String emergencyContact, String password) {
        super(name, dateOfBirth, gender, phoneNumber, emailAddress, password);
        this.userID = userID;
        this.role = role;
        this.department = department;
        this.age = age;
        this.address = address;
        this.shift = shift;
        this.emergencyContact = emergencyContact;
    }

    // Getters
    public String getUserID() {
        return userID;
    }

    public String getRole() {
        return role;
    }

    public String getDepartment() {
        return department;
    }

    public String getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }

    public String getShift() {
        return shift;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    // Optional: Method to get a summary of the staff member's information
    @Override
    public String getSummary() {
        return String.format("Staff ID: %s, Name: %s, Role: %s, Department: %s, Age: %d, Contact: %s, Shift: %s",
                userID, getName(), role, department, age, getPhoneNumber(), shift);
    }
}