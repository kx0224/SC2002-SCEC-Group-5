package controllers;

import java.util.ArrayList;
import java.util.*;
import users.Patient;
import views.PatientView;
import managers.AppointmentManager;
import domain.Appointment;
import domain.AppointmentStatus;
import domain.AppointmentOutcome;
import proxy.PatientProxy;
import domain.MedicalRecords;

public class PatientController extends UserController{
    private AppointmentManager manageAppts;
    private Patient patient;
    private medicalRecordsController medicalRecordsController;
    private PatientView patientView;
    private Scanner sc = new Scanner(System.in);
    public PatientController(Patient patient, AppointmentManager manageAppts, medicalRecordsController medicalRecordsController, PatientView patientView) {
        super(medicalRecordsController);
        this.manageAppts = manageAppts;
        this.patient = patient;
        this.medicalRecordsController = medicalRecordsController;
        this.patientView = patientView;
    }
    public void showMenu() {
        boolean running = true;
        String phoneNumber,email, patientID;
        while (running) {
            int choice = patientView.displayMenu();
            switch (choice) {
                case 1:
                    viewMedicalRecord(patient.hospitalID);
                    break;
                case 2:
                    phoneNumber = patientView.promptphoneNumber();
                    email = patientView.promptemail();
                    updatePersonalInfo(patient.hospitalID, phoneNumber,email);
                    break;
                case 3:
                    System.out.println("\n--- View Available Slots ---");
                    viewAvailableSlots(true);
                    break;
                case 4:
                    patientID = patient.getHospitalID();
                    viewAvailableSlots(true);
                    scheduleAppointment(patientID);
                    break;
                case 5:
//                    viewScheduledAppointments(patient.getHospitalID(),true);
                    System.out.println("\n--- Displaying Your Appointments ---");
                    rescheduleAppointment(patient.getHospitalID());
                    break;
                case 6:
                    System.out.println("\n--- Displaying Your Appointments ---");
                    viewScheduledAppointments(patient.getHospitalID(),true);
                    cancelAppointment(patient.getHospitalID());
                    break;
                case 7:
                    System.out.println("\n--- Displaying Your Appointments ---");
                    patientID = patient.getHospitalID();
                    viewScheduledAppointments(patientID,true);
                    break;
                case 8:
                    System.out.println("\n--- Displaying Appointment Outcomes ---");
                    viewApptOutcome(patient.getHospitalID());
                    break;
                case 9:
//                    System.out.println("Logging out...");
                    running = false;
                    break;
                default:
                    patientView.showMessage("Invalid choice. Please try again.");
                    break;
            }
        }
    }
    public void viewApptOutcome(String patientID){
        List<AppointmentOutcome> appointmentOutcomes = manageAppts.getAllOutcomesByPatientId(patientID);
        patientView.displayApptOutcomes(appointmentOutcomes);

    }
    public void viewMedicalRecord(String patientID){
        MedicalRecords medicalrecord = medicalRecordsController.getMedicalRecord(patientID);
        if (medicalrecord != null){
            Patient patient = getPatientById(patientID);
            PatientProxy patientProxy = new PatientProxy(medicalrecord, patient);
            patientView.displayMedicalRecord(patientProxy);
        }
        else{
            System.out.println("You do not have a medical record");
        }

    }

    public void updatePersonalInfo(String patientID, String phoneNumber, String email) {
        // Retrieve the patient from the UserMap using patientID
        Patient patient = getPatientById(patientID);

        if (patient == null) {
            patientView.showMessage("Patient with ID " + patientID + " not found.");
            return; // Exit if patient does not exist
        }

        // Update the patient's phone number and email
        if (!phoneNumber.isEmpty()){
            patient.setPhonenumber(phoneNumber);
        }
        if (!email.isEmpty()){
            patient.setEmail(email);
        }



        patientView.showMessage("Patient information updated successfully for ID: " + patientID);
    }

    public List<Appointment> viewAvailableSlots(boolean display) {
        List<Appointment> availableSlots = new ArrayList<>();

        // Iterate through all entries in the Appointments HashMap
        for (Appointment appointment : manageAppts.getAllAppointments().values()) {
            // Check if the appointment status is PENDING
            if (appointment.get_status() == AppointmentStatus.PENDING) {
                availableSlots.add(appointment); // Add to available slots list
            }
        }
        if (display){
        patientView.displayAppointments(availableSlots);}

        return availableSlots; // Return the list of available appointments
    }
    public void scheduleAppointment(String patientID){
        List<Appointment> availableSlots = viewAvailableSlots(false); // Retrieve the list of available slots

        if (availableSlots.isEmpty()) {
            patientView.showMessage("There are no available slots to book an appointment.");
            return; // Exit if there are no available slots
        }

        // Prompt the user to select an appointment slot
        int index = patientView.promptIndex_book();// Prompt for the index

        Appointment bookSlot = availableSlots.get(index-1);
        if (hasConflictWithExistingAppointments(patientID, bookSlot)) {
            patientView.showMessage("The selected appointment timing conflicts with an existing appointment. Please choose another slot.");
            return;}


            boolean success = manageAppts.bookAppointment(patientID, bookSlot.get_appointment_id());
            if (success){
                patientView.showMessage("Appointment successfully booked");
            }
            else {
                patientView.showMessage("Failed to book appointment");
            }

    }
    private boolean hasConflictWithExistingAppointments(String patientID, Appointment newAppointment) {
        for (Appointment existingAppointment : viewScheduledAppointments(patientID, false)) {
            if (existingAppointment.get_date().equals(newAppointment.get_date()) &&
                    existingAppointment.get_startTime().equals(newAppointment.get_startTime())) {
                return true; // Conflict found
            }
        }
        return false; // No conflict
    }

    public void rescheduleAppointment(String patientID){
        List<Appointment> scheduledAppts = viewScheduledAppointments(patientID,false);
        List<Appointment> availableSlots = viewAvailableSlots(false);
        if (scheduledAppts.isEmpty()){
            patientView.showMessage("You have no appointments to reschedule.");
        }
        else{
            patientView.displayAppointments(scheduledAppts);
            int old_index = patientView.promptIndex_cancel();

            if (old_index < 1 || old_index > scheduledAppts.size()) {
                patientView.showMessage("Invalid index. Please select a valid slot.");
                return;
            }
            Appointment old_slot = scheduledAppts.get(old_index-1);
            System.out.println("\n--- Displaying Available Slots ---");
            patientView.displayAppointments(availableSlots);
            int new_index = patientView.promptIndex_book();

            if (new_index < 1 || new_index > availableSlots.size()) {
                patientView.showMessage("Invalid index. Please select a valid slot.");
                return;
            }

            Appointment new_slot = availableSlots.get(new_index - 1);

            // Check if the new slot is available
            if (new_slot.get_status() != AppointmentStatus.PENDING) {
                patientView.showMessage("The selected slot is not available. Please choose another slot.");
                return;
            }
            boolean rescheduleSuccess = manageAppts.rescheduledAppointments(old_slot.get_appointment_id());
            boolean bookSuccess = manageAppts.bookAppointment(patientID, new_slot.get_appointment_id());

            if (rescheduleSuccess && bookSuccess){
                patientView.showMessage("Appointment has been successfully rescheduled from "+ old_slot.get_appointment_id()+ " to "+ new_slot.get_appointment_id());
            }
            else{
                patientView.showMessage("Failed to reschedule appointment.");
            }

        }

    }
    public List<Appointment> viewScheduledAppointments(String patientID,boolean display){
        List<Appointment> scheduledAppts = new ArrayList<>();

        // Iterate through all entries in the Appointments HashMap
        for (Appointment appointment : manageAppts.getAllAppointments().values()) {
            if (appointment.get_patient_id() !=null){
            if (appointment.get_patient_id().equals(patientID) &&
                    (appointment.get_status() == AppointmentStatus.CONFIRMED || appointment.get_status() == AppointmentStatus.SCHEDULED)) {
                scheduledAppts.add(appointment); // Add to patient's appointments
            }
        }}
        if (display){
        patientView.displayAppointments(scheduledAppts);}

        return scheduledAppts; // Return the list of available appointments
    }

    public void cancelAppointment(String patientID){
        List<Appointment> scheduledAppts = viewScheduledAppointments(patientID,false);
        if (scheduledAppts.isEmpty()){
            patientView.showMessage("You have no appointments to reschedule.");
        }
        else{
            int index = patientView.promptIndex_cancel();
            Appointment cancelAppt = scheduledAppts.get(index-1);
            boolean success = manageAppts.rescheduledAppointments(cancelAppt.get_appointment_id());

            if (success){
                patientView.showMessage("Appointment successfully cancelled");
            }
            else{
                patientView.showMessage("Failed to cancel appointment");
            }
        }


    }

}
