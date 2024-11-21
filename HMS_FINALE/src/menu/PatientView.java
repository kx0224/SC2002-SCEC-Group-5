package menu;

import java.util.*;
import controllers.PatientController;
import managers.*;
import models.Appointment;
import models.AppointmentOutcome;
import models.MedicalRecords;
import models.PrescribedMed;
import service.PatientProxy;
import controllers.medicalRecordsController;

public class PatientView {
    private PatientController patientController;
    private medicalRecordsController medicalRecordsController;
    private MedicalRecords medicalRecords;
    private AppointmentManager appointmentManager;
    private PrescriptionManager prescriptionManager;
    private Scanner sc = new Scanner(System.in);

    public PatientView(PatientController patientController) {
        this.patientController = patientController;
    }

    public void setPatientController(PatientController patientController, AppointmentManager appointmentManager, PrescriptionManager prescriptionManager) {
        this.patientController = patientController;
        this.appointmentManager = appointmentManager;
        this.prescriptionManager = prescriptionManager;
    }

    public void displayMedicalRecord(PatientProxy patientProxy) {
        System.out.println("\nPatient ID : " + patientProxy.getPatientID());
        System.out.println("Patient Name: " + patientProxy.getPatientName());
        System.out.println("Date of Birth: " + patientProxy.getDateOfBirth());
        System.out.println("Gender: " + patientProxy.getGender()); // Assuming getGender returns a boolean
        System.out.println("Email Address: " + patientProxy.getEmail());
        System.out.println("Phone Number: " + patientProxy.getPhoneNumber());
        System.out.println("Blood Type: " + patientProxy.getBloodType());
        displayPrescription(patientProxy);
        displayDiagnosis(patientProxy);
        displayTreatment(patientProxy);
    }

    public void displayApptOutcomes(List<AppointmentOutcome> appointmentOutcomes) {
        for (AppointmentOutcome appointmentOutcome : appointmentOutcomes) {
            System.out.println("\nAppointment ID: " + appointmentOutcome.getAppointment_id());
            Appointment appointment = appointmentManager.getAppointmentById(appointmentOutcome.getAppointment_id());
            if (appointment != null) {
                System.out.println("Doctor Name  : " + appointment.get_doctor_name());
            }
            System.out.println("Service  : " + appointmentOutcome.getServiceType());
            List<PrescribedMed> prescriptions = prescriptionManager.getPrescriptionsByApptID(appointmentOutcome.getAppointment_id());
            if (prescriptions != null && !prescriptions.isEmpty()) {
                System.out.println("Medications  : ");
                for (PrescribedMed prescription : prescriptions) {
                    System.out.println("- Medication Name: " + prescription.getMedname());
                    System.out.println("  Quantity: " + prescription.getQuantity());
                    System.out.println("  Status: " + prescription.getStatus());
                }
            } else {
                System.out.println("Medications  : None");
            }
        }
    }

    public void displayDiagnosis(PatientProxy patientProxy) {
        List<String> diagnoses = patientProxy.getPastDiagnoses(); // Assuming this method retrieves a list of past diagnoses
        if (diagnoses != null && !diagnoses.isEmpty()) {
            System.out.println("Past Diagnoses: ");
            for (String diagnosis : diagnoses) {
                System.out.println("- " + diagnosis); // Display each diagnosis
            }
        } else {
            System.out.println("No past diagnoses available.");
        }
    }

    public void displayPrescription(PatientProxy patientProxy) {
        List<PrescribedMed> prescriptions = patientProxy.getPrescription(); // Assuming this method retrieves a list of past diagnoses
        if (prescriptions != null && !prescriptions.isEmpty()) {
            System.out.println("Prescription: ");
            for (PrescribedMed prescription : prescriptions) {
                System.out.println("- " + prescription); // Display each diagnosis
            }
        } else {
            System.out.println("No past prescriptions available.");
        }
    }

    public void displayTreatment(PatientProxy patientProxy) {
        List<String> treatments = patientProxy.getTreatment(); // Assuming this method retrieves a list of past diagnoses
        if (treatments != null && !treatments.isEmpty()) {
            System.out.println("Treatments: ");
            for (String treatment : treatments) {
                System.out.println("- " + treatment); // Display each diagnosis
            }
        } else {
            System.out.println("No past treatments available.");
        }
    }

    public String promptphoneNumber() {
        System.out.print("Enter new Phone Number (blank if no edits): ");
        return sc.nextLine().trim();
    }

    public String promptemail() {
        System.out.print("Enter new Email (blank if no edits): ");
        return sc.nextLine().trim();
    }

    public Integer promptIndex_book() {
        try {
            System.out.print("Enter Index of the slot you want to book: ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Invalid input. Please enter a number between 1 and 9.");
                return -1;
            }
            return Integer.parseInt(input); // Parse the input as an integer
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 9.");
            return -1; // Return an invalid value to prompt the user again
        }
    }

    public Integer promptIndex_cancel() {
        try {
            System.out.print("Enter Index of the slot you want to cancel: ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Invalid input. Please enter a number between 1 and 9.");
                return -1;
            }
            return Integer.parseInt(input); // Parse the input as an integer
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 9.");
            return -1; // Return an invalid value to prompt the user again
        }
    }

    public int displayMenu() {
        try {
            System.out.println("\n--- Patient Menu ---");
            System.out.println("1. View Medical Record");
            System.out.println("2. Update Personal Information");
            System.out.println("3. View Available Appointment Slots");
            System.out.println("4. Schedule an Appointment");
            System.out.println("5. Reschedule an Appointment");
            System.out.println("6. Cancel an Appointment");
            System.out.println("7. View Scheduled Appointments");
            System.out.println("8. View Past Appointment Outcome Records");
         
            System.out.println("9 Logout");
            System.out.print("Select an option (1-9): ");
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 9.");
            return -1; // Return an invalid value to prompt the user again
        }
    }

    

    public void displayAppointments(List<Appointment> Appointments) {
        int index = 1;
        if (Appointments.isEmpty()) {
            System.out.println("No available slots.");
        }
        for (Appointment slot : Appointments) {
            System.out.println("Index: " + index);
            System.out.println("Appointment ID: " + slot.get_appointment_id());
            System.out.println("Doctor ID: " + slot.get_doctor_id());
            System.out.println("Doctor Name: " + slot.get_doctor_name());
            System.out.println("Date: " + slot.get_date());
            System.out.println("Time: " + slot.get_startTime());
            System.out.println("----------------------------------");
            index++;
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
