package dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Appointment;
import model.Appointment.AppointmentOutcome;
import model.Appointment.AppointmentStatus;

public class AppointmentDAO {
    private static final Logger logger = Logger.getLogger(AppointmentDAO.class.getName());
    private static final String APPOINTMENTS_FILE_PATH = "data/Appointment_List.csv";
    private static final String AVAILABILITY_FILE_PATH = "data/DoctorAvailability.csv";

    // Method to read appointments from CSV file
    public List<Appointment> readAppointmentsFromCSV() {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH))) {
            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) {
                    logger.log(Level.WARNING, "Invalid data format. Skipping line: {0}", line);
                    continue;
                }
                String appointmentId = data[0];
                String doctorId = data[1];
                String patientId = data[2];
                String date = data[3];
                String timeSlot = data[4];
                AppointmentStatus status = AppointmentStatus.valueOf(data[5].toUpperCase());
                AppointmentOutcome outcome = data.length > 6 ? new AppointmentOutcome(data[6], List.of(), data.length > 7 ? data[7] : "") : null;
                appointments.add(new Appointment(appointmentId, doctorId, patientId, date, timeSlot, status, outcome));
            }
        } catch (IOException | IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Error reading appointments from CSV: {0}", e.getMessage());
        }
        return appointments;
    }

    // Method to update appointments in CSV file
    public void updateAppointmentsInCSV(List<Appointment> appointments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE_PATH))) {
            bw.write("AppointmentID,DoctorID,PatientID,Date,TimeSlot,Status,Outcome");
            bw.newLine();
            for (Appointment appointment : appointments) {
                bw.write(appointment.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error updating appointments in CSV: {0}", e.getMessage());
        }
    }

    // Method to update doctor availability in CSV file
    @SuppressWarnings("UnnecessaryContinue")
    public void updateDoctorAvailability(String doctorId, String date, String timeSlot, boolean isAvailable) {
        List<String> availabilityData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(AVAILABILITY_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3 && data[0].equalsIgnoreCase(doctorId) && data[1].equals(date) && data[2].equals(timeSlot)) {
                    if (!isAvailable) {
                        continue; // Remove availability if no longer available
                    }
                } else {
                    availabilityData.add(line);
                }
            }
            if (isAvailable) {
                availabilityData.add(doctorId + "," + date + "," + timeSlot);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading doctor availability from CSV: {0}", e.getMessage());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(AVAILABILITY_FILE_PATH))) {
            for (String availability : availabilityData) {
                bw.write(availability);
                bw.newLine();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error updating doctor availability in CSV: {0}", e.getMessage());
        }
    }

    // Method to get available slots by doctor and date
    public List<String> getAvailableSlotsByDoctorAndDate(String doctorId, String date) {
        List<String> availableSlots = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(AVAILABILITY_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3 && data[0].equalsIgnoreCase(doctorId) && data[1].equals(date)) {
                    availableSlots.add(data[2]);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading doctor availability from CSV: {0}", e.getMessage());
        }
        return availableSlots;
    }

    // Method to get available slots across all doctors by date
    public List<String> getAvailableSlotsByDate(String date) {
        List<String> availableSlots = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(AVAILABILITY_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3 && data[1].equals(date)) {
                    availableSlots.add(data[0] + " - " + data[2]);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading doctor availability from CSV: {0}", e.getMessage());
        }
        return availableSlots;
    }

    // Method to get all scheduled appointments
    public List<Appointment> getAllAppointments() {
        return readAppointmentsFromCSV();
    }

    // Method to get appointments by doctor ID
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        List<Appointment> appointments = readAppointmentsFromCSV();
        List<Appointment> doctorAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equalsIgnoreCase(doctorId)) {
                doctorAppointments.add(appointment);
            }
        }
        return doctorAppointments;
    }

    // Method to get pending appointments for a doctor
    public List<Appointment> getPendingAppointments(String doctorId) {
        List<Appointment> appointments = readAppointmentsFromCSV();
        List<Appointment> pendingAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equalsIgnoreCase(doctorId) && appointment.getStatus() == AppointmentStatus.PENDING) {
                pendingAppointments.add(appointment);
            }
        }
        return pendingAppointments;
    }

    // Method to get confirmed appointments by doctor ID
    public List<Appointment> getConfirmedAppointmentsByDoctor(String doctorId) {
        List<Appointment> appointments = readAppointmentsFromCSV();
        List<Appointment> confirmedAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equalsIgnoreCase(doctorId) && appointment.getStatus() == AppointmentStatus.CONFIRMED) {
                confirmedAppointments.add(appointment);
            }
        }
        return confirmedAppointments;
    }

    // Method to update appointment status
    public boolean updateAppointmentStatus(String appointmentId, AppointmentStatus status) {
        List<Appointment> appointments = readAppointmentsFromCSV();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                try {
                    appointment.setStatus(status);
                } catch (IllegalArgumentException e) {
                    logger.log(Level.WARNING, "Invalid status transition: {0}", e.getMessage());
                    return false;
                }
                updateAppointmentsInCSV(appointments);
                updateDoctorAvailability(appointment.getDoctorId(), appointment.getDate(), appointment.getTimeSlot(), status == AppointmentStatus.CANCELED);
                return true;
            }
        }
        return false;
    }

    // Method to update appointment outcome
    public boolean updateAppointmentOutcome(String appointmentId, AppointmentOutcome outcome) {
        List<Appointment> appointments = readAppointmentsFromCSV();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                appointment.setOutcome(outcome);
                try {
                    appointment.setStatus(AppointmentStatus.COMPLETED);
                } catch (IllegalArgumentException e) {
                    logger.log(Level.WARNING, "Invalid status transition: {0}", e.getMessage());
                    return false;
                }
                updateAppointmentsInCSV(appointments);
                updateDoctorAvailability(appointment.getDoctorId(), appointment.getDate(), appointment.getTimeSlot(), true);
                return true;
            }
        }
        return false;
    }

    // Method to approve appointment
    public boolean approveAppointment(String appointmentId) {
        return updateAppointmentStatus(appointmentId, AppointmentStatus.CONFIRMED);
    }

    // Method to decline appointment
    public boolean declineAppointment(String appointmentId) {
        return updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELED);
    }

    // Method to schedule a new appointment
       // Method to schedule a new appointment
    public boolean scheduleAppointment(String doctorId, String patientId, String date, String timeSlot) {
        List<Appointment> appointments = readAppointmentsFromCSV();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equals(doctorId) && appointment.getDate().equals(date) &&
                appointment.getTimeSlot().equals(timeSlot) && appointment.getStatus() == AppointmentStatus.SCHEDULED) {
                logger.log(Level.WARNING, "The selected time slot is already taken. Please choose a different slot.");
                return false;
            }
        }
        String appointmentId = Appointment.generateUniqueAppointmentId(appointments.size());
        Appointment newAppointment = new Appointment(appointmentId, doctorId, patientId, date, timeSlot, AppointmentStatus.SCHEDULED);
        appointments.add(newAppointment);
        updateAppointmentsInCSV(appointments);
        updateDoctorAvailability(doctorId, date, timeSlot, false);
        return true;
    }
    // Method to reschedule an appointment
    public boolean rescheduleAppointment(String appointmentId, String newDate, String newTimeSlot) {
        List<Appointment> appointments = readAppointmentsFromCSV();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId) && appointment.getStatus() == AppointmentStatus.SCHEDULED) {
                for (Appointment otherAppointment : appointments) {
                    if (otherAppointment.getDoctorId().equals(appointment.getDoctorId()) &&
                        otherAppointment.getDate().equals(newDate) &&
                        otherAppointment.getTimeSlot().equals(newTimeSlot) &&
                        otherAppointment.getStatus() == AppointmentStatus.SCHEDULED) {
                        logger.log(Level.WARNING, "The selected new time slot is already taken. Please choose a different slot.");
                        return false;
                    }
                }
                updateDoctorAvailability(appointment.getDoctorId(), appointment.getDate(), appointment.getTimeSlot(), true);
                appointment.setDate(newDate);
                appointment.setTimeSlot(newTimeSlot);
                updateAppointmentsInCSV(appointments);
                updateDoctorAvailability(appointment.getDoctorId(), newDate, newTimeSlot, false);
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
