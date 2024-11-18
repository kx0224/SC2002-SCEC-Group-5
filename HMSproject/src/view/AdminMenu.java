package view;

import java.util.InputMismatchException;
import java.util.Scanner;
import model.Administrator;

public class AdminMenu {
    public static void showMenu(Administrator admin) {
        if (admin == null) {
            System.out.println("Error: Administrator object is null. Cannot display menu.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nAdministrator Menu");
            System.out.println("1. View and Manage Hospital Staff");
            System.out.println("2. View Appointments details");
            System.out.println("3. View and Manage Medication Inventory");
            System.out.println("4. Approve Replenishment Requests");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        admin.manageStaff();
                        break;
                    case 2:
                        admin.manageAppointments();
                        break;
                    case 3:
                        admin.manageInventory();
                        break;
                    case 4:
                        admin.approveReplenishmentRequests();
                        break;
                    case 5:
                        running = false;
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}
