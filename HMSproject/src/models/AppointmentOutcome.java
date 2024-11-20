package models;

import java.util.ArrayList;
import java.util.List;

public class AppointmentOutcome {
    private String patientId;
    private String doctorId;
    private String consultationNotes;
    private List<String> appointmentMedications;
    private String appointmentId;
    private String appointmentOutcome;
    private String serviceType;
    private String outcome;

    // Constructor
    public AppointmentOutcome(String patientId, String doctorId, String appointmentId) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.consultationNotes = "";
        this.appointmentMedications = new ArrayList<>();
        this.appointmentOutcome = "";
        this.serviceType = "";
        this.outcome = "";
    }

    // Add a prescription to the appointment
    public void addPrescription(String prescription) {
        if (prescription != null && !prescription.isEmpty()) {
            appointmentMedications.add(prescription);
        }
    }

    // Get list of prescriptions
    public List<String> getPrescriptions() {
        return new ArrayList<>(appointmentMedications);
    }

    // Getters and setters
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentOutcome() {
        return appointmentOutcome;
    }

    public void setAppointmentOutcome(String appointmentOutcome) {
        this.appointmentOutcome = appointmentOutcome;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
