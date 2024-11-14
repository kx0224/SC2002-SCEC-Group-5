package hms.users;

import java.util.Scanner;

public class Administrator extends User {
    private AllUsers allUsers;
    private Scanner scanner;
    private Inventory inventory;

    // Constructor
    public Administrator(String userID, String password, String name, String gender, String role, int age, AllUsers allUsers) {
        super(userID, password, name, gender, role, age);
        this.allUsers = allUsers;
        this.scanner = new Scanner(System.in);
        this.inventory = Inventory.getInstance(); // Get the singleton instance
    }

    // Display the administrator menu
    public void showMenu() {
        System.out.println("---- Administrator Menu ----");
        System.out.println("1. View and Manage Hospital Staff");
        System.out.println("2. View Appointments Details");
        System.out.println("3. View and Manage Medication Inventory");
        System.out.println("4. Approve Replenishment Requests");
        System.out.println("5. Logout");
    }

    // Execute selected option
    public boolean performAction(int choice) {
        switch (choice) {
            case 1:
                viewAndManageStaff();
                break;
            case 2:
                viewAppointments();
                break;
            case 3:
                manageInventory();
                break;
            case 4:
                approveReplenishmentRequests();
                break;
            case 5:
                System.out.println("Logging out...");
                return false; // Ends the session for the administrator
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        return true;
    }

    // Method to manage hospital staff
    private void viewAndManageStaff() {
        System.out.println("---- Manage Hospital Staff ----");
        System.out.println("1. View All Staff");
        System.out.println("2. Add Staff Member");
        System.out.println("3. Remove Staff Member");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewAllUsers();
                break;
            case 2:
                addStaffMember();
                break;
            case 3:
                removeStaffMember();
                break;
            default:
                System.out.println("Invalid choice. Returning to Administrator Menu.");
        }
    }

    // Method to view all users in the system
    public void viewAllUsers() {
        System.out.println("---- Staff List ----");
        for (User user : allUsers.getAllUsers()) { // Assuming getAllUsers() retrieves the full list of staff
            System.out.printf("ID: %s | Role: %s | Name: %s | Gender: %s | Age: %d",
                    user.getHospitalID(),
                    user.getRole(),
                    user.getName(),
                    user.getGender(),
                    user.getAge());

            // Display specialization if the user is a doctor
            if (user instanceof Doctor) {
                Doctor doctor = (Doctor) user;
                System.out.printf(" | Specialization: %s", doctor.getSpecialization());
            }

            System.out.println(); // Move to the next line after each user
        }
    }

    // Method to add a new staff member
    public void addStaffMember() {
        System.out.print("Enter new staff ID: ");
        String staffID = scanner.nextLine();

        if (allUsers.findUserById(staffID) != null) {
            System.out.println("A user with this ID already exists. Please use a unique ID.");
            return;
        }

        String password;
        while (true) {
            System.out.print("Enter new staff password (at least 8 characters): ");
            password = scanner.nextLine();

            if (password.length() >= 8) {
                break;
            } else {
                System.out.println("Password must be at least 8 characters. Please try again.");
            }
        }

        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter role (Doctor/Pharmacist/Administrator): ");
        String role = scanner.nextLine();

        User newUser;
        if (role.equalsIgnoreCase("Doctor")) {
            System.out.print("Enter specialization: ");
            String specialization = scanner.nextLine();
            newUser = new Doctor(staffID, password, name, gender, role, age, specialization);
        } else if (role.equalsIgnoreCase("Pharmacist")) {
            newUser = new Pharmacist(staffID, password, name, gender, role, age);
        } else if (role.equalsIgnoreCase("Administrator")) {
            newUser = new Administrator(staffID, password, name, gender, role, age, allUsers);
        } else {
            System.out.println("Invalid role. Operation aborted.");
            return;
        }

        allUsers.addUser(newUser);
        System.out.printf("Added new %s with ID: %s%n", role, staffID);
    }

    // Method to remove an existing staff member by ID
    public void removeStaffMember() {
        System.out.print("Enter the ID of the staff member to remove: ");
        String staffID = scanner.nextLine();

        if (staffID.equals(this.getHospitalID())) {
            System.out.println("You cannot remove your own account while logged in.");
            return;
        }

        User userToRemove = allUsers.findUserById(staffID);

        if (userToRemove != null) {
            allUsers.getAllUsers().remove(userToRemove);
            System.out.println("User removed successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to view appointments details
    private void viewAppointments() {
        System.out.println("---- View Appointments Details ----");
        // Implement code to display all appointment details here
        // This should include Patient ID, Doctor ID, Status, Date, and Time
    }

    // Method to manage inventory items
    public void manageInventory() {
        System.out.println("---- Manage Medication Inventory ----");
        inventory.viewInventory();
        System.out.print("Would you like to update the inventory? (yes/no): ");
        String choice = scanner.nextLine().trim().toLowerCase();
        if (choice.equals("yes")) {
            inventory.updateInventory();
            System.out.println("---- Updated Inventory ----");
            inventory.viewInventory();
        }
    }

    // Method to approve replenishment requests
    private void approveReplenishmentRequests() {
        System.out.println("---- Approve Replenishment Requests ----");
        // Implement code to approve replenishment requests here
        // This should allow the administrator to confirm requests and adjust inventory levels
    }
}
