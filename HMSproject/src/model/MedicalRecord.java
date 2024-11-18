package model;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    private String userID;
    private String bloodType;
    private List<String> medicalHistory;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private int quantity;
    private String comments;

    // Constructor with all fields
    public MedicalRecord(String userID, String bloodType, List<String> medicalHistory, String diagnosis, String treatment, String prescription, int quantity, String comments) {
        this.userID = userID;
        this.bloodType = bloodType;
        this.medicalHistory = new ArrayList<>(medicalHistory);
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.prescription = prescription;
        this.quantity = quantity;
        this.comments = comments;
    }

    // Getters and Setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public List<String> getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(List<String> medicalHistory) {
        this.medicalHistory = new ArrayList<>(medicalHistory);
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    // Method to add a medical history entry
    public void addMedicalHistoryEntry(String historyEntry) {
        medicalHistory.add(historyEntry);
    }

    // Method to get a patient-friendly summary of the medical record
    public String getPatientViewSummary() {
        return "User ID: " + userID + "\n" +
               "Blood Type: " + bloodType + "\n" +
               "Medical History: " + (medicalHistory.isEmpty() ? "No medical history available" : String.join(", ", medicalHistory));
    }

    // Method to get a detailed summary of the medical record
    public String getSummary() {
        return "User ID: " + userID + "\n" +
                "Blood Type: " + bloodType + "\n" +
                "Medical History: " + (medicalHistory.isEmpty() ? "No medical history available" : String.join(", ", medicalHistory)) + "\n" +
                "Diagnosis: " + diagnosis + "\n" +
                "Treatment: " + treatment + "\n" +
                "Prescription: " + prescription + "\n" +
                "Quantity: " + quantity + "\n" +
                "Comments: " + comments;
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
