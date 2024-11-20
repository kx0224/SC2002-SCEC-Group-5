package HMSystem.views;

import java.util.*;
import HMSystem.controllers.PharmacistController;
import HMSystem.managers.PrescriptionManager;
import HMSystem.domain.PrescribedMed;
import HMSystem.domain.Inventory;
import HMSystem.domain.Medication;
public class PharmacistView {
    private PharmacistController pharmacistController;
    private Scanner sc = new Scanner(System.in);
    public PharmacistView(PharmacistController pharmacistController) {
        this.pharmacistController = pharmacistController;
    }
    public void setPharmacistController(PharmacistController pharmacistController) {
        this.pharmacistController = pharmacistController;
    }
    public int displayMenu(){
        System.out.println("\n--- Pharmacist Menu ---");
        System.out.println("1. View Medication Inventory");
        System.out.println("2. Dispense Medication");
        System.out.println("3. Submit Replenishment Request");
        System.out.println("4. View Prescription Records");
        System.out.println("5. Logout");
        System.out.print("Select an option (1-5): ");
        return sc.nextInt();
    }
    public void showMessage(String message) {
        System.out.println(message);
    }

    public String promptPrescriptionID() {
        sc.nextLine();
        System.out.print("Enter Prescription ID: ");
        return sc.nextLine();
    }
    public String promptMedID() {
        sc.nextLine();
        System.out.print("Enter Medication ID: ");
        return sc.nextLine().trim();
    }
    public Integer promptQuantity() {
        try {
            System.out.print("Enter Quantity to replenish: ");
            return sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric value.");
            sc.next();
            return -1;
        }
    }


    public void displayPrescriptionRecords(PrescriptionManager prescriptionManager){
        System.out.println("\n--- Prescription Records ---");;
        for (PrescribedMed prescription : prescriptionManager.getAllPrescriptions().values()){
            System.out.println("Prescription ID: "+ prescription.getPrescription_id());
            System.out.println("Name: " + prescription.getMedname());
            System.out.println("Quantity: "+ prescription.getQuantity());
            System.out.println("Status: "+ prescription.getStatus());
            System.out.println("-----------------------------------");
        }

    }
    public void displayPrescriptionRecord(PrescribedMed prescribedMed){
        System.out.println("\n--- Prescription Record ---");;
            System.out.println("Prescription ID: "+ prescribedMed.getPrescription_id());
            System.out.println("Name: " + prescribedMed.getMedname());
            System.out.println("Quantity: "+ prescribedMed.getQuantity());
            System.out.println("Status: "+ prescribedMed.getStatus());
            System.out.println("-----------------------------------");


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
