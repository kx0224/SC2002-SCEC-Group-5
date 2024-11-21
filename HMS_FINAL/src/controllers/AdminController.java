package controllers;

import java.util.*;
import java.util.stream.Collectors;
import managers.AppointmentManager;
import menu.AdminView;
import models.Inventory;
import models.Medication;
import models.ReplenishmentRequest;
import models.ReplenishmentStatus;
import models.User;
import models.UserRoles;
import users.*;

public class AdminController extends UserController {
    private AppointmentManager manageAppt;
    private ReplenishmentRequestController replenishmentRequestController;
    private Inventory inventory;
    private AdminView adminView;
    private Administrator administrator;


    public AdminController(AppointmentManager manageAppt,
                           ReplenishmentRequestController replenishmentRequestController,
                           Inventory inventory,
                           AdminView adminView, medicalRecordsController medicalRecordsController, Administrator administrator) {
        super(medicalRecordsController);
        this.manageAppt = manageAppt;
        this.replenishmentRequestController = replenishmentRequestController;
        this.inventory = inventory;
        this.adminView = adminView;
        this.administrator = administrator;
    }

    public void showMenu() {
        boolean running = true;
        String StaffID, Req_id;
        int input;
        while (running) {
            try {
                int choice = adminView.displayMenu();
                switch (choice) {
                    case 1 -> addStaff();
                    case 2 -> {
                        System.out.println("\n--- Update Staff ---");
                        adminView.displayStaff(getFilteredStaffList());
                        System.out.println("Enter ID of the Staff you want to update ");
                        StaffID = adminView.promptStaffID().toUpperCase();
                        updateStaff(StaffID);
                    }
                    case 3 -> {
                        System.out.println("\n--- Remove Staff ---");
                        System.out.println("Enter ID of the Staff you want to remove ");
                        StaffID = adminView.promptStaffID().toUpperCase();
                        removeStaff(StaffID);
                    }
                    case 4 -> {
                        input = adminView.viewStaffMenu();
                        switch (input) {
                            case 1 -> {
                                String role = adminView.promptRole();
                                viewStaffByRole(role);
                            }
                            case 2 -> {
                                String gender = adminView.promptGender();
                                viewStaffByGender(gender);
                            }
                            case 3 -> {
                                adminView.displayStaff(getFilteredStaffList());
                            }
                        }
                    }
                    case 5 -> viewAppointments();
                    
                    case 6 -> {
                        System.out.println("\n--- Approve Replenishment Request ---");
                        Req_id = adminView.prompt_reqID();
                        approveReplenishmentRequest(Req_id);
                    }
                    case 7 -> {
                        System.out.println("\n--- Reject Replenishment Request ---");
                        Req_id = adminView.prompt_reqID();
                        rejectReplenishmentRequest(Req_id);
                    }
                    case 8 -> adminView.viewReplenishmentRequests();
                    case 9 -> adminView.viewMedicationInventory(inventory);
                    case 10 -> updateStock();
                    case 0 -> {
                        System.out.println("Logging out...");

                        running = false;
                    }
                    default -> {
                        adminView.showMessage("Invalid choice. Please try again.");
                        break;
                    }

                }
            } catch (InputMismatchException e) {
                adminView.showMessage("Invalid input. Please enter a valid number.");
            } catch (Exception e) {
                adminView.showMessage("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    public void updateStaff(String staffID) {
        Scanner scanner = new Scanner(System.in);
        User staffToUpdate = getUserById(staffID);

        if (staffToUpdate == null) {
            System.out.println("Staff with ID " + staffID + " not found.");
            return;
        }

        // Prompt for and update common attributes
        System.out.println("Enter new name (current: " + staffToUpdate.getName() + ") (leave blank if not changing): ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            staffToUpdate.setName(newName);
        }
        if (staffToUpdate instanceof Doctor doctor) {
            System.out.println("Enter new age (leave blank if not changing): ");
            String newAge = scanner.nextLine().trim();
            if (!newAge.isEmpty()) {
                try {
                    int new_Age = Integer.parseInt(newAge);
                    if (new_Age > 0) {
                        doctor.setAge(new_Age);
                        System.out.println("Age updated to: " + newAge);
                    } else {
                        System.out.println("Error: Age must be a positive integer.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Age must be a valid integer.");
                }
            }

        } else if (staffToUpdate instanceof Pharmacist pharmacist) {
            System.out.println("Enter new age (leave blank if not changing): ");
            String newAge = scanner.nextLine().trim();
            if (!newAge.isEmpty()) {
                try {
                    int new_Age = Integer.parseInt(newAge);
                    if (new_Age > 0) {
                        pharmacist.setAge(new_Age);
                        System.out.println("Age updated to: " + newAge);
                    } else {
                        System.out.println("Error: Age must be a positive integer.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Age must be a valid integer.");
                }
            }
        } else if (staffToUpdate instanceof Administrator administrator) {
            System.out.println("Enter new age (leave blank if not changing): ");
            String newAge = scanner.nextLine().trim();
            if (!newAge.isEmpty()) {
                try {
                    int new_Age = Integer.parseInt(newAge);
                    if (new_Age > 0) {
                        administrator.setAge(new_Age);
                        System.out.println("Age updated to: " + newAge);
                    } else {
                        System.out.println("Error: Age must be a positive integer.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Age must be a valid integer.");
                }
            }
        }
        System.out.println("Enter new email (leave blank if not changing): ");
        String newEmail = scanner.nextLine().trim();
        if (!newEmail.isEmpty()) {
            staffToUpdate.setEmail(newEmail);
        }
        System.out.println("Enter new Contact Number (leave blank if not changing): ");
        String newcontact = scanner.nextLine().trim();
        if (!newcontact.isEmpty()) {
            staffToUpdate.setPhonenumber(newcontact);


        }
    }

    void addStaff() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter new staff member details...");
        System.out.println("Enter role (Admin/Doctor/Pharmacist): ");
        String role = sc.nextLine().toUpperCase();
        System.out.println(role);
        System.out.println("Enter name: ");
        String name = sc.nextLine();
        System.out.println("Enter gender: ");
        String gender = sc.nextLine();
        System.out.println("Enter age: ");
        String age = sc.nextLine();
        Integer int_age = 0;
        if (!age.isEmpty()) {
            try {
                int_age = Integer.parseInt(age);
                if (int_age < 0) {
                    System.out.println("Error: Age must be a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Age must be a valid integer.");
            }
        }
        System.out.println("Enter email address: ");
        String email = sc.nextLine();
        System.out.println("Enter phone number: ");
        String phonenumber = sc.nextLine();
        User newStaff = null;
        String hospitalID = null;
        switch (role) {
            case "DOCTOR":
                hospitalID = generateHospitalID(UserRoles.DOCTOR);
                newStaff = new Doctor(hospitalID, "password", UserRoles.DOCTOR, name, gender, int_age, email, phonenumber);
                users.get(UserRoles.DOCTOR).add(newStaff);
                System.out.println("Doctor added successfully.");
                break;
            case "PHARMACIST":
                hospitalID = generateHospitalID(UserRoles.PHARMACIST);
                newStaff = new Pharmacist(hospitalID, "password", UserRoles.PHARMACIST, name, gender, int_age, email, phonenumber);
                users.get(UserRoles.PHARMACIST).add(newStaff);
                System.out.println("Pharmacist added successfully.");
                break;
            case "ADMINISTRATOR":
                hospitalID = generateHospitalID(UserRoles.ADMIN);
                newStaff = new Administrator(hospitalID, "password", UserRoles.ADMIN, name, gender, int_age, email, phonenumber);
                users.get(UserRoles.ADMIN).add(newStaff);
                System.out.println("Admin added successfully.");
                break;
            default:
                System.out.println("Invalid role. Please enter Doctor, Pharmacist, or Admin.");

        }
    }

    public Map<String, User> getFilteredStaffList() {
        Map<String, User> filteredStaff = new HashMap<>();

        for (Map.Entry<UserRoles, List<User>> entry : users.entrySet()) {
            UserRoles role = entry.getKey();
            if (role == UserRoles.DOCTOR || role == UserRoles.PHARMACIST || role == UserRoles.ADMIN) {
                for (User user : entry.getValue()) {
                    filteredStaff.put(user.getHospitalID(), user);
                }
            }
        }

        return filteredStaff;
    }

    public void removeStaff(String staffID) {
        boolean removed = false;
        // loop over user roles
        for (Map.Entry<UserRoles, List<User>> staff : users.entrySet()) {
            // get list of users under that role
            List<User> userList = staff.getValue();
            Iterator<User> iterator = userList.iterator();
            // loop through list
            while (iterator.hasNext()) {
                User user = iterator.next();
                if (user.getHospitalID().equals(staffID)) {
                    iterator.remove();
                    removed = true;
                    System.out.println("Staff with ID " + staffID + " removed.");
                    break; // Exit loop after finding and removing the user
                }
            }
            if (removed) break; // Exit outer loop if a user was removed
        }

        if (!removed) {
            System.out.println("Staff with ID " + staffID + " not found.");
        }
    }

    public void viewStaffByRole(String role) {
        UserRoles userRole;
        try {
            // Convert to uppercase to match enum values
            userRole = UserRoles.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Print an error message and return if the role is invalid
            System.out.println("Invalid role. Please enter a valid role (e.g., DOCTOR, PHARMACIST, ADMIN).");
            return; // Exit the method
        }

        // Retrieve the list of users by the given role and convert it to a map
        List<User> userList = users.getOrDefault(userRole, Collections.emptyList());
        if (userList.isEmpty()) {
            System.out.println("No staff found for the role: " + role);
            return; // Exit the method if no users are found
        }

        // Convert the list to a map with the hospital ID as the key
        Map<String, User> userMap = userList.stream()
                .collect(Collectors.toMap(User::getHospitalID, user -> user));

        // Display the staff using the admin view
        adminView.displayStaff(userMap);
    }


    public void viewStaffByGender(String gender) {
        Map<String, User> filteredStaff = new HashMap<>();
        for (List<User> userList : users.values()) {
            userList.stream()
                    .filter(user -> user.getGender().equalsIgnoreCase(gender))
                    .forEach(user -> filteredStaff.put(user.getHospitalID(), user));
        }

        if (filteredStaff.isEmpty()) {
            System.out.println("No staff found for the gender: " + gender);
        } else {
            adminView.displayStaff(filteredStaff);
        }
    }


    public void viewAppointments() {
        adminView.displayAppointments(manageAppt.getAllAppointments());
    }

    public void viewAppointmentDetails(String appointmentID) {
        manageAppt.getAppointmentById(appointmentID);
    }

    public void approveReplenishmentRequest(String requestID) {
        replenishmentRequestController.approveReplenishmentRequest(requestID);
//        System.out.println("Replenishment request " + requestID + " approved.");
    }

    public void rejectReplenishmentRequest(String requestID) {
        replenishmentRequestController.rejectReplenishmentRequest(requestID);
//        System.out.println("Replenishment request " + requestID + " rejected.");
    }

    public List<ReplenishmentRequest> getReplenishmentRequests() {
        List<ReplenishmentRequest> pendingRequests = replenishmentRequestController.viewReplenishmentRequests(ReplenishmentStatus.PENDING);
        return pendingRequests;
    }
    public void updateStock(){
        String medID = adminView.promptMedID();
        Medication medication = inventory.getMedicationByID(medID);
        adminView.showMessage("Current Stock of "+medication.getName()+ ": "+medication.getStock());
        Integer updateQuantity = adminView.promptQuantity();
        boolean success = inventory.updateStock(medID,updateQuantity);
        if (success){
            System.out.println("Updated stock for " + medication.getName() + " to " + medication.getStock());
        }
        else {
            System.out.println("Medication not found with ID: " + medID);
        }
    }
}