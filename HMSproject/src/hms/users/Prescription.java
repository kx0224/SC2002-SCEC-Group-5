package hms.users;

import java.util.List;

public class Prescription {
    private String patientID;
    private String doctorID;
    private List<String> medications;
    private List<Float> quantities;
    private String status; // e.g., "Pending", "Fulfilled"

    // Constructor
    public Prescription(String patientID, String doctorID, List<String> medications, List<Float> quantities) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.medications = medications;
        this.quantities = quantities;
        this.status = "Pending"; // Default status
    }

    // Method to display prescription details
    public void showDetails() {
        System.out.println("---- Prescription Details ----");
        System.out.println("Patient ID: " + patientID);
        System.out.println("Prescribed by Doctor ID: " + doctorID);
        System.out.println("Status: " + status);
        System.out.println("Medications:");
        for (int i = 0; i < medications.size(); i++) {
            System.out.printf("Medication: %s, Quantity: %.2f grams\n", medications.get(i), quantities.get(i));
        }
    }

    // Getters
    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public List<String> getMedications() {
        return medications;
    }

    public List<Float> getQuantities() {
        return quantities;
    }

    public String getStatus() {
        return status;
    }

    // Setter for updating prescription status
    public void setStatus(String status) {
        this.status = status;
    }
}
