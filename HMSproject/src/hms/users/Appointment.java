package hms.users;

import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private int timeslot;
    private int[] dateArray; // Holds [month, day]
    private String patientName;
    private String doctorName;
    private List<String> outcomeMedications;
    private List<Float> medicationQuantity;
    private String treatmentType;
    private String consultationNotes;
    private int acceptedStatus; // 0 = pending, 1 = accepted, -1 = declined, 2 = completed

    // Basic constructor for new appointment requests
    public Appointment(int timeslot, String date, String patientName, String doctorName) {
        this.timeslot = timeslot;
        this.dateArray = dateStringToArray(date);
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.outcomeMedications = new ArrayList<>();
        this.medicationQuantity = new ArrayList<>();
        this.acceptedStatus = 0; // Default: pending
        this.treatmentType = "";
        this.consultationNotes = "Pending consultation";
    }

    // Full constructor for completed appointments
    public Appointment(int timeslot, String date, String patientName, List<String> meds, List<Float> quantities,
                       String treatmentType, String consultationNotes, String doctorName, int acceptedStatus) {
        this.timeslot = timeslot;
        this.dateArray = dateStringToArray(date);
        this.patientName = patientName;
        this.outcomeMedications = meds;
        this.medicationQuantity = quantities;
        this.treatmentType = treatmentType;
        this.consultationNotes = consultationNotes;
        this.doctorName = doctorName;
        this.acceptedStatus = acceptedStatus;
    }

    // Helper method to convert date string to int array [month, day]
    private int[] dateStringToArray(String dateString) {
        String[] parts = dateString.split("/");
        int[] dateArray = new int[2];
        dateArray[0] = Integer.parseInt(parts[0]);
        dateArray[1] = Integer.parseInt(parts[1]);
        return dateArray;
    }

    // Display appointment details
    public void showDetails() {
        System.out.println("---- Appointment Details ----");
        System.out.printf("Patient Name: %s\n", patientName);
        System.out.printf("Doctor Name: %s\n", doctorName);
        System.out.printf("Date: %d/%d\n", dateArray[0], dateArray[1]);
        System.out.printf("Timeslot: %d\n", timeslot);
        System.out.println("Status: " + getStatusDescription());

        if (acceptedStatus == 2) { // Completed
            System.out.println("Outcome Medications:");
            for (int i = 0; i < outcomeMedications.size(); i++) {
                System.out.printf("Medication: %s, Quantity: %.2f grams\n", outcomeMedications.get(i), medicationQuantity.get(i));
            }
            System.out.println("Treatment Type: " + treatmentType);
            System.out.println("Consultation Notes: " + consultationNotes);
        }
    }

    // Utility method to get human-readable status description
    public String getStatusDescription() {
        switch (acceptedStatus) {
            case 0:
                return "Pending";
            case 1:
                return "Upcoming";
            case -1:
                return "Declined";
            case 2:
                return "Completed";
            default:
                return "Unknown";
        }
    }

    // Getters and Setters
    public int getTimeslot() {
        return timeslot;
    }

    public int[] getDateArray() {
        return dateArray;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public int getAcceptedStatus() {
        return acceptedStatus;
    }

    public void setAcceptedStatus(int acceptedStatus) {
        this.acceptedStatus = acceptedStatus;
    }

    public void addMedication(String medication, float quantity) {
        outcomeMedications.add(medication);
        medicationQuantity.add(quantity);
    }

    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    public List<String> getOutcomeMedications() {
        return outcomeMedications;
    }

    public List<Float> getMedicationQuantity() {
        return medicationQuantity;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    public String getTreatmentType() {
        return treatmentType;
    }
}
