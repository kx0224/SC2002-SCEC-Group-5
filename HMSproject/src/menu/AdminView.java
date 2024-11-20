package menu;
import java.util.List;
import java.util.*;
import java.util.Scanner;
import controllers.AdminController;
import models.Appointment;
import models.Inventory;
import models.Medication;
import models.ReplenishmentRequest;
import models.User;
import roles.*;
public class AdminView {
    private AdminController adminController;
    private Scanner sc = new Scanner(System.in);

    public AdminView(AdminController adminController) {
        this.adminController = adminController;
    }
    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    public int displayMenu() {
        Scanner sc = new Scanner(System.in);
        try{
            System.out.println("\n--- Administrator Menu ---");
            System.out.println("1. Add Staff");
            System.out.println("2. Update Staff");
            System.out.println("3. Remove Staff");
            System.out.println("4. View Staff");
            System.out.println("5. View All Appointments");
            System.out.println("6. View Appointment Details");
            System.out.println("7. Approve Replenishment Request");
            System.out.println("8. Reject Replenishment Request");
            System.out.println("9. View Replenishment Requests");
            System.out.println("10. View Inventory");
            System.out.println("11. Update Inventory");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            return sc.nextInt();}
        catch (Exception e) {
            System.out.println("Invalid input. Please enter a number between 0 and 11.");
            sc.nextLine(); // Clear the buffer
            return -1; // Invalid choice
        }

    }
    public String promptStaffID() {
        System.out.print("StaffID: ");
        return sc.nextLine().trim();
    }
    public String promptMedID() {
        System.out.print("Enter Medicine ID: ");
        return sc.nextLine().trim();
    }
    public Integer promptQuantity() {
        try {
            System.out.print("Enter Quantity you want to add: ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Invalid input.");
                return -1;
            }
            return Integer.parseInt(input); // Parse the input as an integer
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return -1; // Return an invalid value to prompt the user again
        }
    }




    public void showMessage(String message) {
        System.out.println(message);
    }

    public void displayAppointments(Map<String, Appointment> appointments) {
        int index = 1;
        if (appointments.isEmpty()) {
            System.out.println("No Appointments.");
            return; // Exit if no appointments to display
        }

        // Iterate over the map values to display each appointment
        for (Appointment slot : appointments.values()) {
            System.out.println("Index: " + index);
            System.out.println("Appointment ID: " + slot.get_appointment_id());
            System.out.println("Doctor ID: " + slot.get_doctor_id());
            System.out.println("Doctor Name: " + slot.get_doctor_name());
            System.out.println("Date: " + slot.get_date());
            System.out.println("Time: " + slot.get_startTime());
            System.out.println("----------------------------------");
            index++;
        }
    }

    public int viewStaffMenu() {
        try{
        System.out.println("\n--- View Staff ---");
        System.out.println("1. Filter by Role");
        System.out.println("2. Filter by Gender");
        System.out.println("3. All");
        System.out.println("Your Choice: ");
        String input = sc.nextLine().trim();
        return Integer.parseInt(input); // Parse the input as an integer
    } catch (NumberFormatException e)

    {
        System.out.println("Invalid input. Please enter a number between 1 and 9.");
        return -1; // Return an invalid value to prompt the user again
    }

    }

    public String promptRole() {
        System.out.println("\n--- View Staff by Role ---");
        System.out.print("Enter role: ");
        return sc.nextLine().trim();
    }
    public String promptGender() {
        System.out.print("Enter gender to filter by (e.g., Male, Female): ");
        return sc.nextLine().trim();
    }
    public void displayStaff(Map<String, User> stafflist) {
        System.out.println("\n--- Displaying Staff ---");
        for (Map.Entry<String, User> entry : stafflist.entrySet()) {
            User staff = entry.getValue();
            System.out.println("Staff ID: " + staff.getHospitalID());
            System.out.println("Name: " + staff.getName());
            System.out.println("Role: " + staff.getRole());
            System.out.println("Gender: " + staff.getGender());

            if (staff instanceof Doctor) {
                System.out.println("Age: " + ((Doctor) staff).getAge());
            } else if (staff instanceof Pharmacist) {
                System.out.println("Age: " + ((Pharmacist) staff).getAge());
            } else if (staff instanceof Administrator) {
                System.out.println("Age: " + ((Administrator) staff).getAge());
            } else {
                System.out.println("Age: Not applicable");
            }

            System.out.println("Phone Number: " + staff.getPhonenumber());
            System.out.println("Email: " + staff.getEmail());
            System.out.println("-----------------------------------");
        }
    }


    public String prompt_reqID() {
        System.out.print("Enter request ID: ");
        return sc.nextLine().trim();
    }
    public String prompt_ApptID() {
        System.out.print("Enter Appointment ID: ");
        return sc.nextLine().trim();
    }



    public void viewReplenishmentRequests() {
        System.out.println("\n--- View Replenishment Requests ---");
        List<models.ReplenishmentRequest> requests = adminController.getReplenishmentRequests();

        if (requests.isEmpty()) {
            System.out.println("No pending replenishment requests.");
        } else {
            for (ReplenishmentRequest request : requests) {
                System.out.println("Request ID: "+request.getReplenishmentID());
                System.out.println("Med ID: "+request.getMedicationID());
                System.out.println("Quantity: "+request.getQuantity());
                System.out.println("Status: "+request.getStatus());
                System.out.println("-----------------------------------");
            }
        }
    }

    public void viewMedicationInventory(Inventory medicationInventory) {
        System.out.println("\n--- Inventory ---");;
        for (Medication medication : medicationInventory.getInventory()) {
            System.out.println("Medication ID: " + medication.getMed_id());
            System.out.println("Name: " + medication.getName());
            System.out.println("Stock Count: " + medication.getStock());
            System.out.println("Low Stock level: " +medication.getLowStockAlert());
            System.out.println("-----------------------------------");
        }
    }
}