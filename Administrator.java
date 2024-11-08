import java.util.*;
import java.io.*;

public class Administrator extends User {
    private ArrayList<User> staffList = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    Administrator(String userID, String password, String name, String Role) {
        super(userID, password, name, Role);
        importStaffList();
    }

    void showMenu() {
        System.out.println("1. View and Manage Hospital Staff");
        System.out.println("2. View Appointments Details");
        System.out.println("3. View and Manage Medication Inventory");
        System.out.println("4. Approve Replenishment Requests");
        System.out.println("5. Logout");
    }

    boolean performAction(int option) {
        switch (option) {
            case 1:
                manageHospitalStaff();
                break;
            case 2:
                viewAppointmentsDetails();
                break;
            case 3:
                manageMedicationInventory();
                break;
            case 4:
                approveReplenishmentRequests();
                break;
            case 5:
                sc.close();
                System.out.println("Logging out...");
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }

    private void importStaffList() {
        String line = "";
        String splitBy = ",";
        try {
            BufferedReader br = new BufferedReader(new FileReader("Staff_List.csv"));
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] staff = line.split(splitBy);
                String staffID = staff[0];
                String name = staff[1];
                String role = staff[2];
                String gender = staff[3];
                int age = Integer.parseInt(staff[4]);
                staffList.add(new User(staffID, role));
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading Staff_List.csv");
        }
    }

    private void manageHospitalStaff() {
        int choice = 0;
        boolean managing = true;
        while (managing) {
            System.out.println("\n1. View all staff");
            System.out.println("2. Add staff member");
            System.out.println("3. Remove staff member");
            System.out.println("4. Filter staff list");
            System.out.println("5. Return to main menu");
            System.out.println("Enter your choice:");
            
            choice = sc.nextInt();
            sc.nextLine(); // Clear buffer
            
            switch (choice) {
                case 1:
                    viewAllStaff();
                    break;
                case 2:
                    addStaffMember();
                    break;
                case 3:
                    removeStaffMember();
                    break;
                case 4:
                    filterStaffList();
                    break;
                case 5:
                    managing = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void viewAllStaff() {
        System.out.println("Current Staff List:");
        for (User staff : staffList) {
            System.out.println("ID: " + staff.getHospitalID() + ", Role: " + staff.getRole());
        }
    }

    private void filterStaffList() {
        System.out.println("Filter by:");
        System.out.println("1. Role");
        System.out.println("2. Gender");
        System.out.println("3. Age");
        int filterChoice = sc.nextInt();
        sc.nextLine(); // Clear buffer

        switch (filterChoice) {
            case 1:
                System.out.println("Enter role (Doctor/Pharmacist/Administrator):");
                String role = sc.nextLine();
                for (User staff : staffList) {
                    if (staff.getRole().equalsIgnoreCase(role)) {
                        System.out.println("ID: " + staff.getHospitalID() + ", Role: " + staff.getRole());
                    }
                }
                break;
            // Add more filter cases as needed
            default:
                System.out.println("Invalid filter option.");
        }
    }

    private void viewAppointmentsDetails() {
        System.out.println("Enter date (MM/DD):");
        String date = sc.nextLine();
        String[] dateParts = date.split("/");
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);

        for (Doctor doc : getDoctors()) {
            System.out.println("Doctor: " + doc.getName());
            for (int slot = 0; slot < 10; slot++) {
                String appointment = doc.schedule[month-1][day-1][slot];
                if (appointment != null) {
                    System.out.println("Slot " + (slot+1) + ": " + appointment);
                }
            }
        }
    }

    private void manageMedicationInventory() {
    int choice = 0;
    boolean managing = true;
    while (managing) {
        System.out.println("\n1. View inventory");
        System.out.println("2. Update stock level");
        System.out.println("3. Update alert level");
        System.out.println("4. Return to main menu");
        System.out.println("Enter your choice:");
        
        choice = sc.nextInt();
        sc.nextLine(); // Clear buffer
        
        switch (choice) {
            case 1:
                viewInventory();
                break;
            case 2:
                updateStockLevel();
                break;
            case 3:
                updateAlertLevel();
                break;
            case 4:
                managing = false;
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}

private void viewInventory() {
    try {
        BufferedReader br = new BufferedReader(new FileReader("Medicine_List.csv"));
        String line;
        boolean firstLine = true;
        System.out.println("\nCurrent Medication Inventory:");
        System.out.println("Medicine Name | Stock | Low Stock Alert Level");
        System.out.println("----------------------------------------");
        while ((line = br.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
                continue;
            }
            String[] med = line.split(",");
            System.out.println(med[0] + " | " + med[1] + " | " + med[2]);
        }
        br.close();
    } catch (IOException e) {
        System.out.println("Error reading Medicine_List.csv");
    }
}

private void updateStockLevel() {
    try {
        // First, read all data into memory
        ArrayList<String[]> medicines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("Medicine_List.csv"));
        String line;
        boolean firstLine = true;
        String header = "";
        
        while ((line = br.readLine()) != null) {
            if (firstLine) {
                header = line;
                firstLine = false;
                continue;
            }
            medicines.add(line.split(","));
        }
        br.close();

        // Get medicine to update
        System.out.println("Enter medicine name:");
        String medName = sc.nextLine();
        
        boolean found = false;
        for (String[] med : medicines) {
            if (med[0].equals(medName)) {
                System.out.println("Current stock: " + med[1]);
                System.out.println("Enter new stock level:");
                String newStock = sc.nextLine();
                med[1] = newStock;
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Medicine not found.");
            return;
        }

        // Write updated data back to file
        PrintWriter pw = new PrintWriter(new FileWriter("Medicine_List.csv"));
        pw.println(header);
        for (String[] med : medicines) {
            pw.println(String.join(",", med));
        }
        pw.close();
        System.out.println("Stock updated successfully.");

    } catch (IOException e) {
        System.out.println("Error updating Medicine_List.csv");
    }
}

private void updateAlertLevel() {
    try {
        // First, read all data into memory
        ArrayList<String[]> medicines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("Medicine_List.csv"));
        String line;
        boolean firstLine = true;
        String header = "";
        
        while ((line = br.readLine()) != null) {
            if (firstLine) {
                header = line;
                firstLine = false;
                continue;
            }
            medicines.add(line.split(","));
        }
        br.close();

        // Get medicine to update
        System.out.println("Enter medicine name:");
        String medName = sc.nextLine();
        
        boolean found = false;
        for (String[] med : medicines) {
            if (med[0].equals(medName)) {
                System.out.println("Current alert level: " + med[2]);
                System.out.println("Enter new alert level:");
                String newAlert = sc.nextLine();
                med[2] = newAlert;
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Medicine not found.");
            return;
        }

        // Write updated data back to file
        PrintWriter pw = new PrintWriter(new FileWriter("Medicine_List.csv"));
        pw.println(header);
        for (String[] med : medicines) {
            pw.println(String.join(",", med));
        }
        pw.close();
        System.out.println("Alert level updated successfully.");

    } catch (IOException e) {
        System.out.println("Error updating Medicine_List.csv");
    }
}

    private void approveReplenishmentRequests() {
        if (HospitalManagementSystem.replenishmentRequests.isEmpty()) {
            System.out.println("No pending replenishment requests.");
            return;
        }

        System.out.println("Pending Replenishment Requests:");
        for (String request : HospitalManagementSystem.replenishmentRequests) {
            Medicine med = HospitalManagementSystem.getMedicationInventory().get(request);
            System.out.println("Medicine: " + request + 
                             " | Current Stock: " + med.getStock() + 
                             " | Alert Level: " + med.getLowStockAlert());
        }

        System.out.println("Enter medicine name to approve (or 'exit' to return):");
        String medName = sc.nextLine();
        
        if (!medName.equalsIgnoreCase("exit")) {
            Medicine med = HospitalManagementSystem.getMedicationInventory().get(medName);
            if (med != null && HospitalManagementSystem.replenishmentRequests.contains(medName)) {
                System.out.println("Enter quantity to add:");
                int quantity = sc.nextInt();
                sc.nextLine(); // Clear buffer
                med.setStock(med.getStock() + quantity);
                HospitalManagementSystem.replenishmentRequests.remove(medName);
                System.out.println("Replenishment approved and stock updated.");
            } else {
                System.out.println("Invalid medicine name or no replenishment request found.");
            }
        }
    }

    private ArrayList<Doctor> getDoctors() {
        ArrayList<Doctor> doctors = new ArrayList<>();
        for (User staff : staffList) {
            if (staff.getRole().equals("Doctor")) {
                doctors.add((Doctor)staff);
            }
        }
        return doctors;
    }
}
