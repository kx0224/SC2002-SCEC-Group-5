package controller;

import dao.AppointmentDAO;
import dao.PatientDAO;
import java.util.List;
import model.Appointment;
import model.MedicalRecord;
import model.Patient;

public class PatientController {
    private PatientDAO patientDAO;
    private AppointmentDAO appointmentDAO;
    private MedicalRecordController medicalRecordController;
    private AppointmentController appointmentController;

    public PatientController() {
        this.patientDAO = new PatientDAO();
        this.appointmentDAO = new AppointmentDAO();
        this.medicalRecordController = new MedicalRecordController();
        this.appointmentController = new AppointmentController();
    }

    // View patient's own medical record
    public void viewMedicalRecord(Patient patient) {
        MedicalRecord record = medicalRecordController.viewMedicalRecord(patient.getUserId(), patient.getUserId());
        if (record != null) {
            System.out.println("Patient ID: " + record.getUserID());
            System.out.println("Blood Type: " + record.getBloodType());
            System.out.println("Medical History: " + (record.getMedicalHistory() != null ? String.join(", ", record.getMedicalHistory()) : "No medical history available"));
            System.out.println("Diagnosis: " + record.getDiagnosis());
            System.out.println("Treatment: " + record.getTreatment());
            System.out.println("Prescription: " + record.getPrescription());
            System.out.println("Quantity: " + record.getQuantity());
            System.out.println("Comments: " + record.getComments());
        } else {
            System.out.println("No medical record available for User ID: " + patient.getUserId());
        }
    }

    // Update patient's personal information (e.g., phone number, email address)
    public void updatePersonalInformation(Patient patient, String newPhoneNumber, String newEmailAddress) {
        patient.setPhoneNumber(newPhoneNumber);
        patient.setEmailAddress(newEmailAddress);
        boolean success = patientDAO.updatePatient(patient);
        if (success) {
            System.out.println("Personal information updated successfully.");
        } else {
            System.out.println("Error: Unable to update personal information.");
        }
    }

    // View available appointment slots
    public void viewAvailableAppointments() {
        List<String> availableSlots = appointmentController.getAvailableSlotsAcrossDoctors(null);
        if (availableSlots.isEmpty()) {
            System.out.println("No available appointment slots at the moment.");
        } else {
            System.out.println("Available Appointment Slots:");
            for (String slot : availableSlots) {
                System.out.println(slot);
            }
        }
    }

    // Schedule a new appointment
    public void scheduleAppointment(Patient patient, String doctorId, String date, String timeSlot) {
        boolean success = appointmentController.scheduleAppointment(doctorId, patient.getUserId(), date, timeSlot, "patient");
        if (success) {
            System.out.println("Appointment successfully scheduled.");
        } else {
            System.out.println("Error: Unable to schedule appointment. Please try another slot.");
        }
    }

    // Reschedule an existing appointment
    public void rescheduleAppointment(Patient patient, String appointmentId, String newDate, String newTimeSlot) {
        boolean success = appointmentController.rescheduleAppointment(appointmentId, newDate, newTimeSlot, "patient");
        if (success) {
            System.out.println("Appointment successfully rescheduled.");
        } else {
            System.out.println("Error: Unable to reschedule appointment. Please try another slot.");
        }
    }

    // Cancel an existing appointment
    public void cancelAppointment(Patient patient, String appointmentId) {
        boolean success = appointmentController.cancelAppointment(appointmentId, "patient");
        if (success) {
            System.out.println("Appointment successfully canceled.");
        } else {
            System.out.println("Error: Unable to cancel appointment.");
        }
    }

    // View patient's scheduled appointments
    public void viewScheduledAppointments(Patient patient) {
        List<Appointment> scheduledAppointments = appointmentController.getScheduledAppointmentsByPatient(patient.getUserId());
        if (scheduledAppointments.isEmpty()) {
            System.out.println("No scheduled appointments.");
        } else {
            System.out.println("Scheduled Appointments:");
            for (Appointment appointment : scheduledAppointments) {
                System.out.println("Appointment ID: " + appointment.getAppointmentId() + ", Doctor ID: " + appointment.getDoctorId() + ", Date: " + appointment.getDate() + ", Time Slot: " + appointment.getTimeSlot() + ", Status: " + appointment.getStatus());
            }
        }
    }

    // View patient's past appointment outcomes
    public void viewPastAppointmentOutcomes(Patient patient) {
        List<Appointment> pastAppointments = appointmentController.getPastAppointmentOutcomesByPatient(patient.getUserId());
        if (pastAppointments.isEmpty()) {
            System.out.println("No past appointment records available.");
        } else {
            System.out.println("Past Appointment Outcomes:");
            for (Appointment appointment : pastAppointments) {
                System.out.println("Appointment ID: " + appointment.getAppointmentId() + ", Outcome: " + (appointment.getOutcome() != null ? appointment.getOutcome().getConsultationNotes() : "No outcome available"));
            }
        }
    }
}