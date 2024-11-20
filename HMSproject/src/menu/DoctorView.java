package HMSystem.views;
import java.util.*;
import HMSystem.controllers.DoctorController;
import HMSystem.domain.Appointment;
import HMSystem.domain.MedicalRecords;
import HMSystem.domain.PrescribedMed;
public class DoctorView {
    private DoctorController doctorController;
    private Scanner sc = new Scanner(System.in);

    public DoctorView(DoctorController doctorController) {
        this.doctorController = doctorController;
    }
    public void setDoctorController(DoctorController doctorController) {
        this.doctorController = doctorController;
    }

    // Displays the main menu for the doctor
    public int displayMenu() {
        try {
            System.out.println("");
            System.out.println("--- Doctor Menu ---");
            System.out.println("1. View Personal Schedule");
            System.out.println("2. View Patient Records");
            System.out.println("3. Update Patient Records");
            System.out.println("4. Accept Appointment");
            System.out.println("5. Decline Appointment");
            System.out.println("6. Set Available Slots");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. Exit");
            System.out.print("Select an option (1-8): ");
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 8.");
            return -1; // Return an invalid value to prompt the user again
        }
    }
    // Prompt for Patient ID
    public String promptPatientID() {

        System.out.print("Enter Patient ID: ");
        return sc.nextLine().trim();
    }
    public String promptAppointmentOutcome() {

        System.out.print("Enter Appointment Outcome: ");
        return sc.nextLine().trim();
    }
    public String promptServiceType() {

        System.out.print("Enter Appointment Service Type: ");
        return sc.nextLine().trim();
    }
    public String promptAppointmentMed() {

        System.out.print("Enter Medication needed for Appointment: ");
        return sc.nextLine().trim();
    }
    public Integer promptMedQuantity() {
    try{
        System.out.print("Enter Medication Quantity: ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Invalid input. Please enter a number between 1 and 9.");
            return -1;
        }
        return Integer.parseInt(input); // Parse the input as an integer
    } catch (NumberFormatException e)

    {
        System.out.println("Invalid input. Please enter a number between 1 and 9.");
        return -1; // Return an invalid value to prompt the user again
    }
    }

    // Prompt for Appointment ID
    public String promptAppointmentID() {

        System.out.print("Enter Appointment ID: ");
        return sc.nextLine().trim();
    }
    public String promptdate() {

        System.out.print("Enter the date (DD/MM/YYYY): ");
        return sc.nextLine().trim();
    }
    public String promptstartTime() {

        System.out.print("Enter start time (24 hour clock HH:mm, only 00 & 30 minutes are accepted): ");
        return sc.nextLine().trim();
    }
    public String promptendTime() {

        System.out.print("Enter end time (24 hour clock HH:mm, only 00 & 30 minutes are accepted): ");
        return sc.nextLine().trim();
    }



    // Displays the personal schedule for the doctor
    public void displayPersonalSchedule(List<Appointment> appointments) {
        int index = 1;
        if (appointments.isEmpty()) {
            System.out.println("No upcoming appointments found.");
        } else {
            System.out.println("\n--- Upcoming Appointments ---");
            for (Appointment appointment : appointments) {
                System.out.println("Index: "+index);
                System.out.println("Appointment ID: " + appointment.get_appointment_id());
                System.out.println("Patient ID: " + appointment.get_patient_id());
                System.out.println("Date: " + appointment.get_date());
                System.out.println("Time: " + appointment.get_startTime());
                System.out.println("Status: " + appointment.get_status());
                System.out.println("-----------------------------------");
                index++;
            }
        }
    }
    public Integer promptIndex_accept(){
        try{
            System.out.print("Enter Index of the slot you want to accept: ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Invalid input.");
                return -1;
            }
            return Integer.parseInt(input); // Parse the input as an integer
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return -1; // Return an invalid value to prompt the user again
        }
    }
    public Integer promptIndex_cancel(){
        try{
            System.out.print("Enter Index of the slot you want to cancel: ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Invalid input.");
                return -1;
            }
            return Integer.parseInt(input); // Parse the input as an integer
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return -1; // Return an invalid value to prompt the user again
        }
    }
    public MedicalRecords promptMedicalRecordUpdate(String patientID, String doctorID) {
        try {
            System.out.println("Updating Medical Record for Patient ID: " + patientID);

            // Create a temporary MedicalRecords object to hold updates
            MedicalRecords updatedRecord = new MedicalRecords(patientID, "", "");

            // Get diagnoses
            System.out.print("Enter new diagnoses: ");
            String diagnosesInput = sc.nextLine().trim();
            updatedRecord.addDiagnosis(diagnosesInput);
            sc.nextLine();

            // Get treatments
            System.out.print("Enter new treatments: ");
            String treatmentsInput = sc.nextLine().trim();
            updatedRecord.addTreatment(treatmentsInput);

            return updatedRecord;
        } catch (Exception e) {
            System.out.println("Error while updating medical records: " + e.getMessage());
            return null;
        }
    }
    public void showMessage(String message) {
        System.out.println(message);
    }


    public void displayMedicalRecord(MedicalRecords medicalRecord){
        System.out.println("\n--- Patient Medical Record ---");
        if (medicalRecord != null) {
            System.out.println("Patient ID: " + medicalRecord.getPatientID());
            System.out.println("Name: " + medicalRecord.getPatientName());
            System.out.println("Blood Type: " + medicalRecord.getBloodType());
             displayDiagnosis(medicalRecord);
            displayPrescription(medicalRecord);
            displayTreatment(medicalRecord);
        } else {
            System.out.println("No medical record found for Patient ID: ");
        }
    }
    public void displayDiagnosis(MedicalRecords medicalRecord) {
        List<String> diagnoses = medicalRecord.getDiagnoses(); // Assuming this method retrieves a list of past diagnoses
        if (diagnoses != null && !diagnoses.isEmpty()) {
            System.out.println("Past Diagnoses: ");
            for (String diagnosis : diagnoses) {
                System.out.println("- " + diagnosis); // Display each diagnosis
            }
        } else {
            System.out.println("No past diagnoses available.");
        }
    }
    public void displayPrescription(MedicalRecords medicalRecord) {
        List<PrescribedMed> prescriptions = medicalRecord.getPrescriptions(); // Assuming this method retrieves a list of past diagnoses
        if (prescriptions != null && !prescriptions.isEmpty()) {
            System.out.println("Prescription: ");
            for (PrescribedMed prescription : prescriptions) {
                System.out.println("- " +prescription); // Display each diagnosis
            }
        } else {
            System.out.println("No past prescriptions available.");
        }
    }
    public void displayTreatment(MedicalRecords medicalRecord) {
        List<String> treatments = medicalRecord.getTreatments(); // Assuming this method retrieves a list of past diagnoses
        if (treatments != null && !treatments.isEmpty()) {
            System.out.println("Treatments: ");
            for (String treatment : treatments) {
                System.out.println("- " +treatment); // Display each diagnosis
            }
        } else {
            System.out.println("No past treatments available.");
        }
    }

}
