package controller;

import dao.AppointmentDAO;
import dao.PatientDAO;
import java.util.InputMismatchException;
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
        goBackToMainMenu();
    }

    // Update patient's personal information (phone number or email address)
    public void updatePersonalInformation() {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        boolean validChoice = false;

        while (!validChoice) {
            System.out.println("What information would you like to update?");
            System.out.println("1. Phone Number");
            System.out.println("2. Email Address");
            System.out.println("3. Go back to main menu");
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (choice >= 1 && choice <= 3) {
                    validChoice = true;
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                scanner.nextLine(); // Consume invalid input
            }
        }

        switch (choice) {
            case 1:
                boolean validPhoneNumber = false;
                String phoneNumber = "";
                while (!validPhoneNumber) {
                    System.out.print("Enter new phone number (8 digits): ");
                    phoneNumber = scanner.nextLine();
                    if (User.validatePhoneNumber(phoneNumber)) {
                        validPhoneNumber = true;
                    } else {
                        System.err.println("Invalid phone number format. Please enter exactly 8 digits.");
                        System.out.print("Would you like to try again or go back to the main menu? (try again: T / main menu: M): ");
                        String response = scanner.nextLine();
                        if (response.equalsIgnoreCase("M")) {
                            return;
                        }
                    }
                }
                System.out.println("Are you sure you want to update your phone number? (Y/N)");
                if (!scanner.nextLine().equalsIgnoreCase("Y")) {
                    System.out.println("Update canceled by user.");
                    goBackToMainMenu();
                    return;
                }
                patient.setPhoneNumber(phoneNumber);
                break;
            case 2:
                boolean validEmail = false;
                String emailAddress = "";
                while (!validEmail) {
                    System.out.print("Enter new email address: ");
                    emailAddress = scanner.nextLine();
                    if (User.validateEmailAddress(emailAddress)) {
                        validEmail = true;
                    } else {
                        System.err.println("Invalid email address format. Please enter a valid email (e.g., example@example.com).\n");
                        System.out.print("Would you like to try again or go back to the main menu? (try again: T / main menu: M): ");
                        String response = scanner.nextLine();
                        if (response.equalsIgnoreCase("M")) {
                            goBackToMainMenu();
                            return;
                        }
                    }
                }
                System.out.println("Are you sure you want to update your email address? (Y/N)");
                if (!scanner.nextLine().equalsIgnoreCase("Y")) {
                    System.out.println("Update canceled by user.");
                    goBackToMainMenu();
                    return;
                }
                patient.setEmailAddress(emailAddress);
                break;
            case 3:
                return;
            default:
                System.err.println("Invalid choice. Update aborted.");
                goBackToMainMenu();
                return;
        }
        boolean success = patientDAO.updatePatient(patient);
        printResult(success, "Personal information updated successfully.", "Error: Unable to update personal information.");
        goBackToMainMenu();
    }

    // Utility method to print result messages
    private void printResult(boolean success, String successMessage, String errorMessage) {
        if (success) {
            System.out.println(successMessage);
        } else {
            System.out.println(errorMessage);
        }
    }

    // View available appointment slots and schedule an appointment
    public void viewAndScheduleAppointments() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the date for which you want to view available slots (yyyy-MM-dd): ");
        String date = scanner.nextLine();
        List<String> availableSlots = appointmentController.getAvailableSlotsAcrossDoctors(date);
        if (availableSlots.isEmpty()) {
            System.out.println("No available appointment slots on " + date);
        } else {
            System.out.println("Available Appointment Slots on " + date + ":");
            availableSlots.forEach(System.out::println);
            System.out.print("\nWould you like to schedule an appointment? (Y/N): ");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("Y")) {
                System.out.print("Enter Doctor ID: ");
                String doctorId = scanner.nextLine();
                System.out.print("Enter the time slot for the appointment (e.g., 10:00 AM): ");
                String timeSlot = scanner.nextLine();
                boolean success = appointmentController.scheduleAppointment(doctorId, patient.getUserId(), date, timeSlot, "patient");
                printResult(success, "Appointment successfully scheduled.", "Error: Unable to schedule appointment. Please try another slot.");
            }
        }
        goBackToMainMenu();
    }

    // Reschedule an existing appointment
    public void rescheduleAppointment() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Appointment ID to reschedule: ");
        String appointmentId = scanner.nextLine();
        System.out.print("Enter the new date for the appointment (yyyy-MM-dd): ");
        String newDate = scanner.nextLine();
        System.out.print("Enter the new time slot for the appointment (e.g., 10:00 AM): ");
        String newTimeSlot = scanner.nextLine();

        boolean success = appointmentController.rescheduleAppointment(appointmentId, newDate, newTimeSlot, "patient");
        printResult(success, "Appointment successfully rescheduled.", "Error: Unable to reschedule appointment. Please try another slot.");
        goBackToMainMenu();
    }

    // Cancel an existing appointment
    public void cancelAppointment() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Appointment ID to cancel: ");
        String appointmentId = scanner.nextLine();

        boolean success = appointmentController.cancelAppointment(appointmentId, "patient");
        printResult(success, "Appointment successfully canceled.", "Error: Unable to cancel appointment.");
        goBackToMainMenu();
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
        goBackToMainMenu();
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
        goBackToMainMenu();
    }

    // Utility method to provide option to return to the main menu
    private void goBackToMainMenu() {
        System.out.println("\nPress 'M' to return to the main menu.");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();
        while (!response.equalsIgnoreCase("M")) {
            System.out.println("Invalid input. Press 'M' to return to the main menu.");
            response = scanner.nextLine();
        }
    }
}
