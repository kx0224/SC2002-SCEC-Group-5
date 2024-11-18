package model;

import controller.AppointmentController;
import controller.MedicalRecordController;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Appointment.AppointmentStatus;

public class Doctor extends Staff {
    private static final Logger logger = Logger.getLogger(Doctor.class.getName());
    private final AppointmentController appointmentController;
    private final MedicalRecordController medicalRecordController;
    private static final Scanner scanner = new Scanner(System.in); // Reuse Scanner instance to prevent resource leak

    // Constructor with Dependency Injection for Controllers
    public Doctor(String userID, String name, String role, String department, String gender, int age, String contactNumber, String emailAddress, String address, String shift, String emergencyContact, String password,
                  AppointmentController appointmentController, MedicalRecordController medicalRecordController) {
        super(userID, name, role, department, gender, age, contactNumber, emailAddress, address, shift, emergencyContact, password);
        this.appointmentController = appointmentController;
        this.medicalRecordController = medicalRecordController;
    }

    // Method to view patient medical records
    public void viewPatientMedicalRecords() {
        System.out.print("Enter Patient ID to view medical records: ");
        String patientId = scanner.nextLine().trim();
        if (patientId.isEmpty()) {
            System.out.println("Invalid input. Patient ID cannot be empty.");
            return;
        }
        MedicalRecord record = medicalRecordController.viewMedicalRecord(getUserID(), patientId);
        if (record == null) {
            System.out.println("No medical records found for Patient ID: " + patientId);
        } else {
            System.out.println("Medical Record for Patient ID: " + patientId);
            System.out.println(record.getSummary());
        }
    }

    // Method to update patient medical records
    public void updatePatientMedicalRecords() {
        System.out.print("Enter Patient ID to update medical records: ");
        String patientId = scanner.nextLine().trim();
        if (patientId.isEmpty()) {
            System.out.println("Invalid input. Patient ID cannot be empty.");
            return;
        }

        MedicalRecord record = medicalRecordController.viewMedicalRecord(getUserID(), patientId);
        if (record == null) {
            System.out.println("No medical records found for Patient ID: " + patientId);
            return;
        }

        System.out.print("Enter diagnosis (or leave blank to skip): ");
        String diagnosis = scanner.nextLine().trim();
        if (!diagnosis.isEmpty()) {
            record.setDiagnosis(diagnosis);
        }

        System.out.print("Enter treatment (or leave blank to skip): ");
        String treatment = scanner.nextLine().trim();
        if (!treatment.isEmpty()) {
            record.setTreatment(treatment);
        }

        System.out.print("Enter prescription (or leave blank to skip): ");
        String prescription = scanner.nextLine().trim();
        if (!prescription.isEmpty()) {
            record.setPrescription(prescription);
        }

        System.out.print("Enter quantity (or leave blank to skip): ");
        String quantityInput = scanner.nextLine().trim();
        if (!quantityInput.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityInput);
                if (quantity > 0) {
                    record.setQuantity(quantity);
                } else {
                    System.out.println("Invalid input. Quantity must be greater than zero.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Quantity must be a positive integer.");
            }
        }

        System.out.print("Enter comments (or leave blank to skip): ");
        String comments = scanner.nextLine().trim();
        if (!comments.isEmpty()) {
            record.setComments(comments);
        }

        medicalRecordController.updateMedicalRecord(getUserID(), record);
        System.out.println("Medical record updated successfully for Patient ID: " + patientId);
    }

    // Method to view personal schedule
    public void viewPersonalSchedule() {
        List<Appointment> schedule = appointmentController.getAppointmentsByDoctor(getName());
        if (schedule.isEmpty()) {
            System.out.println("No upcoming appointments found.");
        } else {
            System.out.println("Upcoming Appointments for Dr. " + getName() + ":");
            for (Appointment appointment : schedule) {
                System.out.println(appointment);
            }
        }
    }

    // Method to set availability for appointments
    public void setAvailabilityForAppointments() {
        System.out.print("Enter date to set availability (yyyy-mm-dd): ");
        String date = scanner.nextLine().trim();
        System.out.print("Enter available time slots (e.g., 10:00-12:00, 14:00-16:00): ");
        String timeSlots = scanner.nextLine().trim();
        if (date.isEmpty() || timeSlots.isEmpty()) {
            System.out.println("Invalid input. Date and time slots cannot be empty.");
            return;
        }
        boolean success = appointmentController.setAvailability(getName(), date, timeSlots, true);
        if (success) {
            System.out.println("Availability set successfully for Dr. " + getName() + " on " + date);
        } else {
            System.out.println("Failed to set availability. Please try again.");
        }
    }

    // Method to accept or decline appointment requests
    public void acceptOrDeclineAppointmentRequests() {
        List<Appointment> requests = appointmentController.getPendingAppointments(getName());
        if (requests.isEmpty()) {
            System.out.println("No pending appointment requests found.");
        } else {
            for (Appointment appointment : requests) {
                System.out.println(appointment);
                System.out.print("Do you want to accept this appointment? (yes/no): ");
                String response = scanner.nextLine().trim().toLowerCase();
                switch (response) {
                    case "yes" -> {
                        appointmentController.updateAppointmentStatus(appointment.getAppointmentId(), AppointmentStatus.CONFIRMED, "doctor");
                        System.out.println("Appointment accepted.");
                    }
                    case "no" -> {
                        appointmentController.updateAppointmentStatus(appointment.getAppointmentId(), AppointmentStatus.CANCELED, "doctor");
                        System.out.println("Appointment declined.");
                    }
                    default -> {
                        System.out.println("Invalid input. Skipping appointment.");
                        logger.log(Level.WARNING, "Invalid response received: {0}", response);
                    }
                }
            }
        }
    }

    // Method to record appointment outcome
    public void recordAppointmentOutcome() {
        System.out.print("Enter Appointment ID to record outcome: ");
        String appointmentId = scanner.nextLine().trim();
        if (appointmentId.isEmpty()) {
            System.out.println("Invalid input. Appointment ID cannot be empty.");
            return;
        }
        System.out.print("Enter service provided: ");
        String serviceProvided = scanner.nextLine().trim();
        System.out.print("Enter prescribed medications (comma separated, or leave blank if none): ");
        String medicationsInput = scanner.nextLine().trim();
        List<String> prescribedMedications = medicationsInput.isEmpty() ? new ArrayList<>() : List.of(medicationsInput.split(","));
        System.out.print("Enter consultation notes: ");
        String consultationNotes = scanner.nextLine().trim();

        if (serviceProvided.isEmpty() || consultationNotes.isEmpty()) {
            System.out.println("Invalid input. Service provided and consultation notes cannot be empty.");
            return;
        }

        boolean success = appointmentController.recordAppointmentOutcome(appointmentId, serviceProvided, prescribedMedications, consultationNotes, "doctor");
        if (success) {
            System.out.println("Appointment outcome recorded successfully for Appointment ID: " + appointmentId);
        } else {
            System.out.println("Failed to record appointment outcome. Appointment ID not found.");
        }
    }
}
