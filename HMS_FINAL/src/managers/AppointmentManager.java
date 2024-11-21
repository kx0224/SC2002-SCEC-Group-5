package managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.Map;
import java.util.Scanner;

import models.Appointment;
import models.AppointmentOutcome;
import models.AppointmentStatus;

public class AppointmentManager {
    protected Map<String, Appointment> Appointments = new HashMap<>();
    protected Map<String, AppointmentOutcome> appointmentOutcomes = new HashMap<>();

    public AppointmentManager() {
        this.Appointments = new HashMap<>();
        this.appointmentOutcomes = new HashMap<>();
        initializeData();
    }

    private void initializeData() {
        File inventoryFile = new File("data/appointments.csv");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Adjust pattern as needed
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); // Adjust pattern as needed

        try (Scanner sc = new Scanner(inventoryFile)) {
            sc.nextLine(); // Skip header line
            while (sc.hasNextLine()) {
                String[] itemData = sc.nextLine().split(",");
                if (itemData.length >= 8) { // Ensure there are at least the required number of fields
                    String patientID = itemData[1].trim();
                    String doctorID = itemData[2].trim();
                    String name = itemData[3].trim();
                    String dateString = itemData[4].trim();
                    String timeString = itemData[5].trim();
                    AppointmentStatus status = AppointmentStatus.valueOf(itemData[6].trim().toUpperCase());
                    String outcome = itemData[7].trim();
                    String service = itemData[8].trim();
                    String medication = itemData.length > 9 ? itemData[9].trim() : null;

                    try {
                        LocalDate date = LocalDate.parse(dateString, dateFormatter);
                        LocalTime time = LocalTime.parse(timeString, timeFormatter);

                        // Create and set up new appointment
                        Appointment appointment = new Appointment(doctorID, name, date, time); // Adjust Appointment constructor if needed
                        appointment.set_appointment_id(doctorID, date, time);
                        appointment.set_patient_id(patientID);
                        appointment.set_status(status);
                        Appointments.put(appointment.get_appointment_id(), appointment);

                        // Create and set up appointment outcome
                        AppointmentOutcome appointmentOutcome = new AppointmentOutcome(patientID, doctorID, appointment.get_appointment_id());
                        if (medication != null) {
                            appointmentOutcome.addPrescription(medication);
                        }
                        if (outcome != null) {
                            appointmentOutcome.set_outcome(outcome);
                        }
                        if (service != null) {
                            appointmentOutcome.setServiceType(service);
                        }

                        appointmentOutcomes.put(appointment.get_appointment_id(), appointmentOutcome);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date or time format for entry, skipping...");
                    }
                } else {
                    System.out.println("Invalid line format, skipping...");
                }
            }
            System.out.println("Appointments initialized successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Appointment data file not found!");
        } catch (NumberFormatException e) {
            System.out.println("Error parsing appointment data. Please check the format of numeric values.");
        }
    }

    public List<Appointment> getDoctorPersonalSchedule(String doctorId) {
        List<Appointment> personalSchedule = new ArrayList<>();

        for (Appointment appointment : Appointments.values()) {
            if (appointment.get_doctor_id().equalsIgnoreCase(doctorId) &&
                    (appointment.get_status() == AppointmentStatus.CONFIRMED ||
                            appointment.get_status() == AppointmentStatus.SCHEDULED)) {
                personalSchedule.add(appointment);
            }
        }

        return personalSchedule;
    }

    public Map<String, Appointment> getAllAppointments() {
        return Appointments;
    }

    public Appointment getAppointmentById(String appointmentId) {
        return Appointments.get(appointmentId);
    }

    public void addAppointment(String doctorId, String doctorName, LocalDate date, LocalTime startTime, LocalTime endTime) {
        LocalTime currentTime = startTime;

        while (currentTime.isBefore(endTime)) {
            // Create a new appointment slot for every 30 minutes interval
            Appointment newAppointment = new Appointment(doctorId, doctorName, date, currentTime);
            newAppointment.set_appointment_id(doctorId, date, currentTime);
            newAppointment.set_status(AppointmentStatus.PENDING);
            Appointments.put(newAppointment.get_appointment_id(), newAppointment);
            System.out.println("Appointment with ID " + newAppointment.get_appointment_id() + " has been added.");

            // Increment current time by 30 minutes
            currentTime = currentTime.plusMinutes(30);
        }
    }

    public boolean bookAppointment(String patientId, String appointmentId) {
        // Check if the patient hasn't reached the appointment limit or has an appointment on the date
        Appointment appointment = Appointments.get(appointmentId);
        if (appointment == null) {
            System.out.println("Appointment not found.");
            return false;
        }

        // Check if the patient can book the appointment based on the limit
        if (!Appointmentlimit(patientId, appointment.get_date())) {
            System.out.println("Booking failed: Patient has reached the appointment limit or already has an appointment on this date.");
            return false;
        }

        // Set the status to SCHEDULED
        appointment.set_status(AppointmentStatus.SCHEDULED);
        appointment.set_patient_id(patientId);
        return true;
    }

    public List<AppointmentOutcome> getAllOutcomesByPatientId(String patientId) {
        List<AppointmentOutcome> outcomesForPatient = new ArrayList<>();
        for (AppointmentOutcome outcome : appointmentOutcomes.values()) {
            if (outcome.getPatientID().equals(patientId)) {
                Appointment appointment = Appointments.get(outcome.getAppointment_id());
                if (appointment != null && appointment.get_status() == AppointmentStatus.COMPLETE) {
                    outcomesForPatient.add(outcome);
                }
            }
        }
        return outcomesForPatient;
    }

    public Map<String, AppointmentOutcome> getAllAppointmentOutcomes() {
        return appointmentOutcomes;
    }

    public AppointmentOutcome getAppointmentOutcomeByID(String AppointmentID) {
        return appointmentOutcomes.get(AppointmentID);
    }

    private void displayAppointmentDetails(Appointment appointment) {
        System.out.println("Appointment ID: " + appointment.get_appointment_id());
        System.out.println("Patient ID: " + appointment.get_patient_id());
        System.out.println("Doctor Name: " + appointment.get_doctor_name());
        System.out.println("Date: " + appointment.get_date());
        System.out.println("Time: " + appointment.get_startTime());
        System.out.println("Status: " + appointment.get_status());
        System.out.println("----------------------------------");
    }

    public int countTotalAppointments(String patientId) {
        int count = 0;
        for (Appointment appointment : Appointments.values()) {
            if (appointment.get_patient_id() != null) {
                if (appointment.get_patient_id().equals(patientId) && appointment.get_status() == AppointmentStatus.CONFIRMED) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean Appointmentlimit(String patientId, LocalDate date) {
        // Check if patient has reached maximum of 3 appointments
        if (countTotalAppointments(patientId) >= 3) {
            System.out.println("Patient already has 3 appointments.");
            return false;
        }

        // Check if patient already has an appointment on the given date
        if (hasAppointmentOnDate(patientId, date)) {
            System.out.println("Patient already has an appointment on this date.");
            return false;
        }

        return true;
    }

    private boolean hasAppointmentOnDate(String patientId, LocalDate date) {
        for (Appointment appointment : Appointments.values()) {
            if (appointment.get_patient_id() != null &&
                    appointment.get_patient_id().equalsIgnoreCase(patientId) &&
                    appointment.get_date().equals(date) &&
                    (appointment.get_status() == AppointmentStatus.CONFIRMED ||
                            appointment.get_status() == AppointmentStatus.SCHEDULED)) {
                return true; // Appointment found on the specified date
            }
        }
        return false; // No appointment found on the specified date
    }

    public boolean rescheduledAppointments(String appointmentId) {
        Appointment appointment = Appointments.get(appointmentId);
        if (appointment == null) {
            System.out.println("Appointment not found.");
            return false;
        }
        // Set the status to PENDING
        appointment.set_status(AppointmentStatus.PENDING);
        appointment.set_patient_id(null);
        return true;
    }

    public boolean cancelAppointment(String appointmentId) {
        // Find the appointment
        Appointment appointment = Appointments.get(appointmentId);
        if (appointment == null) {
            return false;
        }

        // Set the status to CANCELED
        appointment.set_status(AppointmentStatus.CANCELLED);
        appointment.set_patient_id(null);
        return true;
    }

    public boolean acceptAppointment(String appointmentId) {
        // Find the appointment
        Appointment appointment = Appointments.get(appointmentId);
        if (appointment == null) {
            return false;
        }

        // Set the status to CONFIRMED
        appointment.set_status(AppointmentStatus.CONFIRMED);
        return true;
    }
}
