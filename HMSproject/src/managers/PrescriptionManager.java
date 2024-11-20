package managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import models.*;

public class PrescriptionManager {
    private Map<String, PrescribedMed> prescriptionMap;
    private Inventory inventory;

    // Constructor
    public PrescriptionManager(Inventory inventory) {
        this.prescriptionMap = new HashMap<>();
        this.inventory = inventory;
        initializeData();
    }

    // Initialize prescription data from CSV file
    private void initializeData() {
        File inventoryFile = new File("data/prescription.csv");
        try (Scanner sc = new Scanner(inventoryFile)) {
            sc.nextLine(); // Skip header line
            while (sc.hasNextLine()) {
                String[] itemData = sc.nextLine().split(",");
                if (itemData.length >= 4) {
                    String prescriptionID = itemData[0].trim();
                    String appointmentID = itemData[1].trim();
                    String medID = itemData[2].trim();
                    int quantity = Integer.parseInt(itemData[3].trim());
                    prescriptionStatus status = prescriptionStatus.valueOf(itemData[4].trim().toUpperCase());

                    PrescribedMed prescribedMed = new PrescribedMed(medID, quantity);
                    String medName = inventory.getMedicationNameByID(medID);
                    if (!medName.equals("Medication not found")) {
                        prescribedMed.setmedName(medName);
                    } else {
                        System.out.println("Failed to set medication name: " + medName);
                    }

                    prescribedMed.setPrescription_id(prescriptionID);
                    prescribedMed.setAppointment_id(appointmentID);
                    prescribedMed.setStatus(status);
                    prescriptionMap.put(prescriptionID, prescribedMed);
                } else {
                    System.out.println("Invalid line format, skipping...");
                }
            }
            System.out.println("Prescriptions initialized successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Prescription data file not found!");
        } catch (NumberFormatException e) {
            System.out.println("Error parsing prescription data. Please check the format of numeric values.");
        }
    }

    // Method to add a prescription to the map
    public void addPrescription(PrescribedMed prescription) {
        if (!prescriptionMap.containsKey(prescription.getPrescription_id())) {
            prescriptionMap.put(prescription.getPrescription_id(), prescription);
            System.out.println("Prescription added with ID: " + prescription.getPrescription_id());
        } else {
            System.out.println("Prescription with ID " + prescription.getPrescription_id() + " already exists.");
        }
    }

    // Method to retrieve a prescription by ID
    public PrescribedMed getPrescription(String prescriptionId) {
        return prescriptionMap.get(prescriptionId);
    }

    // Method to retrieve prescriptions by appointment ID
    public List<PrescribedMed> getPrescriptionsByApptID(String apptID) {
        List<PrescribedMed> prescriptionsByApptID = new ArrayList<>();
        for (PrescribedMed prescription : prescriptionMap.values()) {
            if (prescription.getAppointment_id().equals(apptID)) {
                prescriptionsByApptID.add(prescription);
            }
        }
        return prescriptionsByApptID;
    }

    // Method to display all prescriptions
    public Map<String, PrescribedMed> getAllPrescriptions() {
        return prescriptionMap;
    }
}
