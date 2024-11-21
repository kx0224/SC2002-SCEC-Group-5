package controllers;

import java.util.HashMap;
import java.util.Map;

import models.MedicalRecords;
import models.PrescribedMed;

public class medicalRecordsController {
    private Map<String, MedicalRecords> medicalRecords; // Map to store medical records by patient ID

    public medicalRecordsController() {
        this.medicalRecords = new HashMap<>();
    }

    // Method to retrieve a medical record for a specific patient
    public MedicalRecords getMedicalRecord(String patientID) {
        return medicalRecords.get(patientID);
    }

    // Method to update a medical record for a specific patient with diagnosis, prescription, and treatment
    public void updateMedicalRecord(String patientID, String diagnosis, PrescribedMed prescription, String treatment) {
        MedicalRecords record = medicalRecords.get(patientID);
        if (record != null) {
            if (diagnosis != null && !diagnosis.isEmpty()) {
                record.addDiagnosis(diagnosis);
            }
            if (prescription != null) {
                record.addPrescribedMedication(prescription);
            }
            if (treatment != null && !treatment.isEmpty()) {
                record.addTreatment(treatment);
            }
            medicalRecords.put(patientID,record);
            System.out.println("Medical record updated for patient ID: " + patientID);
        } else {
            System.out.println("Medical record not found for patient ID: " + patientID);
        }
    }

     // Method to add a new medical record
    public void addMedicalRecord(String patientID, MedicalRecords record, boolean display) {
        if (medicalRecords.containsKey(patientID)) {
            System.out.println("A medical record already exists for patient ID: " + patientID);
        } else {
            medicalRecords.put(patientID, record);
            if (display){
            System.out.println("Medical record added for patient ID: " + patientID);}
        }
    }
}
