package model;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecords {
    private String patientID;
    private String patientName;
    private String bloodType;
    private List<String> diagnoses;
    private List<PrescribedMed> prescribedMedications;
    private List<String> treatments;

    // Constructor
    public MedicalRecords(String patientID, String patientName, String bloodType) {
        this.patientID = patientID;
        this.patientName = patientName;
        this.bloodType = bloodType;
        this.diagnoses = new ArrayList<>();
        this.prescribedMedications = new ArrayList<>();
        this.treatments = new ArrayList<>();
    }

    // Add Diagnosis
    public void addDiagnosis(String diagnosis) {
        if (diagnosis != null && !diagnosis.isEmpty()) {
            diagnoses.add(diagnosis);
        }
    }

    // Add Prescribed Medication
    public void addPrescribedMedication(PrescribedMed medication) {
        if (medication == null) {
            return;
        }
        boolean exists = prescribedMedications.stream()
                .anyMatch(med -> med.getMedname().equalsIgnoreCase(medication.getMedname()));

        if (!exists) {
            prescribedMedications.add(medication);
        } else {
            System.out.println("Medication " + medication.getMedname() + " is already in the prescribed list.");
        }
    }

    // Add Treatment
    public void addTreatment(String treatment) {
        if (treatment != null && !treatment.isEmpty()) {
            treatments.add(treatment);
        }
    }

    // Getters
    public String getPatientID() {
        return patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getBloodType() {
        return bloodType;
    }

    public List<String> getDiagnoses() {
        return new ArrayList<>(diagnoses);
    }

    public List<PrescribedMed> getPrescriptions() {
        return new ArrayList<>(prescribedMedications);
    }

    public List<String> getTreatments() {
        return new ArrayList<>(treatments);
    }
}
