package view;

import java.util.Scanner;

import model.Doctor;

public class DoctorMenu {
    private Doctor doctor;
    private Scanner scanner;

    public DoctorMenu(Doctor doctor) {
        this.doctor = doctor;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nDoctor Menu for Dr. " + doctor.getName());
            System.out.println("1. View Patient Medical Records");
            System.out.println("2. Update Patient Medical Records");
            System.out.println("3. View Personal Schedule");
            System.out.println("4. Set Availability for Appointments");
            System.out.println("5. Accept or Decline Appointment Requests");
            System.out.println("6. View Upcoming Appointments");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    doctor.viewPatientMedicalRecords();
                    break;
                case 2:
                    doctor.updatePatientMedicalRecords();
                    break;
                case 3:
                    doctor.viewPersonalSchedule();
                    break;
                case 4:
                    doctor.setAvailabilityForAppointments();
                    break;
                case 5:
                    doctor.acceptOrDeclineAppointmentRequests();
                    break;
                case 6:
                    doctor.viewUpcomingAppointments();
                    break;
                case 7:
                    doctor.recordAppointmentOutcome();
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
