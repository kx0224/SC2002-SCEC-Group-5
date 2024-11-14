package hms.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Pharmacist extends User {
    private List<Prescription> pendingPrescriptions;
    private Inventory inventory;

    // Constructor
    public Pharmacist(String hospitalID, String password, String name, String gender, String role, int age) {
        super(hospitalID, password, name, gender, role, age);
        this.pendingPrescriptions = new ArrayList<>();
        this.inventory = Inventory.getInstance(); // Get the singleton instance
    }

    // Method to view all pending prescriptions
    public void viewPendingPrescriptions() {
        if (pendingPrescriptions.isEmpty()) {
            System.out.println("No pending prescriptions.");
        } else {
            System.out.println("Pending Prescriptions:");
            for (int i = 0; i < pendingPrescriptions.size(); i++) {
                System.out.printf("%d. ", i + 1);
                pendingPrescriptions.get(i).showDetails();
            }
        }
    }

    // Method to fulfill a prescription
    public void fulfillPrescription() {
        Scanner scanner = new Scanner(System.in);
        viewPendingPrescriptions();
        System.out.print("Enter the prescription number to fulfill: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (index < 0 || index >= pendingPrescriptions.size()) {
            System.out.println("Invalid selection. Please choose a valid prescription.");
            return;
        }

        Prescription prescription = pendingPrescriptions.get(index);
        if (inventory.isStockAvailable(prescription.getMedications(), prescription.getQuantities())) {
            inventory.updateStock(prescription.getMedications(), prescription.getQuantities());
            prescription.setStatus("Fulfilled");
            pendingPrescriptions.remove(index);
            System.out.println("Prescription fulfilled.");
        } else {
            System.out.println("Insufficient stock to fulfill this prescription.");
        }
    }

    // Method to view and manage inventory
    public void manageInventory() {
        inventory.viewInventory();
        inventory.updateInventory();
    }

    // Method to add a prescription to the pharmacist's pending list
    public void addPrescription(Prescription prescription) {
        pendingPrescriptions.add(prescription);
        System.out.println("Prescription added to pending list.");
    }
}
