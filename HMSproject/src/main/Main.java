package main;

import controller.AppointmentController;
import controller.LoginController;
import controller.MedicalRecordController;
import controller.PatientController;
import dao.AppointmentDAO;
import dao.PatientDAO;
import java.util.Scanner;
import model.Patient;
import model.Staff;
import view.PatientMenu;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LoginController loginController = new LoginController();

        boolean exit = false;
        while (!exit) {
            System.out.println("Welcome to the Hospital Management System");
            System.out.println("Please select your role:");
            System.out.println("1. Patient");
            System.out.println("2. Staff");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1, 2, or 3): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String identifier, password;
            switch (choice) {
                case 1:
                    System.out.print("Enter your email or userID: ");
                    identifier = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    password = scanner.nextLine();

                    Patient authenticatedPatient = loginController.authenticateAndGetPatient(identifier, password);
                    if (authenticatedPatient != null) {
                        System.out.println("Welcome to the Patient Dashboard!");

                        PatientDAO patientDAO = new PatientDAO();
                        AppointmentDAO appointmentDAO = new AppointmentDAO();
                        MedicalRecordController medicalRecordController = new MedicalRecordController();
                        AppointmentController appointmentController = new AppointmentController();

                        PatientController patientController = new PatientController(authenticatedPatient, patientDAO, appointmentDAO, medicalRecordController, appointmentController);
                        PatientMenu patientMenu = new PatientMenu(patientController);
                        patientMenu.showMenu();
                    } else {
                        System.out.println("Login failed. Please try again.");
                        System.out.println("Press 'M' to return to the main menu or any other key to retry login.");
                        String response = scanner.nextLine();
                        if (response.equalsIgnoreCase("M")) {
                            continue;
                        }
                    }
                    break;

                case 2:
                    System.out.print("Enter your staff ID or email: ");
                    identifier = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    password = scanner.nextLine();

                    Staff authenticatedStaff = loginController.authenticateAndGetStaff(identifier, password);
                    if (authenticatedStaff != null) {
                        System.out.println("Welcome to the Staff Dashboard!");
                        // Implement staff functionalities here based on roles (e.g., Doctor, Pharmacist)
                    } else {
                        System.out.println("Login failed. Please try again.");
                        System.out.println("Press 'M' to return to the main menu or any other key to retry login.");
                        String response = scanner.nextLine();
                        if (response.equalsIgnoreCase("M")) {
                            continue;
                        }
                    }
                    break;

                case 3:
                    exit = true;
                    System.out.println("Thank you for using the Hospital Management System. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        scanner.close();
    }
}
