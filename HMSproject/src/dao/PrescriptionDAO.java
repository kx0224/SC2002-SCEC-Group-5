package dao;

import model.Prescription;
import model.Medicine;
import dao.InventoryDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrescriptionDAO {
    private static final Logger logger = Logger.getLogger(PrescriptionDAO.class.getName());
    private static final String FILE_PATH = "data/Prescription_List.csv";

    // Method to fulfill a prescription and update inventory stock
    public boolean fulfillPrescription(Prescription prescription) {
        String medicationId = prescription.getMedicationId();
        int quantity = prescription.getQuantity();

        List<Medicine> medicines = InventoryDAO.readInventoryFromCSV();
        for (Medicine medicine : medicines) {
            if (medicine.getMedicationId().equalsIgnoreCase(medicationId)) {
                if (medicine.getStockLevel() >= quantity) {
                    medicine.setStockLevel(medicine.getStockLevel() - quantity);
                    InventoryDAO.updateInventoryInCSV(medicines);
                    logger.log(Level.INFO, "Prescription fulfilled successfully for Medication ID: " + medicationId);
                    return true;
                } else {
                    logger.log(Level.WARNING, "Insufficient stock for Medication ID: " + medicationId);
                    return false;
                }
            }
        }
        logger.log(Level.WARNING, "Medication ID not found: " + medicationId);
        return false;
    }

    // Method to read prescriptions from CSV file
    public List<Prescription> readPrescriptionsFromCSV() {
        List<Prescription> prescriptions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(PrescriptionDAO.class.getClassLoader().getResource(FILE_PATH).getFile())))) {
            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String prescriptionId = data[0];
                String patientId = data[1];
                String medicationId = data[2];
                String status = data[3];
                prescriptions.add(new Prescription(prescriptionId, patientId, medicationId, status));
            }
        } catch (IOException | NullPointerException e) {
            logger.log(Level.SEVERE, "Error reading prescription data: {0}. Please ensure the file is in the correct location.", e.getMessage());
        }
        return prescriptions;
    }

    // Method to update prescriptions in CSV file
    public void updatePrescriptionsInCSV(List<Prescription> prescriptions) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(PrescriptionDAO.class.getClassLoader().getResource(FILE_PATH).getFile())))) {
            bw.write("PrescriptionID,PatientID,MedicationID,Status");
            bw.newLine();
            for (Prescription prescription : prescriptions) {
                bw.write(prescription.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error updating prescription data: {0}", e.getMessage());
        }
    }

    // Method to update prescription status
    public boolean updatePrescriptionStatus(String prescriptionId, String status) {
        List<Prescription> prescriptions = readPrescriptionsFromCSV();
        for (Prescription prescription : prescriptions) {
            if (prescription.getPrescriptionId().equalsIgnoreCase(prescriptionId)) {
                prescription.setStatus(status);
                updatePrescriptionsInCSV(prescriptions);
                logger.log(Level.INFO, "Prescription status updated for prescription ID: " + prescriptionId);
                return true;
            }
        }
        logger.log(Level.WARNING, "Prescription ID not found for updating status: " + prescriptionId);
        return false;
    }
}
