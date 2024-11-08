package testingpart3;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Pharmacist extends User {
    private List<Appointment> appointments;
    private HashMap<String, Medicine> medicationInventory;
    private Scanner scanner = new Scanner(System.in);

    public Pharmacist(String userID, String password, String name, List<Appointment> appointments, HashMap<String, Medicine> medicationInventory) {
        super(userID, password, "Pharmacist", name);
        this.appointments = appointments;
        this.medicationInventory = medicationInventory;
    }

    @Override
    void showMenu() {
        System.out.println("● View Appointment Outcome Record");
        System.out.println("● Update Prescription Status");
        System.out.println("● View Medication Inventory");
        System.out.println("● Submit Replenishment Request");
        System.out.println("● Logout");
    }

    @Override
    boolean performAction(int option) {
        switch (option) {
            case 1:
                viewAppointmentOutcomeRecord();
                break;
            case 2:
                updatePrescriptionStatus();
                break;
            case 3:
                viewMedicationInventory();
                break;
            case 4:
                submitReplenishmentRequest();
                break;
            case 5:
                System.out.println("Logging out...");
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }

    void viewAppointmentOutcomeRecord() {
        System.out.print("Enter Patient Name to view appointment outcomes: ");
        String patientName = scanner.nextLine().trim();

        boolean found = false;
        for (Appointment appointment : appointments) {
            if (appointment.pname.equals(patientName)) {
                found = true;
                appointment.showdetails();
            }
        }

        if (!found) {
            System.out.println("No appointment outcome found for the given Patient Name.");
        }
    }

    void updatePrescriptionStatus() {
        System.out.print("Enter Patient Name to dispense medication: ");
        String patientName = scanner.nextLine().trim();

        // Find appointment based on patient name
        Appointment appointment = findAppointmentByPatientName(patientName);
        if (appointment == null) {
            System.out.println("Invalid Patient Name. Please try again.");
            return;
        }

        System.out.println("Dispensing medications for patient: " + patientName);
        boolean dispensing = true;
        while (dispensing) {
            System.out.print("Enter medicine name (or type 'done' to finish): ");
            String medicineName = scanner.nextLine().trim();

            if (medicineName.equalsIgnoreCase("done")) {
                dispensing = false;
                break;
            }

            Medicine medicine = medicationInventory.get(medicineName);
            if (medicine != null) {
                System.out.print("Enter quantity to dispense: ");
                int quantity = Integer.parseInt(scanner.nextLine().trim());

                if (quantity <= medicine.getStock()) {
                    medicine.setStock(medicine.getStock() - quantity);
                    System.out.println(quantity + " units of " + medicineName + " dispensed to " + patientName);
                    System.out.println("Updated stock for " + medicineName + ": " + medicine.getStock());
                    if (medicine.getStock() <= medicine.getLowStockAlert()) {
                        System.out.println("Warning: " + medicineName + " stock is low. Current stock: " + medicine.getStock());
                    }
                } else {
                    System.out.println("Insufficient stock for " + medicineName + ". Available stock: " + medicine.getStock());
                }
            } else {
                System.out.println("Medicine not found in inventory.");
            }
        }
    }

    void viewMedicationInventory() {
        System.out.println("Viewing medication inventory...");
        for (Medicine medicine : medicationInventory.values()) {
            System.out.print("Medicine: " + medicine.getName() + " | Stock: " + medicine.getStock());
            if (medicine.getStock() <= medicine.getLowStockAlert()) {
                System.out.println(" | Status: Low Stock Alert!");
            } else {
                System.out.println();
            }
        }
    }

    void submitReplenishmentRequest() {
        System.out.print("Enter medicine name to request replenishment: ");
        String medicineName = scanner.nextLine().trim();
        Medicine medicine = medicationInventory.get(medicineName);
        if (medicine != null) {
            System.out.println("Replenishment request for " + medicineName + " submitted successfully. Awaiting approval.");
            
        } else {
            System.out.println("Medicine not found.");
        }
    }

    private Appointment findAppointmentByPatientName(String patientName) {
        for (Appointment appointment : appointments) {
            if (appointment.pname.equals(patientName)) {
                return appointment;
            }
        }
        return null;
    }
}
