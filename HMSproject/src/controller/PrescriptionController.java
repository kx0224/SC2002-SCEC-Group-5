package controller;

import dao.PrescriptionDAO;
import model.Prescription;

import java.util.List;

public class PrescriptionController {
    private PrescriptionDAO prescriptionDAO;

    // Constructor
    public PrescriptionController() {
        this.prescriptionDAO = new PrescriptionDAO();
    }

    // Method to get all prescriptions
    public List<Prescription> getAllPrescriptions() {
        return prescriptionDAO.readPrescriptionsFromCSV();
    }

    // Method to update prescription status
    public boolean updatePrescriptionStatus(String prescriptionId, String status) {
        return prescriptionDAO.updatePrescriptionStatus(prescriptionId, status);
    }

    // Method to view prescription details
    public void viewPrescriptionDetails(String prescriptionId) {
        List<Prescription> prescriptions = prescriptionDAO.readPrescriptionsFromCSV();
        for (Prescription prescription : prescriptions) {
            if (prescription.getPrescriptionId().equalsIgnoreCase(prescriptionId)) {
                System.out.println(prescription);
                return;
            }
        }
        System.out.println("Prescription ID not found: " + prescriptionId);
    }
}
