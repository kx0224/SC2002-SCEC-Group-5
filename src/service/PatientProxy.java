package service;

import users.Patient;
import users.IPatientInfo;
import java.util.List;

import models.MedicalRecords;
import models.PrescribedMed;

public class PatientProxy implements IPatientInfo {
    private MedicalRecords medicalRecords;
    private Patient patient;

    public PatientProxy(MedicalRecords medicalRecords, Patient patient) {
        this.medicalRecords = medicalRecords;
        this.patient = patient;
    }



    @Override
    public String getPatientID() {
        return patient.getHospitalID(); // Assuming Patient has `getHospitalID()`
    }

    @Override
    public String getDateOfBirth() {
        return patient.getDateOfBirth(); // Ensure that the Patient class has this method
    }

    @Override
    public String getGender() {
        return patient.getGender(); // Assuming this returns "Male" or "Female"
    }

    @Override
    public String getPhoneNumber() {
        return patient.getPhonenumber();
    }
    @Override
    public String getPatientName() {
        return patient.getPatientName();
    }

    // Getters for medical records
    @Override
    public String getBloodType() {
        return medicalRecords.getBloodType();
    }

    // Assuming medicalRecords has a `getDiagnoses` method
    public List<String> getPastDiagnoses() {
        return medicalRecords.getDiagnoses();
    }

    public List<PrescribedMed> getPrescription() {
        return medicalRecords.getPrescriptions();
    }

    public List<String> getTreatment() {
        return medicalRecords.getTreatments();
    }

    // Setters for updating patient information
    @Override
    public String getEmail() {
       return patient.getEmail();
    }

    public void setPhoneNumber(String phoneNumber) {
        patient.setPhonenumber(phoneNumber);
    }

    // Add more methods if needed to interact with medical records
}
