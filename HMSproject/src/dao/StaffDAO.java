package dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Staff;

public class StaffDAO {
    private static final String STAFF_LIST_FILE = System.getProperty("user.dir") + "/src/data/Staff_List.csv";
    private final List<Staff> staffMembers;

    // Constructor
    public StaffDAO() {
        this.staffMembers = new ArrayList<>();
        initializeStaffMembers();
    }

    // Initialize staff from Staff_List.csv
    private void initializeStaffMembers() {
        File staffFile = new File(STAFF_LIST_FILE);
        if (!staffFile.exists()) {
            System.err.println("Error: Staff data file not found at: " + staffFile.getAbsolutePath());
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(staffFile))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",", -1);
                if (details.length == 13) {  // Updated to include all fields including password
                    Staff staff = new Staff(
                        details[0].trim(), // userID
                        details[1].trim(), // name
                        details[2].trim(), // dateOfBirth
                        details[5].trim(), // gender
                        Integer.parseInt(details[6].trim()), // age
                        details[7].trim(), // contactNumber
                        details[8].trim(), // emailAddress
                        details[3].trim(), // role
                        details[4].trim(), // department
                        details[9].trim(), // address
                        details[10].trim(), // shift
                        details[11].trim(), // emergencyContact
                        details[12].trim()  // password
                    );
                    
                    staffMembers.add(staff);
                } else {
                    System.err.println("Error: Incorrect data format for line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing staff from Staff_List.csv: " + e.getMessage());
        }
    }

    // Method to authenticate staff by staffID and password
    public Staff authenticateStaffById(String staffID, String password) {
        return staffMembers.stream()
                .filter(staff -> staff.getUserID().equals(staffID) && staff.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    // Method to authenticate staff by email and password
    public Staff authenticateStaffByEmail(String email, String password) {
        return staffMembers.stream()
                .filter(staff -> staff.getEmailAddress().equals(email) && staff.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    // Check if a user is a doctor
    public boolean isDoctor(String userID) {
        return staffMembers.stream()
                .anyMatch(staff -> staff.getUserID().equals(userID) && staff.getRole().equalsIgnoreCase("Doctor"));
    }

    // Check if a user is an admin
    public boolean isAdmin(String userID) {
        return staffMembers.stream()
                .anyMatch(staff -> staff.getUserID().equals(userID) && staff.getRole().equalsIgnoreCase("Administrator"));
    }

    // Method to retrieve a list of all staff members
    public List<Staff> getAllStaff() {
        return new ArrayList<>(staffMembers);
    }

    // Method to add a new staff member to the CSV file
    public void addStaffMember(Staff newStaff) {
        staffMembers.add(newStaff); // Add to in-memory list
        updateStaffCSV(); // Update the CSV file
    }

    // Method to remove a staff member from the list and update CSV
    public boolean removeStaffMember(String staffId) {
        boolean removed = staffMembers.removeIf(staff -> staff.getUserID().equals(staffId));
        if (removed) {
            updateStaffCSV();  // Update CSV after removal
        }
        return removed;
    }

    // Method to update staff information
    public boolean updateStaffMember(Staff updatedStaff) {
        for (int i = 0; i < staffMembers.size(); i++) {
            if (staffMembers.get(i).getUserID().equals(updatedStaff.getUserID())) {
                staffMembers.set(i, updatedStaff);
                updateStaffCSV(); // Update the CSV file after modification
                return true;
            }
        }
        return false;
    }

    // Method to update the CSV file with the current list of staff members
    private void updateStaffCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STAFF_LIST_FILE))) {
            bw.write("staffID,name,role,department,gender,age,contactNumber,emailAddress,address,shift,emergencyContact,password"); // Corrected CSV header
            bw.newLine();
            for (Staff staff : staffMembers) {
                bw.write(String.join(",",
                        staff.getUserID(),
                        staff.getName(),
                        staff.getRole(),
                        staff.getDepartment(),
                        staff.getGender(),
                        String.valueOf(staff.getAge()),
                        staff.getPhoneNumber(),
                        staff.getEmailAddress(),
                        staff.getAddress(),
                        staff.getShift(),
                        staff.getEmergencyContact(),
                        staff.getPassword()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating staff CSV: " + e.getMessage());
        }
    }
}
