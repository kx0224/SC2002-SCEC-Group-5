package hms.users;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllUsers {
    private List<User> users;

    // Constructor initializes the users list and loads data from files
    public AllUsers() {
        users = new ArrayList<>();
        initialiseStaff(); // Load staff from Staff_List.csv
        initialisePatients(); // Load patients from Patient_List.csv
    }

    // Method to retrieve a user by hospital ID
    public User getUserById(String hospitalID) {
        for (User user : users) {
            if (user.getHospitalID().equals(hospitalID)) {
                return user;
            }
        }
        return null; // Return null if user not found
    }

    // Method to initialize staff from Staff_List.csv
    private void initialiseStaff() {
        String line;
        String splitBy = ",";

        // Reading staff list
        try (BufferedReader br = new BufferedReader(new FileReader("Staff_List.csv"))) {
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] newuser = line.split(splitBy);
                if (newuser.length >= 6) { // Ensure there are enough fields
                    String hospitalID = newuser[0].trim();
                    String password = newuser[1].trim();
                    String name = newuser[2].trim();
                    String gender = newuser[3].trim();
                    String role = newuser[4].trim();
                    int age;
                    try {
                        age = Integer.parseInt(newuser[5].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping line due to invalid age: " + line);
                        continue;
                    }

                    // Role-based instantiation for staff
                    switch (role.toLowerCase()) {
                        case "doctor":
                            String specialization = newuser.length > 6 ? newuser[6].trim() : "General"; // Optional specialization
                            users.add(new Doctor(hospitalID, password, name, gender, role, age, specialization));
                            break;
                        case "pharmacist":
                            users.add(new Pharmacist(hospitalID, password, name, gender, role, age));
                            break;
                        case "administrator":
                            users.add(new Administrator(hospitalID, password, name, gender, role, age, this));
                            break;
                        default:
                            System.out.println("Invalid role: " + role + " for staff member: " + name);
                    }
                } else {
                    System.out.println("Skipping malformed line in Staff_List.csv: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Staff_List.csv: " + e.getMessage());
        }
    }

    // Method to initialize patients from Patient_List.csv
    private void initialisePatients() {
        String line;
        String splitBy = ",";

        // Reading patient list
        try (BufferedReader br = new BufferedReader(new FileReader("Patient_List.csv"))) {
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] newuser = line.split(splitBy);
                if (newuser.length >= 10) { // Ensure there are enough fields
                    String hospitalID = newuser[0].trim();
                    String password = newuser[1].trim();
                    String name = newuser[2].trim();
                    String gender = newuser[3].trim();
                    int age;
                    try {
                        age = Integer.parseInt(newuser[4].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping line due to invalid age: " + line);
                        continue;
                    }
                    String dateOfBirth = newuser[5].trim();
                    String contactInfo = newuser[6].trim();
                    String bloodType = newuser[7].trim();
                    String pastDiagnoses = newuser[8].trim();
                    String role = newuser[9].trim();

                    // Ensure gender is correctly assigned
                    if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
                        System.out.println("Invalid gender for patient: " + name + ". Skipping.");
                        continue;
                    }

                    // Capitalize the first letter of the gender to ensure proper formatting
                    gender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();

                    // Create Patient instance
                    Patient patient = new Patient(hospitalID, password, name, gender, role, age, dateOfBirth, contactInfo, bloodType, pastDiagnoses);
                    users.add(patient);
                } else {
                    System.out.println("Skipping malformed line in Patient_List.csv: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Patient_List.csv: " + e.getMessage());
        }
    }

    // Method to get the list of all users
    public List<User> getAllUsers() {
        return users;
    }

    // Method to add a new user to the list
    public void addUser(User user) {
        users.add(user);
    }

    // Method to find a user by hospital ID
    public User findUserById(String hospitalID) {
        for (User user : users) {
            if (user.getHospitalID().equals(hospitalID)) {
                return user;
            }
        }
        return null; // Return null if user not found
    }
}