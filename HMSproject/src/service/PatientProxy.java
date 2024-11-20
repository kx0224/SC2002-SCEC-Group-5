package service;

import model.MedicalRecords;
import model.PrescribedMed;
import roles.Patient;
import model.PatientInterface;
import java.util.List;

public class PatientProxy implements PatientInterface {

    // Fields for medical records and patient information
    private MedicalRecords medicalRecords;
    private Patient patient;

    // Constructor
    public PatientProxy(MedicalRecords medicalRecords, Patient patient) {
        this.medicalRecords = medicalRecords;
        this.patient = patient;
    }

    // Implementation of PatientInterface methods

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

    @Override
    public String getEmail() {
       return patient.getEmail();
    }

    // Methods to interact with medical records

    @Override
    public String getBloodType() {
        return medicalRecords.getBloodType();
    }

    public List<String> getPastDiagnoses() {
        return medicalRecords.getDiagnoses(); // Assuming medicalRecords has a `getDiagnoses` method
    }

    public List<PrescribedMed> getPrescription() {
        return medicalRecords.getPrescriptions();
    }

    public List<String> getTreatment() {
        return medicalRecords.getTreatments();
    }

    // Setters for updating patient information

    public void setPhoneNumber(String phoneNumber) {
        patient.setPhonenumber(phoneNumber);
    }

    // Add more methods if needed to interact with medical records or patient information
}
