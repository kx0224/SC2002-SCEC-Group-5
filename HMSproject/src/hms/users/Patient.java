package hms.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Patient extends User {
    private String dateOfBirth;
    private String contactInfo;
    private String bloodType;
    private String pastDiagnoses;
    private List<Appointment> appointments;

    // Constructor
    public Patient(String hospitalID, String password, String name, String gender, String role, int age, String dateOfBirth, String contactInfo, String bloodType, String pastDiagnoses) {
        super(hospitalID, password, name, gender, role, age);
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
        this.bloodType = bloodType;
        this.pastDiagnoses = pastDiagnoses;
        this.appointments = new ArrayList<>();
    }

    // Overloaded Constructor for initial instantiation with fewer parameters
    public Patient(String hospitalID, String password, String name, String gender, String role, int age) {
        super(hospitalID, password, name, gender, role, age);
        this.dateOfBirth = "N/A";
        this.contactInfo = "N/A";
        this.bloodType = "N/A";
        this.pastDiagnoses = "N/A";
        this.appointments = new ArrayList<>();
    }

    // Method to view medical record
    public void viewMedicalRecord() {
        System.out.println("Medical Record for Patient ID " + getHospitalID() + ":");
        System.out.println("Name: " + getName());
        System.out.println("Date of Birth: " + (dateOfBirth.isEmpty() ? "N/A" : dateOfBirth));
        System.out.println("Gender: " + getGender());
        System.out.println("Contact Information: " + (contactInfo.isEmpty() ? "N/A" : contactInfo));
        System.out.println("Blood Type: " + (bloodType.isEmpty() ? "N/A" : bloodType));
        System.out.println("Past Diagnoses and Treatments: " + (pastDiagnoses.isEmpty() ? "N/A" : pastDiagnoses));
    }

    // Method to update contact information
    public void updateContactInfo(String newContactInfo) {
        this.contactInfo = newContactInfo;
        System.out.println("Contact information updated to: " + newContactInfo);
    }

    // Method to schedule an appointment with basic input validation
    public void scheduleAppointment(int timeslot, String date, String doctorName) {
        if (timeslot < 1 || timeslot > 10) {
            System.out.println("Invalid timeslot. Please select a timeslot between 1 and 10.");
            return;
        }

        Appointment newAppointment = new Appointment(timeslot, date, this.getHospitalID(), doctorName);
        appointments.add(newAppointment);
        System.out.println("Appointment scheduled with Dr. " + doctorName + " on " + date + " at timeslot " + timeslot);
    }

    // Method to cancel an appointment with validation
    public void cancelAppointment(int timeslot, String date) {
        Appointment appointmentToCancel = null;
        for (Appointment appointment : appointments) {
            if (appointment.getTimeslot() == timeslot &&
                appointment.getDateArray()[0] == Integer.parseInt(date.split("/")[0]) &&
                appointment.getDateArray()[1] == Integer.parseInt(date.split("/")[1])) {
                appointmentToCancel = appointment;
                break;
            }
        }
        
        if (appointmentToCancel != null) {
            appointments.remove(appointmentToCancel);
            System.out.println("Appointment on " + date + " at timeslot " + timeslot + " has been canceled.");
        } else {
            System.out.println("No matching appointment found to cancel.");
        }
    }

    // Method to view all scheduled appointments
    public void viewScheduledAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No scheduled appointments.");
        } else {
            System.out.println("Scheduled Appointments for Patient ID " + getHospitalID() + ":");
            for (Appointment appointment : appointments) {
                appointment.showDetails();
            }
        }
    }

    // Method to handle patient-specific functionality
    public void showPatientMenu(Scanner scanner) {
        boolean patientRunning = true;
        while (patientRunning) {
            System.out.println("\n--- Patient Menu ---");
            System.out.println("1. View Medical Record");
            System.out.println("2. Update Contact Info");
            System.out.println("3. Schedule Appointment");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. View Scheduled Appointments");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewMedicalRecord();
                    break;
                case 2:
                    System.out.print("Enter new contact info: ");
                    String newContact = scanner.nextLine();
                    updateContactInfo(newContact);
                    break;
                case 3:
                    System.out.print("Enter appointment timeslot (integer): ");
                    int timeslot = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter appointment date (MM/DD): ");
                    String date = scanner.nextLine();
                    System.out.print("Enter doctor name: ");
                    String doctorName = scanner.nextLine();
                    scheduleAppointment(timeslot, date, doctorName);
                    break;
                case 4:
                    System.out.print("Enter appointment timeslot to cancel (integer): ");
                    int cancelTimeslot = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter appointment date to cancel (MM/DD): ");
                    String cancelDate = scanner.nextLine();
                    cancelAppointment(cancelTimeslot, cancelDate);
                    break;
                case 5:
                    viewScheduledAppointments();
                    break;
                case 6:
                    System.out.println("Logging out...");
                    patientRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Getters for restricted access fields
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getPastDiagnoses() {
        return pastDiagnoses;
    }
}
