package models;

import java.util.*;
import controllers.UserController;


abstract public class User {
    // Fields
    public String hospitalID;
    protected String passwordHash;
    protected UserRoles role;
    protected String name;
    protected boolean firstLogin;
    protected String gender;
    protected String email;
    protected String phonenumber;

    // Constructor
    public User(String hospitalID, String password, UserRoles role, String name, String gender, String email, String phonenumber) {
        this.hospitalID = hospitalID;
        this.passwordHash = password;
        this.role = role;
        this.name = name;
        this.firstLogin = true;
        this.gender = gender;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    // Getters and Setters
    public String getHospitalID() {
        return hospitalID;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public boolean getfirstLogin() {
        return firstLogin;
    }

    public void setfirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    // Method to change password
    public void changePassword() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter new password (must be more than 6 characters and cannot be 'password'): ");
                String newPassword = sc.nextLine().trim();

                // Check password length
                if (newPassword.length() < 7) {
                    System.out.println("Error: Password is too short. Please enter a password with more than 6 characters.");
                    continue;
                }

                // Check if the password is "password"
                if (newPassword.equalsIgnoreCase("password")) {
                    System.out.println("Error: Password cannot be 'password'. Please choose a different password.");
                    continue;
                }

                System.out.print("Confirm new password: ");
                String confirmPassword = sc.nextLine().trim();

                // Check if the passwords match
                if (newPassword.equals(confirmPassword)) {
                    this.passwordHash = newPassword; // Ideally, hash the password here before storing
                    System.out.println("Updated password hash: " + this.passwordHash); // For demonstration purposes
                    System.out.println("Password updated successfully.");

                    // Update the user in the user list
                    List<User> userList = UserController.users.get(this.role);
                    if (userList != null) {
                        boolean userUpdated = false;
                        for (int i = 0; i < userList.size(); i++) {
                            if (userList.get(i).getHospitalID().equals(this.hospitalID)) {
                                userList.set(i, this); // Replace the user in the list with the updated one
                                userUpdated = true;
                                break;
                            }
                        }
                    }
                    break;
                } else {
                    System.out.println("Error: Passwords do not match. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    // Abstract method to show menu
    public abstract void showMenu();
}