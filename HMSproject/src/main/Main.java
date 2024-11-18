package main;

import controller.AppointmentController;
import controller.LoginController;
import controller.MedicalRecordController;
import controller.PatientController;
import dao.AppointmentDAO;
import dao.PatientDAO;
import java.util.Scanner;
import model.Patient;
import view.PatientMenu;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LoginController loginController = new LoginController();

        System.out.println("Welcome to the Hospital Management System");
        System.out.println("Please select your role:");
        System.out.println("1. Patient");
        System.out.println("2. Staff");
        System.out.print("Enter your choice (1 or 2): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String identifier, password;
        boolean authenticated = false;
        String role = "none";
        Patient authenticatedPatient = null;

        switch (choice) {
            case 1:
                System.out.print("Enter your email or userID: ");
                identifier = scanner.nextLine();
                System.out.print("Enter your password: ");
                password = scanner.nextLine();

                authenticatedPatient = loginController.authenticateAndGetPatient(identifier, password);
                if (authenticatedPatient != null) {
                    authenticated = true;
                    role = "patient";
                } else {
                    role = "none";
                }
                break;

            case 2:
                System.out.print("Enter your staff ID or email: ");
                identifier = scanner.nextLine();
                System.out.print("Enter your password: ");
                password = scanner.nextLine();

                role = loginController.authenticate(identifier, password);
                authenticated = !role.equals("none");
                break;

            default:
                System.out.println("Invalid choice. Exiting the system.");
                System.exit(0);
        }

        if (authenticated) {
            if (role.equals("patient")) {
                System.out.println("Welcome to the Patient Dashboard!");

                // Assuming you can retrieve the Patient object after successful login
                PatientDAO patientDAO = new PatientDAO();
                AppointmentDAO appointmentDAO = new AppointmentDAO();
                MedicalRecordController medicalRecordController = new MedicalRecordController();
                AppointmentController appointmentController = new AppointmentController();

                // Update the constructor call to match the defined constructor in PatientController
                PatientController patientController = new PatientController(authenticatedPatient, patientDAO, appointmentDAO, medicalRecordController, appointmentController);

                // Instantiate the PatientMenu and show the menu
                PatientMenu patientMenu = new PatientMenu(patientController);
                patientMenu.showMenu();
            } else if (role.equals("staff")) {
                System.out.println("Welcome to the Staff Dashboard!");
                // Implement staff functionalities here based on roles (e.g., Doctor, Pharmacist)
            }
        } else {
            System.out.println("Login failed. Please try again.");
        }

        scanner.close();
    }
}
