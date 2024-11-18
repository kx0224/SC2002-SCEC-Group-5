package controller;

import dao.AppointmentDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Appointment;
import model.Appointment.AppointmentOutcome;
import model.Appointment.AppointmentStatus;
import model.Medicine;

public class AppointmentController {
    private static final Logger logger = Logger.getLogger(AppointmentController.class.getName());
    private final AppointmentDAO appointmentDAO;

    // Constructor
    public AppointmentController() {
        this.appointmentDAO = new AppointmentDAO();
    }

    // Method to decline an appointment
    @SuppressWarnings("LoggerStringConcat")
    public boolean declineAppointment(String appointmentId, String role) {
        if (role.equalsIgnoreCase("doctor")) {
            boolean isDeclined = appointmentDAO.updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELED);
            if (isDeclined) {
                logger.log(Level.INFO, "Appointment declined successfully for Appointment ID: " + appointmentId);
            } else {
                logger.log(Level.WARNING, "No pending appointment found with the provided ID or invalid status transition: " + appointmentId);
            }
            return isDeclined;
        } else {
            logger.log(Level.WARNING, "Unauthorized role attempting to decline appointment: " + role);
            return false;
        }
    }

    // Method to get all scheduled appointments
    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }

    // Method to get available slots across all doctors
    public List<String> getAvailableSlotsAcrossDoctors(String date) {
        return appointmentDAO.getAvailableSlotsByDate(date);
    }

    // Method to get appointments by doctor ID
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointmentDAO.getAppointmentsByDoctor(doctorId);
    }

    // Method to set availability for appointments
    @SuppressWarnings("LoggerStringConcat")
    public boolean setAvailability(String doctorId, String date, String timeSlot, boolean isAvailable) {
        try {
            appointmentDAO.updateDoctorAvailability(doctorId, date, timeSlot, isAvailable);
            logger.log(Level.INFO, "Availability set for Doctor ID: " + doctorId + " on " + date + " with time slot: " + timeSlot);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while setting availability: " + e.getMessage());
            return false;
        }
    }

    // Method to get available appointment slots for a doctor
    public List<String> getAvailableSlots(String doctorId, String date) {
        return appointmentDAO.getAvailableSlotsByDoctorAndDate(doctorId, date);
    }

    // Method to get pending appointments for a doctor
    public List<Appointment> getPendingAppointments(String doctorId) {
        return appointmentDAO.getPendingAppointments(doctorId);
    }

    // Method to update appointment status
    public boolean updateAppointmentStatus(String appointmentId, AppointmentStatus status, String role) {
        if (role.equalsIgnoreCase("doctor") || role.equalsIgnoreCase("administrator")) {
            boolean isUpdated = appointmentDAO.updateAppointmentStatus(appointmentId, status);
            if (isUpdated) {
                logger.log(Level.INFO, "Appointment status updated to {0} for Appointment ID: {1}", new Object[]{status, appointmentId});
            } else {
                logger.log(Level.WARNING, "Appointment ID not found or invalid status transition for Appointment ID: {0}", appointmentId);
            }
            return isUpdated;
        } else {
            logger.log(Level.WARNING, "Unauthorized role attempting to update appointment status: {0}", role);
            return false;
        }
    }

    // Method to record appointment outcome
    @SuppressWarnings("LoggerStringConcat")
    public boolean recordAppointmentOutcome(String appointmentId, String role) {
    if (role.equalsIgnoreCase("doctor")) {
        AppointmentOutcome outcome = collectAppointmentOutcome();
        
        boolean isUpdated = appointmentDAO.updateAppointmentOutcome(appointmentId, outcome);
        if (isUpdated) {
            logger.log(Level.INFO, "Appointment outcome recorded for Appointment ID: " + appointmentId);
        } else {
            logger.log(Level.WARNING, "Appointment ID not found or invalid status transition for Appointment ID: " + appointmentId);
        }
        return isUpdated;
    } else {
        logger.log(Level.WARNING, "Unauthorized role attempting to record appointment outcome: " + role);
        return false;
    }
}
    private AppointmentOutcome collectAppointmentOutcome() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter service provided: ");
            String serviceProvided = scanner.nextLine();

            List<Medicine> prescribedMedications = new ArrayList<>();
            System.out.print("Enter number of medications prescribed: ");
            int numMedications = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            for (int i = 0; i < numMedications; i++) {
                System.out.print("Enter Medication Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter Dosage: ");
                String dosage = scanner.nextLine();
                System.out.print("Enter Quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                // Assuming a system function to create or fetch a medication ID based on the name
                String medicationId = generateMedicationId(name);
                Medicine medicine = new Medicine(medicationId, name, dosage, quantity, 0, false, ""); // Adjust as necessary
                prescribedMedications.add(medicine);
            }

            // Convert List<Medicine> to List<String> (extracting names or other desired fields)
            List<String> medicationNames = prescribedMedications.stream()
                    .map(Medicine::getName)
                    .toList();

            System.out.print("Enter consultation notes: ");
            String consultationNotes = scanner.nextLine();

            return new Appointment.AppointmentOutcome(serviceProvided, medicationNames, consultationNotes);
        }
    }


    // This method simulates generating or retrieving a medication ID based on the name
    private String generateMedicationId(String medicationName) {
        // This is a placeholder; replace with actual logic to fetch or create medication IDs
        return "MED" + medicationName.hashCode();
    }


    // Method to get all appointment outcomes
    public List<Appointment> getAllAppointmentOutcomes() {
        return appointmentDAO.getAllAppointments();
    }

    // Method to schedule a new appointment
    @SuppressWarnings("LoggerStringConcat")
    public boolean scheduleAppointment(String doctorId, String patientId, String date, String timeSlot, String role) {
        if (role.equalsIgnoreCase("patient")) {
            boolean isScheduled = appointmentDAO.scheduleAppointment(doctorId, patientId, date, timeSlot);
            if (isScheduled) {
                logger.log(Level.INFO, "Appointment scheduled successfully for Doctor ID: " + doctorId + ", Patient ID: " + patientId);
            } else {
                logger.log(Level.WARNING, "The selected time slot is already taken or invalid.");
            }
            return isScheduled;
        } else {
            logger.log(Level.WARNING, "Unauthorized role attempting to schedule appointment: " + role);
            return false;
        }
    }

    // Method to cancel an appointment
    public boolean cancelAppointment(String appointmentId, String role) {
        if (role.equalsIgnoreCase("patient") || role.equalsIgnoreCase("administrator")) {
            boolean isCanceled = appointmentDAO.updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELED);
            if (isCanceled) {
                logger.log(Level.INFO, "Appointment canceled successfully for Appointment ID: {0}", appointmentId);
            } else {
                logger.log(Level.WARNING, "No scheduled appointment found with the provided ID or invalid status transition: {0}", appointmentId);
            }
            return isCanceled;
        } else {
            logger.log(Level.WARNING, "Unauthorized role attempting to cancel appointment: {0}", role);
            return false;
        }
    }

    // Method to reschedule an appointment
    @SuppressWarnings("LoggerStringConcat")
    public boolean rescheduleAppointment(String appointmentId, String newDate, String newTimeSlot, String role) {
        if (role.equalsIgnoreCase("patient")) {
            boolean isRescheduled = appointmentDAO.rescheduleAppointment(appointmentId, newDate, newTimeSlot);
            if (isRescheduled) {
                logger.log(Level.INFO, "Appointment rescheduled successfully for Appointment ID: " + appointmentId);
            } else {
                logger.log(Level.WARNING, "No scheduled appointment found with the provided ID or the new slot is already taken: " + appointmentId);
            }
            return isRescheduled;
        } else {
            logger.log(Level.WARNING, "Unauthorized role attempting to reschedule appointment: " + role);
            return false;
        }
    }

    // Method to approve appointment
    @SuppressWarnings("LoggerStringConcat")
    public boolean approveAppointment(String appointmentId, String role) {
        if (role.equalsIgnoreCase("doctor")) {
            boolean isApproved = appointmentDAO.updateAppointmentStatus(appointmentId, AppointmentStatus.CONFIRMED);
            if (isApproved) {
                logger.log(Level.INFO, "Appointment approved successfully for Appointment ID: " + appointmentId);
            } else {
                logger.log(Level.WARNING, "No pending appointment found with the provided ID or invalid status transition: " + appointmentId);
            }
            return isApproved;
        } else {
            logger.log(Level.WARNING, "Unauthorized role attempting to approve appointment: " + role);
            return false;
        }
    }

    // Method to get scheduled appointments for a patient
    public List<Appointment> getScheduledAppointmentsByPatient(String patientId) {
        return appointmentDAO.getScheduledAppointmentsByPatient(patientId);
    }

    // Method to get past appointment outcomes for a patient
    public List<Appointment> getPastAppointmentOutcomesByPatient(String patientId) {
        return appointmentDAO.getPastAppointmentOutcomesByPatient(patientId);
    }

    // Method to get appointments with prescribed medications for pharmacists
    public List<Appointment> getAppointmentsWithMedications(String role) {
        if (role.equalsIgnoreCase("pharmacist")) {
            return appointmentDAO.getAppointmentsWithMedications();
        } else {
            logger.log(Level.WARNING, "Unauthorized role attempting to access prescribed medications: {0}", role);
            return new ArrayList<>();
        }
    }
    
}
