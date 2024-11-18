package main;

import controller.LoginController;
import java.util.Scanner;

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

        switch (choice) {
            case 1:
                System.out.print("Enter your email or userID: ");
                identifier = scanner.nextLine();
                System.out.print("Enter your password: ");
                password = scanner.nextLine();

                authenticated = loginController.authenticatePatient(identifier, password);
                role = authenticated ? "patient" : "none";
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
                // Here, you can direct the user to specific patient functionalities
            } else if (role.equals("staff")) {
                System.out.println("Welcome to the Staff Dashboard!");
                // Here, you can direct the user to specific staff functionalities (doctor, pharmacist, etc.)
            }
        } else {
            System.out.println("Login failed. Please try again.");
        }
        
        scanner.close();
    }
}
