package model;

import controller.AppointmentController;
import controller.InventoryController;
import controller.StaffController;
import dao.AppointmentDAO;
import dao.StaffDAO;
import view.AdminMenu;
import java.util.List;
import java.util.Scanner;

public class Administrator extends User {
    private InventoryController inventoryController;
    private AppointmentController appointmentController;
    private StaffController staffController;
    private List<Medicine> medicines;
    private static final Scanner scanner = new Scanner(System.in); // Reuse Scanner instance to prevent resource leak

    // Constructor
    public Administrator(String userId, String password, String name, String dateOfBirth, String contactInformation, String gender) {
        super(userId, password, name, dateOfBirth, contactInformation, gender, UserRole.ADMINISTRATOR);
        this.inventoryController = new InventoryController();
        this.appointmentController = new AppointmentController();
        this.staffController = new StaffController(new StaffDAO());
        this.medicines = inventoryController.loadMedicines();
    }

    @Override
    public void showMenu() {
        AdminMenu.showMenu(this);
    }

    // Enhanced Method to manage inventory
    public void manageInventory() {
        boolean managingInventory = true;

        while (managingInventory) {
            System.out.println("\nInventory Management");
            System.out.println("1. View Inventory");
            System.out.println("2. Update Low Stock Alert");
            System.out.println("3. Approve Replenishment Requests");
            System.out.println("4. View Low Stock Medicines");
            System.out.println("5. Return to Previous Menu");
            System.out.print("Choose an option: ");

            int choice = getInputAsInt();

            switch (choice) {
                case 1:
                    inventoryController.showInventoryMenu();
                    break;
                case 2:
                    manageLowStockAlert();
                    break;
                case 3:
                    approveReplenishmentRequests();
                    break;
                case 4:
                    viewLowStockMedicines();
                    break;
                case 5:
                    managingInventory = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    // Method to manage low stock alerts
    private void manageLowStockAlert() {
        System.out.print("Enter Medicine ID: ");
        String medicineId = scanner.nextLine();
        System.out.print("Enter new low stock alert level: ");
        int newLowStockLevel = getInputAsInt();
        boolean success = inventoryController.updateLowStockAlert(medicineId, newLowStockLevel);
        if (success) {
            System.out.println("Low stock alert level updated successfully.");
        } else {
            System.out.println("Failed to update. Medicine not found.");
        }
    }

    // Enhanced Method to approve replenishment requests
    public void approveReplenishmentRequests() {
        inventoryController.approveReplenishmentRequests(medicines);
    }

    // Method to view low stock medicines
    private void viewLowStockMedicines() {
        List<Medicine> lowStockMedicines = inventoryController.getLowStockMedicines();
        if (lowStockMedicines.isEmpty()) {
            System.out.println("No medicines with low stock.");
        } else {
            System.out.println("\nLow Stock Medicines:");
            for (Medicine medicine : lowStockMedicines) {
                System.out.println("- " + medicine.getName() + " (Current Stock: " + medicine.getStockLevel() + ")");
            }
        }
    }

    // Method to manage hospital staff
    public void manageStaff() {
        boolean managingStaff = true;

        while (managingStaff) {
            System.out.println("\nHospital Staff Management");
            System.out.println("1. View All Staff");
            System.out.println("2. Add New Staff Member");
            System.out.println("3. Remove Staff Member");
            System.out.println("4. Update Staff Information");
            System.out.println("5. View Staff by Role");
            System.out.println("6. View Staff by Gender");
            System.out.println("7. Return to Previous Menu");
            System.out.print("Choose an option: ");

            int choice = getInputAsInt();

            switch (choice) {
                case 1 -> staffController.viewAllStaff();
                case 2 -> addNewStaffMember();
                case 3 -> removeStaffMember();
                case 4 -> updateStaffInformation();
                case 5 -> staffController.viewStaffByRole();
                case 6 -> staffController.viewStaffByGender();
                case 7 -> managingStaff = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addNewStaffMember() {
        System.out.print("Enter Staff ID: ");
        String staffId = scanner.nextLine();
        if (staffController.viewAllStaff().stream().anyMatch(staff -> staff.getUserId().equals(staffId))) {
            System.out.println("Staff ID already exists. Please use a unique Staff ID.");
            return;
        }
        System.out.print("Enter Staff Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Staff Role (DOCTOR/PHARMACIST/ADMINISTRATOR): ");
        String roleInput = scanner.nextLine().toUpperCase();
        UserRole role;
        try {
            role = UserRole.valueOf(roleInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role. Staff member not added.");
            return;
        }
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Contact Information: ");
        String contactInformation = scanner.nextLine();
        System.out.print("Enter Date of Birth (yyyy-mm-dd): ");
        String dateOfBirth = scanner.nextLine();
        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();

        User newStaff;
        switch (role) {
            case DOCTOR -> newStaff = new Doctor(staffId, password, name, dateOfBirth, contactInformation, gender);
            case PHARMACIST -> newStaff = new Pharmacist(staffId, password, name, dateOfBirth, contactInformation, gender);
            case ADMINISTRATOR -> newStaff = new Administrator(staffId, password, name, dateOfBirth, contactInformation, gender);
            default -> {
                System.out.println("Invalid role. Staff member not added.");
                return;
            }
        }

        try {
            boolean success = staffController.addStaffMember(newStaff);
            if (success) {
                System.out.println("Staff member added successfully.");
            } else {
                System.out.println("Failed to add staff member.");
            }
        } catch (Exception e) {
            System.out.println("Error while adding staff member: " + e.getMessage());
        }
    }

    private void removeStaffMember() {
        System.out.print("Enter Staff ID to Remove: ");
        String staffId = scanner.nextLine();
        try {
            boolean success = staffController.removeStaffMember(staffId);
            if (success) {
                System.out.println("Staff member removed successfully.");
            } else {
                System.out.println("Staff member not found.");
            }
        } catch (Exception e) {
            System.out.println("Error while removing staff member: " + e.getMessage());
        }
    }

    private void updateStaffInformation() {
        System.out.print("Enter Staff ID to Update: ");
        String staffId = scanner.nextLine();
        System.out.print("Enter new contact information: ");
        String newContactInfo = scanner.nextLine();
        try {
            boolean success = staffController.updateStaffInformation(staffId, newContactInfo);
            if (success) {
                System.out.println("Staff information updated successfully.");
            } else {
                System.out.println("Staff member not found.");
            }
        } catch (Exception e) {
            System.out.println("Error while updating staff information: " + e.getMessage());
        }
    }

    // Method to manage appointments
    public void manageAppointments() {
        boolean managingAppointments = true;

        while (managingAppointments) {
            System.out.println("\nAppointment Management");
            System.out.println("1. View All Appointments");
            System.out.println("2. Schedule New Appointment");
            System.out.println("3. Cancel Appointment");
            System.out.println("4. Update Appointment Details");
            System.out.println("5. Record Appointment Outcome");
            System.out.println("6. Reschedule Appointment");
            System.out.println("7. Return to Previous Menu");
            System.out.print("Choose an option: ");

            int choice = getInputAsInt();

            switch (choice) {
                case 1:
                    viewAllAppointments();
                    break;
                case 2:
                    scheduleNewAppointment();
                    break;
                case 3:
                    cancelAppointment();
                    break;
                case 4:
                    updateAppointmentDetails();
                    break;
                case 5:
                    recordAppointmentOutcome();
                    break;
                case 6:
                    rescheduleAppointment();
                    break;
                case 7:
                    managingAppointments = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private int getInputAsInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private void viewAllAppointments() {
        try {
            List<Appointment> allAppointments = appointmentController.getAllAppointments();
            if (allAppointments.isEmpty()) {
                System.out.println("No appointments found.");
            } else {
                System.out.println("\nAppointments:");
                for (Appointment appointment : allAppointments) {
                    System.out.println(appointment);
                }
            }
        } catch (Exception e) {
            System.out.println("Error while fetching appointments: " + e.getMessage());
        }
    }

    private void scheduleNewAppointment() {
        System.out.print("Enter Appointment ID: ");
        String appointmentId = scanner.nextLine();
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();
        System.out.print("Enter Appointment Date (yyyy-mm-dd): ");
        String date = scanner.nextLine();
        System.out.print("Enter Appointment Time: ");
        String time = scanner.nextLine();

        Appointment newAppointment = new Appointment(appointmentId, doctorId, patientId, date, time, "Scheduled");
        try {
            boolean success = appointmentController.scheduleAppointment(newAppointment);
            if (success) {
                System.out.println("Appointment scheduled successfully.");
            } else {
                System.out.println("Failed to schedule the appointment. Please check for conflicts.");
            }
        } catch (Exception e) {
            System.out.println("Error while scheduling appointment: " + e.getMessage());
        }
    }

    private void cancelAppointment() {
        System.out.print("Enter Appointment ID to Cancel: ");
        String appointmentId = scanner.nextLine();
        try {
            boolean success = appointmentController.cancelAppointment(appointmentId);
            if (success) {
                System.out.println("Appointment canceled successfully.");
            } else {
                System.out.println("Appointment not found.");
            }
        } catch (Exception e) {
            System.out.println("Error while canceling appointment: " + e.getMessage());
        }
    }

    private void updateAppointmentDetails() {
        System.out.print("Enter Appointment ID to Update: ");
        String appointmentId = scanner.nextLine();
        System.out.print("Enter new Appointment Date (yyyy-mm-dd): ");
        String newDate = scanner.nextLine();
        System.out.print("Enter new Appointment Time: ");
        String newTime = scanner.nextLine();
        try {
            boolean success = appointmentController.updateAppointmentDetails(appointmentId, newDate, newTime);
            if (success) {
                System.out.println("Appointment details updated successfully.");
            } else {
                System.out.println("Appointment not found or conflict exists.");
            }
        } catch (Exception e) {
            System.out.println("Error while updating appointment details: " + e.getMessage());
        }
    }

    private void recordAppointmentOutcome() {
        System.out.print("Enter Appointment ID to Record Outcome: ");
        String appointmentId = scanner.nextLine();
        System.out.print("Enter Appointment Outcome: ");
        String outcome = scanner.nextLine();
        try {
            boolean success = appointmentController.recordAppointmentOutcome(appointmentId, outcome);
            if (success) {
                System.out.println("Appointment outcome recorded successfully.");
            } else {
                System.out.println("Appointment not found.");
            }
        } catch (Exception e) {
            System.out.println("Error while recording appointment outcome: " + e.getMessage());
        }
    }

    private void rescheduleAppointment() {
        System.out.print("Enter Appointment ID to Reschedule: ");
        String appointmentId = scanner.nextLine();
        System.out.print("Enter new Appointment Date (yyyy-mm-dd): ");
        String newDate = scanner.nextLine();
        System.out.print("Enter new Appointment Time: ");
        String newTimeSlot = scanner.nextLine();
        try {
            boolean success = appointmentController.rescheduleAppointment(appointmentId, newDate, newTimeSlot);
            if (success) {
                System.out.println("Appointment rescheduled successfully.");
            } else {
                System.out.println("Appointment not found or conflict exists.");
            }
        } catch (Exception e) {
            System.out.println("Error while rescheduling appointment: " + e.getMessage());
        }
    }
}
