package controller;

import dao.PatientDAO;
import dao.StaffDAO;
import java.util.List;
import model.Patient;
import model.Staff;

public class LoginController {
    private PatientDAO patientDAO;
    private final StaffDAO staffDAO;

    // Constructor
    public LoginController() {
        this.patientDAO = new PatientDAO();
        this.staffDAO = new StaffDAO();
    }

    // Overloaded constructor to handle external DAOs
    public LoginController(PatientDAO patientDAO, StaffDAO staffDAO) {
        this.patientDAO = patientDAO != null ? patientDAO : new PatientDAO();
        this.staffDAO = staffDAO != null ? staffDAO : new StaffDAO();
    }

    // Authenticate patient using email or userID and password, return Patient object if successful
    public Patient authenticateAndGetPatient(String identifier, String password) {
        List<Patient> patients = patientDAO.getAllPatients();

        Patient patient = patients.stream()
                .filter(p -> (p.getEmailAddress().equals(identifier) || p.getUserId().equals(identifier)) && p.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (patient != null) {
            System.out.println("Login successful. Welcome, " + patient.getName() + "!");
            return patient;
        } else {
            System.out.println("Invalid email, userID, or password. Please try again.");
            return null;
        }
    }

    // Authenticate staff using userID or email and password
    public boolean authenticateStaff(String identifier, String password) {
        List<Staff> staffList = staffDAO.getAllStaff();

        Staff staff = staffList.stream()
                .filter(s -> (s.getUserID().equals(identifier) || s.getEmailAddress().equals(identifier)) && s.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (staff != null) {
            System.out.println("Login successful. Welcome, " + staff.getName() + "!");
            return true;
        } else {
            System.out.println("Invalid email, userID, or password. Please try again.");
            return false;
        }
    }

    // Generic authentication method
    public String authenticate(String identifier, String password) {
        if (authenticateAndGetPatient(identifier, password) != null) {
            return "patient";
        } else if (authenticateStaff(identifier, password)) {
            return "staff";
        } else {
            System.out.println("Authentication failed. Invalid credentials.");
            return "none";
        }
    }
}
