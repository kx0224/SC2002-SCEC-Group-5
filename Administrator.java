import java.util.*;
import java.io.*;

public class Administrator extends User {
    Scanner sc = new Scanner(System.in);

    Administrator(String userID, String password, String name, String Role) {
        super(userID, password, name, Role);   
    }

    void showMenu() {
        System.out.println("1. View and Manage Hospital Staff"); //checked
        System.out.println("2. View Appointments Details");
        System.out.println("3. View and Manage Medication Inventory");//checked
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
//checked
private void viewAllStaff() {
    try {
        BufferedReader br = new BufferedReader(new FileReader("Staff_List.csv"));
        String line;
        boolean firstLine = true;
        System.out.println("\nCurrent Staff List:");
        System.out.println("Staff ID | Name | Role | Gender | Age");
        System.out.println("----------------------------------------");
        while ((line = br.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
                continue;
            }
            String[] staff = line.split(",");
            System.out.println(staff[0] + " | " + staff[1] + " | " + staff[2] + " | " + staff[3] + " | " + staff[4]);
        }
        br.close();
    } catch (IOException e) {
        System.out.println("Error reading Staff_List.csv");
    }
}
//checked
private void addStaffMember() {
    try {
        // Read all data into memory
        ArrayList<String[]> staffList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("Staff_List.csv"));
        String line;
        boolean firstLine = true;
        String header = "";
        
        while ((line = br.readLine()) != null) {
            if (firstLine) {
                header = line;
                firstLine = false;
                continue;
            }
            staffList.add(line.split(","));
        }
        br.close();

        // Get new staff details
        System.out.println("\nEnter staff details:");
        System.out.print("Staff ID: ");
        String staffID = sc.nextLine();
        
        // Check if ID exists in the ArrayList
        boolean exists = false;
        for (String[] staff : staffList) {
            if (staff[0].equals(staffID)) {
                System.out.println("Error: Staff ID already exists.");
                return;
            }
        }

        System.out.print("Name: ");
        String name = sc.nextLine();
        
        System.out.print("Role (Doctor/Pharmacist/Administrator): ");
        String role = sc.nextLine();
        
        System.out.print("Gender (M/F): ");
        String gender = sc.nextLine();
        
        System.out.print("Age: ");
        String age = sc.nextLine();

        // Add new staff to ArrayList
        String[] newStaff = {staffID, name, role, gender, age};
        staffList.add(newStaff);

        // Write all data back to file
        PrintWriter pw = new PrintWriter(new FileWriter("Staff_List.csv"));
        pw.println(header);
        for (String[] staff : staffList) {
            pw.println(String.join(",", staff));
        }
        pw.close();
        System.out.println("Staff member added successfully.");

    } catch (IOException e) {
        System.out.println("Error updating Staff_List.csv");
    }
}
//checked
private void removeStaffMember() {
    try {
        System.out.print("Enter Staff ID to remove: ");
        String staffID = sc.nextLine();

        // Read all lines except the one to remove
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("Staff_List.csv"));
        String line;
        boolean firstLine = true;
        boolean found = false;
        
        while ((line = br.readLine()) != null) {
            if (firstLine) {
                lines.add(line);
                firstLine = false;
                continue;
            }
            String[] staff = line.split(",");
            if (!staff[0].equals(staffID)) {
                lines.add(line);
            } else {
                found = true;
            }
        }
        br.close();

        if (!found) {
            System.out.println("Staff member not found.");
            return;
        }

        // Write back all lines except the removed one
        PrintWriter pw = new PrintWriter(new FileWriter("Staff_List.csv"));
        for (String l : lines) {
            pw.println(l);
        }
        pw.close();
        System.out.println("Staff member removed successfully.");

    } catch (IOException e) {
        System.out.println("Error updating Staff_List.csv");
    }
}
//checked
private void filterStaffList() {
    System.out.println("Filter by:");
    System.out.println("1. Role");
    System.out.println("2. Gender");
    System.out.println("3. Age");
    int filterChoice = sc.nextInt();
    sc.nextLine(); // Clear buffer

    try {
        BufferedReader br = new BufferedReader(new FileReader("Staff_List.csv"));
        String line;
        boolean firstLine = true;
        boolean found = false;

        switch (filterChoice) {
            case 1:
                System.out.println("Enter role (Doctor/Pharmacist/Administrator):");
                String role = sc.nextLine();
                System.out.println("\nStaff members with role " + role + ":");
                System.out.println("Staff ID | Name | Role | Gender | Age");
                System.out.println("----------------------------------------");
                while ((line = br.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }
                    String[] staff = line.split(",");
                    if (staff[2].equalsIgnoreCase(role)) {
                        System.out.println(staff[0] + " | " + staff[1] + " | " + staff[2] + " | " + staff[3] + " | " + staff[4]);
                        found = true;
                    }
                }
                break;

            case 2:
                System.out.println("Enter gender (M/F):");
                String gender = sc.nextLine();
                System.out.println("\nStaff members with gender " + gender + ":");
                System.out.println("Staff ID | Name | Role | Gender | Age");
                System.out.println("----------------------------------------");
                while ((line = br.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }
                    String[] staff = line.split(",");
                    if (staff[3].equalsIgnoreCase(gender)) {
                        System.out.println(staff[0] + " | " + staff[1] + " | " + staff[2] + " | " + staff[3] + " | " + staff[4]);
                        found = true;
                    }
                }
                break;

            case 3:
                System.out.println("Enter minimum age:");
                int minAge = sc.nextInt();
                System.out.println("Enter maximum age:");
                int maxAge = sc.nextInt();
                System.out.println("\nStaff members between " + minAge + " and " + maxAge + " years:");
                System.out.println("Staff ID | Name | Role | Gender | Age");
                System.out.println("----------------------------------------");
                while ((line = br.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }
                    String[] staff = line.split(",");
                    int age = Integer.parseInt(staff[4]);
                    if (age >= minAge && age <= maxAge) {
                        System.out.println(staff[0] + " | " + staff[1] + " | " + staff[2] + " | " + staff[3] + " | " + staff[4]);
                        found = true;
                    }
                }
                break;

            default:
                System.out.println("Invalid filter option.");
        }

        if (!found) {
            System.out.println("No staff members found matching the criteria.");
        }
        br.close();

    } catch (IOException e) {
        System.out.println("Error reading Staff_List.csv");
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
//checked
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
//checked
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
//checked
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
