package model;

import java.util.List;

public class Appointment {
    public enum AppointmentStatus {
        PENDING,
        CONFIRMED,
        SCHEDULED,
        CANCELED,
        COMPLETED
    }

    public static class AppointmentOutcome {
        private String serviceProvided;
        private List<String> prescribedMedications;
        private String consultationNotes;

        // Constructor
        public AppointmentOutcome(String serviceProvided, List<String> prescribedMedications, String consultationNotes) {
            this.serviceProvided = serviceProvided;
            this.prescribedMedications = prescribedMedications;
            this.consultationNotes = consultationNotes;
        }

        // Getters and setters
        public String getServiceProvided() {
            return serviceProvided;
        }

        public void setServiceProvided(String serviceProvided) {
            this.serviceProvided = serviceProvided;
        }

        public List<String> getPrescribedMedications() {
            return prescribedMedications;
        }

        public void setPrescribedMedications(List<String> prescribedMedications) {
            this.prescribedMedications = prescribedMedications;
        }

        public String getConsultationNotes() {
            return consultationNotes;
        }

        public void setConsultationNotes(String consultationNotes) {
            this.consultationNotes = consultationNotes;
        }
    }

    private String appointmentId;
    private String doctorId;
    private String patientId;
    private String date;
    private String timeSlot;
    private AppointmentStatus status;
    private boolean isAvailable;
    private AppointmentOutcome outcome;

    // Constructor
    public Appointment(String appointmentId, String doctorId, String patientId, String date, String timeSlot, AppointmentStatus status, boolean isAvailable) {
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
        this.isAvailable = isAvailable;
        this.outcome = null;
    }

    // Overloaded constructor with outcome parameter
    public Appointment(String appointmentId, String doctorId, String patientId, String date, String timeSlot, AppointmentStatus status, boolean isAvailable, AppointmentOutcome outcome) {
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
        this.isAvailable = isAvailable;
        this.outcome = outcome;
    }

    // Getters and setters
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        if (isValidStatusTransition(this.status, status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid status transition from " + this.status + " to " + status);
        }
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public AppointmentOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(AppointmentOutcome outcome) {
        if (this.status != AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot set outcome for an appointment that is not completed");
        }
        this.outcome = outcome;
    }

    // Method to convert appointment to CSV format
    public String toCSV() {
        String outcomeString = (outcome == null) ? "" : "\"" + outcome.getServiceProvided() + "\",\"" + String.join(",", outcome.getPrescribedMedications()) + "\",\"" + outcome.getConsultationNotes() + "\"";
        return String.join(",", appointmentId, doctorId, patientId, date, timeSlot, status.name(), String.valueOf(isAvailable), outcomeString);
    }

    // Method to generate a unique appointment ID
    public static String generateUniqueAppointmentId(int currentSize) {
        return "APT" + (currentSize + 1);
    }

    // Utility method to validate status transitions
    private boolean isValidStatusTransition(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        return switch (currentStatus) {
            case PENDING -> newStatus == AppointmentStatus.CONFIRMED || newStatus == AppointmentStatus.CANCELED;
            case CONFIRMED -> newStatus == AppointmentStatus.SCHEDULED || newStatus == AppointmentStatus.CANCELED;
            case SCHEDULED -> newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.CANCELED;
            case CANCELED -> false;
            case COMPLETED -> false;
            default -> false;
        };
    }

    public String getOutcomeSummary() {
        if (outcome == null) {
            return "No outcome available for this appointment.";
        }
        StringBuilder summary = new StringBuilder();
        summary.append("Service Provided: ").append(outcome.getServiceProvided()).append("\n");
        summary.append("Prescribed Medications: ").append(String.join(", ", outcome.getPrescribedMedications())).append("\n");
        summary.append("Consultation Notes: ").append(outcome.getConsultationNotes()).append("\n");
        return summary.toString();
    }
}
