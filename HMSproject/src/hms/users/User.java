package hms.users;

public class User {
    protected String hospitalID;
    protected String password;
    protected String role;
    protected String name;    // New field for name
    protected String gender;  // New field for gender
    protected int age;        // New field for age
    protected boolean initialLogin;

    // Constructor with the additional fields
    public User(String hospitalID, String password, String role, String name, String gender, int age) {
        this.hospitalID = hospitalID;
        this.password = "password";
        this.role = role;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.initialLogin = true; // Assume first login on creation
    }

    // Method to check if the provided password matches the stored password
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Method to change the user's password
    public boolean changePassword(String newPassword) {
        if (isValidPassword(newPassword)) {
            this.password = newPassword;
            this.initialLogin = false; // User has now set a new password
            return true;
        } else {
            System.out.println("Password must be at least 8 characters.");
            return false;
        }
    }

    // Private helper method to check if the password meets criteria
    private boolean isValidPassword(String password) {
        return password.length() >= 8; // Example rule: password must be 8+ characters
    }

    // Getter for the user's role
    public String getRole() {
        return this.role;
    }

    // Getter for hospital ID
    public String getHospitalID() {
        return this.hospitalID;
    }

    // Getter for name
    public String getName() {
        return this.name;
    }

    // Getter for gender
    public String getGender() {
        return this.gender;
    }

    // Getter for age
    public int getAge() {
        return this.age;
    }

    // Method to check if it’s the user’s first login
    public boolean isFirstLogin() {
        return this.initialLogin;
    }
}
