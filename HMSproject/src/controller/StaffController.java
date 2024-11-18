package controller;

import dao.StaffDAO;
import java.util.List;
import java.util.stream.Collectors;
import model.Staff;

public class StaffController {
    private final StaffDAO staffDAO;

    // Constructor
    public StaffController() {
        this.staffDAO = new StaffDAO();
    }

    // Authenticate staff by staffID and password
    public Staff authenticateById(String staffID, String password) {
        return staffDAO.authenticateStaffById(staffID, password);
    }

    // Authenticate staff by email and password
    public Staff authenticateByEmail(String email, String password) {
        return staffDAO.authenticateStaffByEmail(email, password);
    }

    // Add a new staff member
    public boolean addStaffMember(Staff newStaff) {
        // Validation logic: Ensure that staffID and email are unique
        if (staffDAO.authenticateStaffById(newStaff.getUserID(), newStaff.getPassword()) == null &&
                staffDAO.authenticateStaffByEmail(newStaff.getEmailAddress(), newStaff.getPassword()) == null) {
            staffDAO.addStaffMember(newStaff);
            return true;
        }
        return false; // Staff with the same userID or email already exists
    }

    // Update an existing staff member's information
    public boolean updateStaffMember(Staff updatedStaff) {
        return staffDAO.updateStaffMember(updatedStaff);
    }

    // Remove a staff member by staffID
    public boolean removeStaffMember(String staffId) {
        return staffDAO.removeStaffMember(staffId);
    }

    // Check if a user is a doctor
    public boolean isDoctor(String userID) {
        return staffDAO.isDoctor(userID);
    }

    // Check if a user is an admin
    public boolean isAdmin(String userID) {
        return staffDAO.isAdmin(userID);
    }

    // Retrieve a list of all staff members
    public List<Staff> getAllStaff() {
        return staffDAO.getAllStaff();
    }

    // Retrieve doctors by department
    public List<Staff> getDoctorsByDepartment(String department) {
        return staffDAO.getAllStaff().stream()
                .filter(staff -> staff.getRole().equalsIgnoreCase("Doctor") && staff.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    // Retrieve staff members by gender
    public List<Staff> getStaffByGender(String gender) {
        return staffDAO.getAllStaff().stream()
                .filter(staff -> staff.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
    }

    // Retrieve staff members by age range
    public List<Staff> getStaffByAgeRange(int minAge, int maxAge) {
        return staffDAO.getAllStaff().stream()
                .filter(staff -> staff.getAge() >= minAge && staff.getAge() <= maxAge)
                .collect(Collectors.toList());
    }

    // Retrieve staff members by role
    public List<Staff> getStaffByRole(String role) {
        return staffDAO.getAllStaff().stream()
                .filter(staff -> staff.getRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }
}
