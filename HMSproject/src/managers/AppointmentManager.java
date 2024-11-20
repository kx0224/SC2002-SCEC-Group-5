package managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import models.*;

public class AppointmentManager {
    protected Map<String, Appointment> appointments;
    protected Map<String, AppointmentOutcome> appointmentOutcomes;

    // Constructor
    public AppointmentManager() {
        this.appointments = new HashMap<>();
        this.appointmentOutcomes = new HashMap<>();
        initializeData();
    }

    // Initialize appointment data from CSV file
    private void initializeData() {
        File inventoryFile = new File("data/appointments.csv");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        try (Scanner sc = new Scanner(inventoryFile)) {
            sc.nextLine(); // Skip header line
            while (sc.hasNextLine()) {
                String[] itemData = sc.nextLine().split(",");
                if (itemData.length >= 8) {
                    String patientId = itemData[1].trim();
                    String doctorId = itemData[2].trim();
                    String doctorName = itemData[3].trim();
                    String dateString = itemData[4].trim();
                    String timeString = itemData[5].trim();
                    AppointmentStatus status = AppointmentStatus.valueOf(itemData[6].trim().toUpperCase());
                    String outcome = itemData[7].trim();
                    String service = itemData[8].trim();
                    String medication = itemData[9].trim();

                    try {
                        LocalDate date = LocalDate.parse(dateString, dateFormatter);
                        LocalTime time = LocalTime.parse(timeString, timeFormatter);

                        Appointment appointment = new Appointment(doctorId, doctorName, date, time);
                        appointment.setPatientId(patientId);
                        appointment.setStatus(status);
                        appointments.put(appointment.getAppointmentId(), appointment);

                        AppointmentOutcome appointmentOutcome = new AppointmentOutcome(patientId, doctorId, appointment.getAppointmentId());
                        if (medication != null) {
                            appointmentOutcome.addPrescription(medication);
                        }
                        if (outcome != null) {
                            appointmentOutcome.setOutcome(outcome);
                        }
                        if (service != null) {
                            appointmentOutcome.setServiceType(service);
                        }

                        appointmentOutcomes.put(appointment.getAppointmentId(), appointmentOutcome);
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

    // CRUD operations and other methods
    public Map<String, Appointment> getAllAppointments() {
        return appointments;
    }

    public Appointment getAppointmentById(String appointmentId) {
        return appointments.get(appointmentId);
    }

    public void addAppointment(String doctorId, String doctorName, LocalDate date, LocalTime startTime, LocalTime endTime) {
        LocalTime currentTime = startTime;

        while (currentTime.isBefore(endTime)) {
            Appointment newAppointment = new Appointment(doctorId, doctorName, date, currentTime);
            newAppointment.setStatus(AppointmentStatus.PENDING);
            appointments.put(newAppointment.getAppointmentId(), newAppointment);
            System.out.println("Appointment with ID " + newAppointment.getAppointmentId() + " has been added.");

            currentTime = currentTime.plusMinutes(30);
        }
    }

    public boolean bookAppointment(String patientId, String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment == null) {
            System.out.println("Appointment not found.");
            return false;
        }

        if (!appointmentLimit(patientId, appointment.getDate())) {
            System.out.println("Booking failed: Patient has reached the appointment limit or already has an appointment on this date.");
            return false;
        }

        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setPatientId(patientId);
        return true;
    }

    public List<Appointment> getDoctorPersonalSchedule(String doctorId) {
        List<Appointment> personalSchedule = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorId().equalsIgnoreCase(doctorId) &&
                    (appointment.getStatus() == AppointmentStatus.CONFIRMED ||
                            appointment.getStatus() == AppointmentStatus.SCHEDULED)) {
                personalSchedule.add(appointment);
            }
        }
        return personalSchedule;
    }

    public List<AppointmentOutcome> getAllOutcomesByPatientId(String patientId) {
        List<AppointmentOutcome> outcomesForPatient = new ArrayList<>();
        for (AppointmentOutcome outcome : appointmentOutcomes.values()) {
            if (outcome.getPatientId().equals(patientId)) {
                Appointment appointment = appointments.get(outcome.getAppointment_id());
                if (appointment != null && appointment.getStatus() == AppointmentStatus.COMPLETE) {
                    outcomesForPatient.add(outcome);
                }
            }
        }
        return outcomesForPatient;
    }

    public boolean cancelAppointment(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment == null) {
            return false;
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setPatientId(null);
        return true;
    }

    public boolean rescheduleAppointment(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment == null) {
            System.out.println("Appointment not found.");
            return false;
        }

        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setPatientId(null);
        return true;
    }

    public boolean acceptAppointment(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment == null) {
            return false;
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        return true;
    }

    public int countTotalAppointments(String patientId) {
        int count = 0;
        for (Appointment appointment : appointments.values()) {
            if (appointment.getPatientId() != null && appointment.getPatientId().equals(patientId) &&
                    appointment.getStatus() == AppointmentStatus.CONFIRMED) {
                count++;
            }
        }
        return count;
    }

    public boolean appointmentLimit(String patientId, LocalDate date) {
        if (countTotalAppointments(patientId) >= 3) {
            System.out.println("Patient already has 3 appointments.");
            return false;
        }

        if (hasAppointmentOnDate(patientId, date)) {
            System.out.println("Patient already has an appointment on this date.");
            return false;
        }

        return true;
    }

    private boolean hasAppointmentOnDate(String patientId, LocalDate date) {
        for (Appointment appointment : appointments.values()) {
            if (appointment.getPatientId().equalsIgnoreCase(patientId) &&
                    appointment.getDate().equals(date) &&
                    (appointment.getStatus() == AppointmentStatus.CONFIRMED ||
                            appointment.getStatus() == AppointmentStatus.SCHEDULED)) {
                return true;
            }
        }
        return false;
    }

    public void displayAppointmentDetails(Appointment appointment) {
        System.out.println("Appointment ID: " + appointment.getAppointmentId());
        System.out.println("Patient ID: " + appointment.getPatientId());
        System.out.println("Doctor Name: " + appointment.getDoctorName());
        System.out.println("Date: " + appointment.getDate());
        System.out.println("Time: " + appointment.getStartTime());
        System.out.println("Status: " + appointment.getStatus());
        System.out.println("----------------------------------");
    }

    public Map<String, AppointmentOutcome> getAllAppointmentOutcomes() {
        return appointmentOutcomes;
    }

    public AppointmentOutcome getAppointmentOutcomeById(String appointmentId) {
        return appointmentOutcomes.get(appointmentId);
    }
}
