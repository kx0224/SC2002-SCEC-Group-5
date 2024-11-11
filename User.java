package allusers;

public class User {
    private final String hospitalID;
    private String password;
    private final String role;
    private boolean initialLogin;

    public User(String hospitalID, String role) {
        this.hospitalID = hospitalID;
        this.password = "password"; // default password
        this.role = role;
        this.initialLogin = true;
    }

    public boolean validateLogin(String hospitalID, String password) {
        return this.hospitalID.equals(hospitalID) && this.password.equals(password);
    }

    public void changePassword(String newPassword) {
        if (isValidPassword(newPassword)) {
            this.password = newPassword;
            this.initialLogin = false;
        } else {
            throw new IllegalArgumentException("Password does not meet requirements.");
        }
    }

    private boolean isValidPassword(String password) {
        // Add password validation logic (e.g., length, complexity)
        return password.length() >= 6;
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public String getRole() {
        return role;
    }

    public boolean isInitialLogin() {
        return initialLogin;
    }

    public boolean checkPassword(String inputPassword) {
        return inputPassword.equals(password);
    }
}
