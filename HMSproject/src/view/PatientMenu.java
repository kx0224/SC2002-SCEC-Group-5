package view;

import controller.PatientController;
import java.util.Scanner;

public class PatientMenu {
    private PatientController patientController;
    private Scanner scanner;

    public PatientMenu(PatientController patientController) {
        this.patientController = patientController;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nPatient Menu for " + patientController.getPatient().getName());
            System.out.println("1. View Medical Record");
            System.out.println("2. Update Personal Information");
            System.out.println("3. View Available Appointment Slots and Schedule an Appointment");
            System.out.println("4. Reschedule an Appointment");
            System.out.println("5. Cancel an Appointment");
            System.out.println("6. View Scheduled Appointments");
            System.out.println("7. View Past Appointment Outcome Records");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    patientController.viewMedicalRecord();
                    break;
                case 2:
                    patientController.updatePersonalInformation();
                    break;
                case 3:
                    patientController.viewAndScheduleAppointments();
                    break;
                case 4:
                    patientController.rescheduleAppointment();
                    break;
                case 5:
                    patientController.cancelAppointment();
                    break;
                case 6:
                    patientController.viewScheduledAppointments();
                    break;
                case 7:
                    patientController.viewPastAppointmentOutcomes();
                    break;
                case 8:
                    System.out.println("Logging out...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
