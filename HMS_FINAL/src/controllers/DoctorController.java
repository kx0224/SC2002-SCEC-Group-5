package controllers;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import users.Doctor;
import managers.*;
import menu.DoctorView;
import models.Appointment;
import models.AppointmentOutcome;
import models.AppointmentStatus;

import models.Inventory;
import models.MedicalRecords;
import models.PrescribedMed;
import models.prescriptionStatus;


public class DoctorController {
    private AppointmentManager apptManager;
    private Doctor doctor;
    private medicalRecordsController medicalRecordController;
    private DoctorView doctorView;
    private Inventory inventory;
    private PrescriptionManager prescriptionManager;
    private Scanner sc = new Scanner(System.in);

    public DoctorController(Doctor doctor, DoctorView doctorview,AppointmentManager apptManager,
                            medicalRecordsController medicalRecordController, Inventory inventory,
                            PrescriptionManager prescriptionManager) {
        this.apptManager = apptManager;
        this.medicalRecordController = medicalRecordController;
        this.doctor = doctor;
        this.doctorView = doctorview;
        this.inventory = inventory;
        this.prescriptionManager = prescriptionManager;
    }

    // Show menu for the doctor
    public void showMenu() {
        boolean running = true;
        String patientID, appointmentID,date, startTime, endTime;
        while (running) {
            int choice = doctorView.displayMenu();
            switch (choice) {
                case 1:
                    viewPersonalSchedule(doctor.getHospitalID());
                    break;
                case 2:
                    patientID = doctorView.promptPatientID();
                    viewPatientRecords(patientID.toUpperCase());
                    break;
                case 3:
                    patientID = doctorView.promptPatientID();
                    updatePatientRecords(patientID.toUpperCase());
                    break;
                case 4:
                    viewPersonalSchedule(doctor.getHospitalID());
                    acceptAppointment();
                    break;
                case 5:
                    viewPersonalSchedule(doctor.getHospitalID());
                    declineAppointment();
                    break;
                case 6:
                    try {
                        date = doctorView.promptdate();
                        startTime = doctorView.promptstartTime();
                        endTime = doctorView.promptendTime();
                        setAvailability(date, startTime, endTime);
                    } catch (DateTimeParseException e) {
                        doctorView.showMessage("Invalid time format. Please use HH:mm format.");
                    }
                    break;
                case 7:
                    appointmentID = doctorView.promptAppointmentID();
                    recordAppointmentOutcome();
                    break;
                case 8:
                    running = false;
                    break;
                default:
                    doctorView.showMessage("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    // View patient records by doctor
    public void viewPatientRecords(String patientID) {
        try {
            Set<String> patientIDs = doctor.getPatientsUnderCare(apptManager);
            boolean isPatientUnderCare = patientIDs.stream()
                    .anyMatch(id -> id.equalsIgnoreCase(patientID));

            if (isPatientUnderCare) {
                MedicalRecords specificMedicalRecord = medicalRecordController.getMedicalRecord(patientID);
                if (specificMedicalRecord != null) {
                    doctorView.displayMedicalRecord(specificMedicalRecord);
                } else {
                    doctorView.showMessage("No medical record found for Patient ID: " + patientID);
                }
            } else {
                doctorView.showMessage("Patient ID " + patientID + " is not under your care.");
            }
        } catch (Exception e) {
            doctorView.showMessage("Error while retrieving patient records: " + e.getMessage());
        }
    }

    // Update patient records
    public void updatePatientRecords(String patientID) {
        try {
            Set<String> patientIDs = doctor.getPatientsUnderCare(apptManager);
            if (patientIDs.contains(patientID)) {
                // Prompt the doctor for the updated information
                MedicalRecords updatedInfo = doctorView.promptMedicalRecordUpdate(patientID, doctor.getHospitalID());

                if (updatedInfo != null) {
                    // Update the medical record using medicalRecordsController
                    for (String diagnosis : updatedInfo.getDiagnoses()) {
                        medicalRecordController.updateMedicalRecord(patientID, diagnosis, null, null);
                    }

                    for (String treatment : updatedInfo.getTreatments()) {
                        medicalRecordController.updateMedicalRecord(patientID, null, null, treatment);
                    }

                    doctorView.showMessage("Medical record updated successfully for Patient ID: " + patientID);
                } else {
                    doctorView.showMessage("No updates were made.");
                }
            } else {
                doctorView.showMessage("Patient ID " + patientID + " is not under your care.");
            }
        } catch (Exception e) {
            doctorView.showMessage("Error while updating records: " + e.getMessage());
        }
    }


    // View personal schedule for the doctor
    public List<Appointment> viewPersonalSchedule(String doctorID) {
        try {
            List<Appointment> personalSchedule = apptManager.getDoctorPersonalSchedule(doctorID);

            // Display the doctor's personal schedule
            doctorView.displayPersonalSchedule(personalSchedule);

            return personalSchedule; // Return the list of appointments
        } catch (Exception e) {
            doctorView.showMessage("Error while retrieving schedule: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    public void setAvailability(String dateString, String startTime, String endTime) {
        try {
            // Parse the start and end times
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Adjust pattern as needed
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalDate date = LocalDate.parse(dateString,dateFormatter);
            LocalTime start = LocalTime.parse(startTime, timeFormatter);
            LocalTime end = LocalTime.parse(endTime, timeFormatter);

            while ((start.getMinute() != 0 && start.getMinute() != 30) ||
                    (end.getMinute() != 0 && end.getMinute() != 30)) {
                System.out.println("Invalid time interval. Both start and end times must be at 00 or 30 minutes past the hour.");
                startTime = doctorView.promptstartTime();
                endTime = doctorView.promptendTime();
                start = LocalTime.parse(startTime.trim(), timeFormatter);
                end = LocalTime.parse(endTime.trim(), timeFormatter);
            }

            // Get the doctor's current appointments on the given date
            List<Appointment> currentAppointments = apptManager.getDoctorPersonalSchedule(doctor.getHospitalID());

            // Iterate over each 30-minute slot in the given range
            LocalTime currentTime = start;
            while (currentTime.isBefore(end)) {
                    boolean hasOverlap = false;

                    // Check if the current 30-minute slot overlaps with any existing appointment
                    for (Appointment existingAppointment : currentAppointments) {
                        if (existingAppointment.get_date().equals(date)) {
                            LocalTime existingStart = existingAppointment.get_startTime();
                            LocalTime existingEnd = existingStart.plusMinutes(30); // Assuming each appointment is 30 minutes long

                            // Check for overlap
                            if ((currentTime.isBefore(existingEnd) && currentTime.isAfter(existingStart.minusMinutes(1))) ||
                                    (currentTime.plusMinutes(30).isAfter(existingStart) && currentTime.plusMinutes(30).isBefore(existingEnd.plusMinutes(1)))) {
                                hasOverlap = true;
                                break;
                            }
                        }
                    }

                    // Add the 30-minute slot if no overlap is found
                    if (!hasOverlap) {
                        apptManager.addAppointment(doctor.getHospitalID(), doctor.getName(), date, currentTime, currentTime.plusMinutes(30));
                    } else {
                        System.out.println("Skipping 30-minute slot starting at " + currentTime + " due to overlap.");
                    }
                currentTime = currentTime.plusMinutes(30);
                }

            System.out.println("Availability slots have been set for " + date + " from " + startTime + " to " + endTime);
        } catch (DateTimeParseException e) {
            doctorView.showMessage("Invalid time format. Please use HH:mm format.");
        }
    }



    public void acceptAppointment() {
        int index = doctorView.promptIndex_accept();
        List <Appointment> personalschedule = apptManager.getDoctorPersonalSchedule(doctor.getHospitalID());
        if (index < 1 || index > personalschedule.size()) {
            doctorView.showMessage("Invalid index. Please select a valid slot.");
            return;
        }
        Appointment acceptappt = personalschedule.get(index-1);
        boolean success = apptManager.acceptAppointment(acceptappt.get_appointment_id());

        if (success) {
            System.out.println("Appointment with ID " + acceptappt.get_appointment_id() + " has been accepted.");
        } else {
            System.out.println("Appointment ID " + acceptappt.get_appointment_id() + " not found.");
        }
    }

    // Decline a specific appointment by ID
    public void declineAppointment() {
        int index = doctorView.promptIndex_cancel();
        List <Appointment> personalschedule = apptManager.getDoctorPersonalSchedule(doctor.getHospitalID());
        if (index < 1 || index > personalschedule.size()) {
            doctorView.showMessage("Invalid index. Please select a valid slot.");
            return;
        }
        Appointment cancelappt = personalschedule.get(index-1);
        boolean success = apptManager.cancelAppointment(cancelappt.get_appointment_id());
        if (success){
           System.out.println("Appointment with ID " + cancelappt.get_appointment_id() + " has been declined.");
      } else {
          System.out.println("Appointment ID " + cancelappt.get_appointment_id() + " not found.");
       }}


    // Record outcome for a specific appointment
    public void recordAppointmentOutcome() {
        List<Appointment> personalSchedule = apptManager.getDoctorPersonalSchedule(doctor.getHospitalID());
        doctorView.displayPersonalSchedule(personalSchedule);
    
        int index = doctorView.promptAppointmentIndex();
        if (index < 1 || index > personalSchedule.size()) {
            doctorView.showMessage("Invalid index. Please select a valid appointment.");
            return;
        }
    
        Appointment appointment = personalSchedule.get(index - 1);
        String appointmentID = appointment.get_appointment_id();
        AppointmentOutcome appointmentOutcome = apptManager.getAppointmentOutcomeByID(appointmentID);
    
        if (appointment != null && appointment.get_status() == AppointmentStatus.CONFIRMED) {
            System.out.println("\n--- Record Appointment Outcome ---");
            String outcome = doctorView.promptAppointmentOutcome();
            String prescription = doctorView.promptAppointmentMed();
            Integer quantity = doctorView.promptMedQuantity();
            String service = doctorView.promptServiceType();
    
            // Set appointment outcome details
            appointmentOutcome.setAppointmentOutcome(outcome);
            appointmentOutcome.setServiceType(service);
    
            // Create and set up a new prescription
            PrescribedMed prescribedMed = new PrescribedMed(inventory.getMedicationNameByID(prescription.toUpperCase()), quantity);
            prescribedMed.setAppointment_id(appointmentID);
            prescribedMed.setStatus(prescriptionStatus.PENDING);
    
            // Generate a unique prescription ID and add it to the prescription manager
            String pre_id = prescribedMed.generatePrescription_id(prescriptionManager.getAllPrescriptions());
            prescribedMed.setPrescription_id(pre_id);
            String med_id = inventory.getMed_IDbyMedicationName(prescription);
            prescribedMed.setMedicineID(med_id);
    
            // Use addPrescription method to properly add the prescription to the manager
            prescriptionManager.addPrescription(prescribedMed);
    
            // Update appointment outcome and status
            apptManager.getAllAppointmentOutcomes().put(appointmentID, appointmentOutcome);
            appointment.set_status(AppointmentStatus.COMPLETE);
            apptManager.getAllAppointments().put(appointmentID, appointment);
    
            System.out.println("Recorded outcome for Appointment ID " + appointmentID + ": " + outcome);
        } else {
            System.out.println("Appointment " + appointmentID + " has not been confirmed. Only can record outcomes for confirmed appointments.");
        }
    }
    

}