package controller;

import dao.AppointmentDAO;
import dao.PatientDAO;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import model.Appointment;
import model.MedicalRecord;
import model.Patient;
import model.User;

public class PatientController {

    private PatientDAO patientDAO;
    private AppointmentDAO appointmentDAO;
    private MedicalRecordController medicalRecordController;
    private AppointmentController appointmentController;
    private Patient patient;

    public PatientController(Patient patient, PatientDAO patientDAO, AppointmentDAO appointmentDAO, MedicalRecordController medicalRecordController, AppointmentController appointmentController) {
        this.patient = patient;
        this.patientDAO = patientDAO;
        this.appointmentDAO = appointmentDAO;
        this.medicalRecordController = medicalRecordController;
        this.appointmentController = appointmentController;
    }

    public Patient getPatient() {
        return patient;
    }
    
    // View patient's own medical record
    public void viewMedicalRecord() {
        // Fetch the latest patient data from PatientDAO
        Patient latestPatient = patientDAO.getPatientById(patient.getUserId());
        MedicalRecord record = medicalRecordController.viewMedicalRecord(latestPatient.getUserId(), latestPatient.getUserId());
        Optional.ofNullable(record).ifPresentOrElse(r -> {
            System.out.println("Patient ID: " + latestPatient.getUserId());
            System.out.println("Name: " + latestPatient.getName());
            System.out.println("Date of Birth: " + latestPatient.getDateOfBirth());
            System.out.println("Gender: " + latestPatient.getGender());
            System.out.println("Contact Information: Phone - " + latestPatient.getPhoneNumber() + ", Email - " + latestPatient.getEmailAddress());
            System.out.println("Blood Type: " + r.getBloodType());
            System.out.println("Medical History: " + (r.getMedicalHistory() != null ? String.join(", ", r.getMedicalHistory()) : "No medical history available"));
            System.out.println("Diagnosis: " + r.getDiagnosis());
            System.out.println("Treatment: " + r.getTreatment());
            System.out.println("Prescription: " + r.getPrescription());
            System.out.println("Quantity: " + r.getQuantity());
            System.out.println("Comments: " + r.getComments());
        }, () -> System.out.println("No medical record available for User ID: " + latestPatient.getUserId()));
        
        // Provide option to go back to main menu
        System.out.println("\nPress 'M' to return to the main menu.");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();
        while (!response.equalsIgnoreCase("M")) {
            System.out.println("Invalid input. Press 'M' to return to the main menu.");
            response = scanner.nextLine();
        }
    }

    // Update patient's personal information (phone number or email address)
    public void updatePersonalInformation(int choice, String value) {
        Scanner scanner = new Scanner(System.in);
        switch (choice) {
            case 1:
                boolean validPhoneNumber = false;
                while (!validPhoneNumber) {
                    if (User.validatePhoneNumber(value)) {
                        validPhoneNumber = true;
                        break;  // Exit loop since the phone number is now valid
                    }
                    System.err.println("Invalid phone number format. Please enter exactly 8 digits.");
                    System.out.print("Enter new phone number: ");
                    value = scanner.nextLine();
                    if (User.validatePhoneNumber(value)) {
                        validPhoneNumber = true;
                        break;
                    }
                    System.out.print("Would you like to try again or go back to the main menu? (try again: T / main menu: M): ");
                    String response = scanner.nextLine();
                    if (response.equalsIgnoreCase("M")) {
                        return;
                    }
                }
                System.out.println("Are you sure you want to update your phone number? (Y/N)");
                if (!scanner.nextLine().equalsIgnoreCase("Y")) {
                    System.out.println("Update canceled by user.");
                    return;
                }
                patient.setPhoneNumber(value);
                break;
            case 2:
                boolean validEmail = false;
                while (!validEmail) {
                    if (User.validateEmailAddress(value)) {
                        validEmail = true;
                        break;  // Exit loop since the email address is now valid
                    }
                    System.err.println("Invalid email address format. Please enter a valid email (e.g., example@example.com).");
                    System.out.print("Enter new email address: ");
                    value = scanner.nextLine();
                    if (User.validateEmailAddress(value)) {
                        validEmail = true;
                        break;
                    }
                    System.out.print("Would you like to try again or go back to the main menu? (try again: T / main menu: M): ");
                    String response = scanner.nextLine();
                    if (response.equalsIgnoreCase("M")) {
                        return;
                    }
                }
                System.out.println("Are you sure you want to update your email address? (Y/N)");
                if (!scanner.nextLine().equalsIgnoreCase("Y")) {
                    System.out.println("Update canceled by user.");
                    return;
                }
                patient.setEmailAddress(value);
                break;
            default:
                System.err.println("Invalid choice. Update aborted.");
                return;
        }
        boolean success = patientDAO.updatePatient(patient);
        printResult(success, "Personal information updated successfully.", "Error: Unable to update personal information.");
    }

    // Utility method to print result messages
    private void printResult(boolean success, String successMessage, String errorMessage) {
        if (success) {
            System.out.println(successMessage);
        } else {
            System.out.println(errorMessage);
        }
    }

    // View available appointment slots
    public void viewAvailableAppointments() {
        appointmentController.viewAvailableAppointments();
    }

    // Schedule a new appointment
    public void scheduleAppointment(String doctorId, String date, String timeSlot) {
        boolean success = appointmentController.scheduleAppointment(doctorId, patient.getUserId(), date, timeSlot, "patient");
        printResult(success, "Appointment successfully scheduled.", "Error: Unable to schedule appointment. Please try another slot.");
    }

    // Reschedule an existing appointment
    public void rescheduleAppointment(String appointmentId, String newDate, String newTimeSlot) {
        boolean success = appointmentController.rescheduleAppointment(appointmentId, newDate, newTimeSlot, "patient");
        printResult(success, "Appointment successfully rescheduled.", "Error: Unable to reschedule appointment. Please try another slot.");
    }

    // Cancel an existing appointment
    public void cancelAppointment(String appointmentId) {
        boolean success = appointmentController.cancelAppointment(appointmentId, "patient");
        printResult(success, "Appointment successfully canceled.", "Error: Unable to cancel appointment.");
    }

    // View patient's scheduled appointments
    public void viewScheduledAppointments() {
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
    public void viewPastAppointmentOutcomes() {
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
