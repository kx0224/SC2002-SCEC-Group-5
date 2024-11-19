package dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Appointment;
import model.Appointment.AppointmentOutcome;
import model.Appointment.AppointmentStatus;

public class AppointmentDAO {
    private static final Logger logger = Logger.getLogger(AppointmentDAO.class.getName());
    private static final String APPOINTMENTS_FILE_PATH = System.getProperty("user.dir") + "/src/data/Appointment_List.csv";

    // Method to read appointments from CSV file
    public List<Appointment> readAppointmentsFromCSV() {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH))) {
            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 8) {
                    logger.log(Level.WARNING, "Invalid data format. Skipping line: {0}", line);
                    continue;
                }
                try {
                    String appointmentId = data[0];
                    String doctorId = data[1];
                    String patientId = data[2];
                    String date = data[3];
                    String timeSlot = data[4];
                    AppointmentStatus status = AppointmentStatus.valueOf(data[5].toUpperCase());
                    boolean isAvailable = Boolean.parseBoolean(data[6]);
                    AppointmentOutcome outcome = data.length > 7 ? new AppointmentOutcome(data[7], List.of(), data.length > 8 ? data[8] : "") : null;
                    appointments.add(new Appointment(appointmentId, doctorId, patientId, date, timeSlot, status, isAvailable, outcome));
                } catch (IllegalArgumentException e) {
                    logger.log(Level.WARNING, "Invalid data format or value in line: {0}. Error: {1}", new Object[]{line, e.getMessage()});
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading appointments from CSV: {0}", e.getMessage());
        }
        return appointments;
    }

    // Method to update appointments in CSV file
    public void updateAppointmentsInCSV(List<Appointment> appointments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE_PATH))) {
            bw.write("AppointmentID,DoctorID,PatientID,Date,TimeSlot,Status,isAvailable,Outcome");
            bw.newLine();
            for (Appointment appointment : appointments) {
                bw.write(appointment.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error updating appointments in CSV: {0}", e.getMessage());
        }
    }

    // Method to get available slots by doctor and date
    public List<String> getAvailableSlotsByDoctorAndDate(String doctorId, String date) {
        List<String> availableSlots = new ArrayList<>();
        List<Appointment> appointments = readAppointmentsFromCSV();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equalsIgnoreCase(doctorId) && appointment.getDate().equals(date) && appointment.isAvailable()) {
                availableSlots.add(appointment.getTimeSlot());
            }
        }
        return availableSlots;
    }

    // Method to get available slots across all doctors by date
    public List<String> getAvailableSlotsByDate(String date) {
        List<String> availableSlots = new ArrayList<>();
        List<Appointment> appointments = readAppointmentsFromCSV();
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date) && appointment.isAvailable()) {
                availableSlots.add(appointment.getDoctorId() + " - " + appointment.getTimeSlot());
            }
        }
        return availableSlots;
    }

    // Method to update appointment status
    public boolean updateAppointmentStatus(String appointmentId, AppointmentStatus status) {
        List<Appointment> appointments = readAppointmentsFromCSV();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                if (isValidStatusTransition(appointment.getStatus(), status)) {
                    appointment.setStatus(status);
                    appointment.setAvailable(status == AppointmentStatus.CANCELED);
                    updateAppointmentsInCSV(appointments);
                    return true;
                } else {
                    logger.log(Level.WARNING, "Invalid status transition from {0} to {1} for Appointment ID: {2}", new Object[]{appointment.getStatus(), status, appointmentId});
                    return false;
                }
            }
        }
        return false;
    }

    // Method to validate status transition
    private boolean isValidStatusTransition(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        // Add logic to validate the allowed status transitions
        if (currentStatus == AppointmentStatus.SCHEDULED && (newStatus == AppointmentStatus.CANCELED || newStatus == AppointmentStatus.CONFIRMED || newStatus == AppointmentStatus.COMPLETED)) {
            return true;
        }
        if (currentStatus == AppointmentStatus.CONFIRMED && newStatus == AppointmentStatus.COMPLETED) {
            return true;
        }
        return false;
    }

    // Method to update appointment outcome
    public boolean updateAppointmentOutcome(String appointmentId, AppointmentOutcome outcome) {
        List<Appointment> appointments = readAppointmentsFromCSV();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                if (isValidStatusTransition(appointment.getStatus(), AppointmentStatus.COMPLETED)) {
                    appointment.setOutcome(outcome);
                    appointment.setStatus(AppointmentStatus.COMPLETED);
                    updateAppointmentsInCSV(appointments);
                    return true;
                } else {
                    logger.log(Level.WARNING, "Invalid status transition to COMPLETED for Appointment ID: {0}", appointmentId);
                    return false;
                }
            }
        }
        return false;
    }

    // Method to schedule a new appointment
    public boolean scheduleAppointment(String doctorId, String patientId, String date, String timeSlot) {
        List<Appointment> appointments = readAppointmentsFromCSV();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equals(doctorId) && appointment.getDate().equals(date) && appointment.getTimeSlot().equals(timeSlot) && !appointment.isAvailable()) {
                logger.log(Level.WARNING, "The selected time slot is already taken. Please choose a different slot.");
                return false;
            }
        }
        String appointmentId = UUID.randomUUID().toString();
        Appointment newAppointment = new Appointment(appointmentId, doctorId, patientId, date, timeSlot, AppointmentStatus.SCHEDULED, false);
        appointments.add(newAppointment);
        updateAppointmentsInCSV(appointments);
        return true;
    }

    // Method to reschedule an appointment
    public boolean rescheduleAppointment(String appointmentId, String newDate, String newTimeSlot) {
        List<Appointment> appointments = readAppointmentsFromCSV();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId) && appointment.getStatus() == AppointmentStatus.SCHEDULED) {
                for (Appointment otherAppointment : appointments) {
                    if (otherAppointment.getDoctorId().equals(appointment.getDoctorId()) && otherAppointment.getDate().equals(newDate) && otherAppointment.getTimeSlot().equals(newTimeSlot) && !otherAppointment.isAvailable()) {
                        logger.log(Level.WARNING, "The selected new time slot is already taken. Please choose a different slot.");
                        return false;
                    }
                }
                appointment.setAvailable(true);
                appointment.setDate(newDate);
                appointment.setTimeSlot(newTimeSlot);
                appointment.setAvailable(false);
                updateAppointmentsInCSV(appointments);
                return true;
            }
        }
        return false;
    }

    // Method to get scheduled appointments for a patient
    public List<Appointment> getScheduledAppointmentsByPatient(String patientId) {
        List<Appointment> appointments = readAppointmentsFromCSV();
        List<Appointment> patientAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId().equalsIgnoreCase(patientId) && appointment.getStatus() == AppointmentStatus.SCHEDULED) {
                patientAppointments.add(appointment);
            }
        }
        return patientAppointments;
    }

    // Method to get past appointment outcomes for a patient
    public List<Appointment> getPastAppointmentOutcomesByPatient(String patientId) {
        List<Appointment> appointments = readAppointmentsFromCSV();
        List<Appointment> pastAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId().equalsIgnoreCase(patientId) && appointment.getStatus() == AppointmentStatus.COMPLETED) {
                pastAppointments.add(appointment);
            }
        }
        return pastAppointments;
    }

    // Method to get appointments with prescribed medications for pharmacists
    public List<Appointment> getAppointmentsWithMedications() {
        List<Appointment> appointments = readAppointmentsFromCSV();
        List<Appointment> appointmentsWithMedications = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getStatus() == AppointmentStatus.COMPLETED && appointment.getOutcome() != null && !appointment.getOutcome().getPrescribedMedications().isEmpty()) {
                appointmentsWithMedications.add(appointment);
            }
        }
        return appointmentsWithMedications;
    }
}
