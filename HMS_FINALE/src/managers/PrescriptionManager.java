package managers;
import java.util.*;

import models.Inventory;
import models.PrescribedMed;
import models.prescriptionStatus;

import java.io.File;
import java.io.FileNotFoundException;



public class PrescriptionManager {
    private Map<String, PrescribedMed> prescriptionMap;
    private Inventory inventory;

    // Constructor
    public PrescriptionManager(Inventory inventory) {
        this.prescriptionMap = new HashMap<>();
        this.inventory = inventory;
        initializeData();
    }
    private void initializeData() {
        File inventoryFile = new File("data/prescription.csv");
        try (Scanner sc = new Scanner(inventoryFile)) {
            sc.nextLine(); // Skip header line
            while (sc.hasNextLine()) {
                String[] itemData = sc.nextLine().split(",");
                if (itemData.length >= 4) { // Ensure there are at least 4 fields
                    String prescriptionID = itemData[0].trim();
                    String appointmentID = itemData[1].trim();
                    String medID = itemData[2].trim();
                    int quantity = Integer.parseInt(itemData[3].trim());
                    prescriptionStatus status = prescriptionStatus.valueOf(itemData[4].trim().toUpperCase());

                    // Add new item to inventory
                    PrescribedMed prescribedMed = new PrescribedMed(medID,quantity);
                    String medname = inventory.getMedicationNameByID(medID);
                    if (medname != null) {
                        prescribedMed.setmedName(medname);
                    } else {
                        System.out.println("Failed to set medication name. Medication with ID " + medID + " was not found.");
                        continue; // Skip adding this prescription if the medication is not found
                    }

                    prescribedMed.setPrescription_id(prescriptionID);
                    prescribedMed.setAppointment_id(appointmentID);
                    prescribedMed.setStatus(status);
                    prescribedMed.setmedName(medname);
                    prescriptionMap.put(prescriptionID,prescribedMed);
//                    System.out.println("Loaded medication ID: "+ medID + " with quantity "+prescribedMed.getQuantity());
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
        // Add the prescription to the map if it does not already exist
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
    public List<PrescribedMed> getPrescriptionsByApptID(String ApptID) {
        List<PrescribedMed> prescriptionsByApptID = new ArrayList<>();
        for (PrescribedMed prescription : prescriptionMap.values()) {
            if (prescription.getAppointment_id().equals(ApptID)) {
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
