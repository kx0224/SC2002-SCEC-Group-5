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
            System.out.println("3. View Available Appointment Slots");
            System.out.println("4. Schedule an Appointment");
            System.out.println("5. Reschedule an Appointment");
            System.out.println("6. Cancel an Appointment");
            System.out.println("7. View Scheduled Appointments");
            System.out.println("8. View Past Appointment Outcome Records");
            System.out.println("9. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    patientController.viewMedicalRecord();
                    break;
                case 2:
                    boolean updateRunning = true;
                    while (updateRunning) {
                        System.out.println("What would you like to update?");
                        System.out.println("1. Phone Number (Format: 8 digits, no spaces or special characters)");
                        System.out.println("2. Email Address (Format: example@example.com)");
                        System.out.println("3. Go back to Patient Dashboard");
                        System.out.print("Enter your choice (1, 2, or 3): ");
                        int updateChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        switch (updateChoice) {
                            case 1:
                                System.out.print("Enter new phone number: ");
                                String newPhoneNumber = scanner.nextLine();
                                patientController.updatePersonalInformation(updateChoice, newPhoneNumber);
                                break;
                            case 2:
                                System.out.print("Enter new email address: ");
                                String newEmail = scanner.nextLine();
                                patientController.updatePersonalInformation(updateChoice, newEmail);
                                break;
                            case 3:
                                updateRunning = false;
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                    }
                    break;
                case 3:
                    patientController.viewAvailableAppointments();
                    break;
                case 4:
                    System.out.print("Enter Doctor ID: ");
                    String doctorId = scanner.nextLine();
                    System.out.print("Enter Date (Format: yyyy-mm-dd): ");
                    String date = scanner.nextLine();
                    System.out.print("Enter Time Slot (e.g., 10:00 AM - 11:00 AM): ");
                    String timeSlot = scanner.nextLine();
                    patientController.scheduleAppointment(doctorId, date, timeSlot);
                    break;
                case 5:
                    System.out.print("Enter Appointment ID: ");
                    String appointmentId = scanner.nextLine();
                    System.out.print("Enter new Date (Format: yyyy-mm-dd): ");
                    String newDate = scanner.nextLine();
                    System.out.print("Enter new Time Slot (e.g., 2:00 PM - 3:00 PM): ");
                    String newTimeSlot = scanner.nextLine();
                    patientController.rescheduleAppointment(appointmentId, newDate, newTimeSlot);
                    break;
                case 6:
                    System.out.print("Enter Appointment ID: ");
                    String cancelAppointmentId = scanner.nextLine();
                    patientController.cancelAppointment(cancelAppointmentId);
                    break;
                case 7:
                    patientController.viewScheduledAppointments();
                    break;
                case 8:
                    patientController.viewPastAppointmentOutcomes();
                    break;
                case 9:
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
