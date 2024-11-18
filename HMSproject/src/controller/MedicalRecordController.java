// MedicalRecordController.java
package controller;

import dao.MedicalRecordDAO;
import dao.StaffDAO;
import java.util.List;
import model.MedicalRecord;

public class MedicalRecordController {
    private final MedicalRecordDAO medicalRecordDAO;
    private final StaffDAO staffDAO;

    // Constructor
    public MedicalRecordController() {
        this.medicalRecordDAO = new MedicalRecordDAO();
        this.staffDAO = new StaffDAO();
    }

    // Add a new medical record (Doctor Only)
    public void addMedicalRecord(String doctorID, MedicalRecord record) {
        if (isDoctor(doctorID)) {
            medicalRecordDAO.addMedicalRecord(record);
            System.out.println("Medical record added successfully for user ID: " + record.getUserID());
        } else {
            System.err.println("Access Denied: Only doctors can add medical records.");
        }
    }

    // Update an existing medical record (Doctor Only)
    public void updateMedicalRecord(String doctorID, MedicalRecord updatedRecord) {
        if (isDoctor(doctorID)) {
            medicalRecordDAO.updateMedicalRecord(updatedRecord);
            System.out.println("Medical record updated successfully for user ID: " + updatedRecord.getUserID());
        } else {
            System.err.println("Access Denied: Only doctors can update medical records.");
        }
    }

    // View a medical record by user ID (Patient or Doctor)
    public MedicalRecord viewMedicalRecord(String requesterID, String userID) {
        MedicalRecord record = medicalRecordDAO.getAllMedicalRecords().stream()
                .filter(r -> r.getUserID().equals(userID))
                .findFirst()
                .orElse(null);

        if (record == null) {
            System.err.println("Medical record not found for user ID: " + userID);
            return null;
        }

        if (requesterID.equals(userID) || isDoctor(requesterID)) {
            System.out.println("Medical record found: " + record);
            return record;
        } else {
            System.err.println("Access Denied: You do not have permission to view this medical record.");
            return null;
        }
    }

    // List all medical records (Administrator Only)
    public List<MedicalRecord> listAllMedicalRecords(String adminID) {
        if (isAdmin(adminID)) {
            return medicalRecordDAO.getAllMedicalRecords();
        } else {
            System.err.println("Access Denied: Only administrators can view all medical records.");
            return null;
        }
    }

    // Check if a user is a doctor using StaffDAO
    private boolean isDoctor(String userID) {
        return staffDAO.isDoctor(userID);
    }

    // Check if a user is an admin using StaffDAO
    private boolean isAdmin(String userID) {
        return staffDAO.isAdmin(userID);
    }
}

// End of MedicalRecordController.java