package domain;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecords {
    public String patientID;
//    public String doctorID;
    public String bloodType;
    public String patientName;
    public List<String> diagnoses;
//    private List<Prescription> prescriptions;
    public List<String> treatments;
//    private List<AppointmentOutcome> appointmentOutcomes;
    private List<PrescribedMed> prescribedMedications;

    public MedicalRecords(String patientID, String patientName, String bloodType) {
        this.patientID = patientID;
        this.patientName = patientName;
//        this.doctorID = doctorID;
        this.bloodType = bloodType;
//        this.appointmentOutcomes = new ArrayList<>();
        this.diagnoses = new ArrayList<>();
        this.prescribedMedications = new ArrayList<>();
        this.treatments = new ArrayList<>();
    }

    // Getters and setters for medical record fields
    public void addDiagnosis(String diagnosis) {
        diagnoses.add(diagnosis);
    }


    public void addPrescribedMedication(PrescribedMed medication) {
        // Check if the medication already exists in the prescribed list
        boolean exists = prescribedMedications.stream()
                .anyMatch(med -> med.getMedname().equalsIgnoreCase(medication.getMedname()));

        if (!exists) {
            prescribedMedications.add(medication);
        } else {
            System.out.println("Medication " + medication.getMedname() + " is already in the prescribed list.");
        }
    }

    public void addTreatment(String treatment) {
        treatments.add(treatment);
    }

    public List<String> getDiagnoses() {
        return diagnoses;
    }

    public List<PrescribedMed> getPrescriptions() {
        return prescribedMedications;
    }

    public List<String> getTreatments() {
        return treatments;
    }


    public String getPatientID(){ return patientID;}
    public String getPatientName(){ return patientName;}
    public String getBloodType(){ return bloodType;}
}
