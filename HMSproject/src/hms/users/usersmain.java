package hms.users;

import java.util.Scanner;

public class usersmain {

    public static void main(String[] args) {
        AllUsers users = new AllUsers();
        Inventory inventory = Inventory.getInstance(); // Get the singleton instance
        Scanner scanner = new Scanner(System.in);

        boolean isRunning = true;
        while (isRunning) {
            printLoginInterface();

            // Login process
            System.out.print("Enter Hospital ID: ");
            String hospitalID = scanner.nextLine().trim(); // Trim only the Hospital ID

            System.out.print("Enter Password: ");
            String password = scanner.nextLine(); // Do NOT trim the password

            // Authenticate user
            User loggedInUser = authenticateUser(users, hospitalID, password);

            if (loggedInUser != null) {
                System.out.println("Login successful! Welcome, " + loggedInUser.getName() + " (" + loggedInUser.getHospitalID() + ").");

                // Check role and display appropriate menu
                if (loggedInUser instanceof Patient) {
                    Patient patientUser = (Patient) loggedInUser;
                    patientMenu(patientUser, scanner);
                } else if (loggedInUser instanceof Doctor) {
                    Doctor doctorUser = (Doctor) loggedInUser;
                    doctorMenu(doctorUser, scanner);
                } else if (loggedInUser instanceof Pharmacist) {
                    Pharmacist pharmacistUser = (Pharmacist) loggedInUser;
                    pharmacistMenu(pharmacistUser, scanner, inventory);
                } else if (loggedInUser instanceof Administrator) {
                    Administrator adminUser = (Administrator) loggedInUser;
                    adminMenu(adminUser, scanner, inventory);
                } else {
                    System.out.println("Role-specific functionality for " + loggedInUser.getRole() + " is not yet implemented.");
                }
            } else {
                System.out.println("Invalid Hospital ID or Password. Please try again.");
            }

            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Log in again");
            System.out.println("2. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (choice == 2) {
                isRunning = false;
            }
        }

        System.out.println("Exiting the system...");
        scanner.close();
    }

    private static void printLoginInterface() {
        System.out.println("\n==================================");
        System.out.println("        Hospital Login System     ");
        System.out.println("==================================");
        System.out.println("Please enter your login credentials");
    }

    private static User authenticateUser(AllUsers users, String hospitalID, String password) {
        for (User user : users.getAllUsers()) {
            if (user.getHospitalID().equals(hospitalID) && user.checkPassword(password)) {
                return user;
            }
        }
        return null;
    }

    private static void adminMenu(Administrator adminUser, Scanner scanner, Inventory inventory) {
        boolean adminRunning = true;
        while (adminRunning) {
            adminUser.showMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (!adminUser.performAction(choice)) {
                adminRunning = false;
            }
        }
    }

    // Pharmacist-specific menu with Inventory access
    private static void pharmacistMenu(Pharmacist pharmacistUser, Scanner scanner, Inventory inventory) {
        boolean pharmacistRunning = true;
        while (pharmacistRunning) {
            System.out.println("\n--- Pharmacist Menu ---");
            System.out.println("1. View Pending Prescriptions");
            System.out.println("2. Fulfill Prescription");
            System.out.println("3. View Inventory");
            System.out.println("4. Update Inventory");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    pharmacistUser.viewPendingPrescriptions();
                    break;
                case 2:
                    pharmacistUser.fulfillPrescription();
                    break;
                case 3:
                    inventory.viewInventory(); // View the inventory
                    break;
                case 4:
                    inventory.updateInventory(); // Update inventory in memory
                    break;
                case 5:
                    System.out.println("Logging out...");
                    pharmacistRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Doctor-specific menu
    private static void doctorMenu(Doctor doctorUser, Scanner scanner) {
        boolean doctorRunning = true;
        while (doctorRunning) {
            System.out.println("\n--- Doctor Menu ---");
            System.out.println("1. View Schedule");
            System.out.println("2. Set Availability");
            System.out.println("3. Manage Appointment Requests");
            System.out.println("4. Record Appointment Outcome");
            System.out.println("5. View Upcoming Appointments");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    doctorUser.viewSchedule();
                    break;
                case 2:
                    doctorUser.setAvailability();
                    break;
                case 3:
                    doctorUser.manageAppointmentRequests();
                    break;
                case 4:
                    doctorUser.recordAppointmentOutcome();
                    break;
                case 5:
                    doctorUser.viewUpcomingAppointments();
                    break;
                case 6:
                    System.out.println("Logging out...");
                    doctorRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Patient-specific menu
    private static void patientMenu(Patient patientUser, Scanner scanner) {
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
                    patientUser.viewMedicalRecord();
                    break;
                case 2:
                    System.out.print("Enter new contact info: ");
                    String newContact = scanner.nextLine();
                    patientUser.updateContactInfo(newContact);
                    break;
                case 3:
                    System.out.print("Enter appointment timeslot (integer): ");
                    int timeslot = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter appointment date (MM/DD): ");
                    String date = scanner.nextLine();
                    System.out.print("Enter doctor name: ");
                    String doctorName = scanner.nextLine();
                    patientUser.scheduleAppointment(timeslot, date, doctorName);
                    break;
                case 4:
                    System.out.print("Enter appointment timeslot to cancel (integer): ");
                    int cancelTimeslot = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter appointment date to cancel (MM/DD): ");
                    String cancelDate = scanner.nextLine();
                    patientUser.cancelAppointment(cancelTimeslot, cancelDate);
                    break;
                case 5:
                    patientUser.viewScheduledAppointments();
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
}
