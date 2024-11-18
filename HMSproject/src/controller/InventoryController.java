package controller;

import dao.InventoryDAO;
import dao.PrescriptionDAO;
import model.Medicine;
import model.Prescription;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventoryController {
    private static final Logger logger = Logger.getLogger(InventoryController.class.getName());

    // Load all medicines from CSV file
    public List<Medicine> loadMedicines() {
        return InventoryDAO.readInventoryFromCSV();
    }

    // Update the stock level of a specific medicine
    public boolean updateMedicineStock(String medicationId, int quantityChange) {
        List<Medicine> medicines = loadMedicines();
        boolean updated = false;
        for (Medicine medicine : medicines) {
            if (medicine.getMedicationId().equalsIgnoreCase(medicationId)) {
                int newStockLevel = medicine.getStockLevel() + quantityChange;
                if (newStockLevel < 0) {
                    logger.log(Level.WARNING, "Insufficient stock for Medication ID: " + medicationId);
                    return false;
                }
                medicine.setStockLevel(newStockLevel);
                updated = true;
                break;
            }
        }
        if (updated) {
            saveMedicinesToCSV(medicines);
            logger.log(Level.INFO, "Stock updated for Medication ID: " + medicationId);
            return true;
        } else {
            logger.log(Level.WARNING, "Medication ID not found: " + medicationId);
            return false;
        }
    }

    // Save medicines to CSV file
    private void saveMedicinesToCSV(List<Medicine> medicines) {
        InventoryDAO.updateInventoryInCSV(medicines);
    }

    // Display inventory list
    public void showInventoryMenu() {
        List<Medicine> medicines = loadMedicines();
        System.out.println("\nInventory List:");
        for (Medicine medicine : medicines) {
            System.out.println("- " + medicine.getName() + " (Stock Level: " + medicine.getStockLevel() + ", Low Stock Alert: " + medicine.getLowStockLevel() + ", Manufacturer: " + medicine.getManufacturer() + ")");
        }
    }

    // Update low stock alert level
    public boolean updateLowStockAlert(String medicineId, int newLowStockLevel) {
        List<Medicine> medicines = loadMedicines();
        for (Medicine medicine : medicines) {
            if (medicine.getMedicationId().equalsIgnoreCase(medicineId)) {
                medicine.setLowStockLevel(newLowStockLevel);
                saveMedicinesToCSV(medicines);
                logger.log(Level.INFO, "Low stock level updated successfully for medicine: " + medicineId);
                return true;
            }
        }
        logger.log(Level.WARNING, "Medicine not found for updating low stock level: " + medicineId);
        return false;
    }

    // Approve replenishment requests
    public void approveReplenishmentRequests() {
        List<Medicine> medicines = loadMedicines();
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nReplenishment Requests:");
        for (Medicine medicine : medicines) {
            if (medicine.getReplenishmentRequested()) {
                System.out.println("- " + medicine.getName() + " (Current Stock: " + medicine.getStockLevel() + ")");
            }
        }

        System.out.print("Enter Medicine ID to approve replenishment request: ");
        String medicineId = scanner.nextLine();
        boolean replenished = false;
        for (Medicine medicine : medicines) {
            if (medicine.getMedicationId().equalsIgnoreCase(medicineId) && medicine.getReplenishmentRequested()) {
                System.out.print("Enter quantity to replenish: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                medicine.setStockLevel(medicine.getStockLevel() + quantity);
                medicine.setReplenishmentRequested(false);
                replenished = true;
                break;
            }
        }

        if (replenished) {
            saveMedicinesToCSV(medicines);
            System.out.println("Replenishment request approved and stock updated successfully.");
        } else {
            logger.log(Level.WARNING, "Invalid Medicine ID or no replenishment request found.");
        }
    }

    // Get low stock medicines
    public List<Medicine> getLowStockMedicines() {
        List<Medicine> medicines = loadMedicines();
        return medicines.stream()
                .filter(medicine -> medicine.getStockLevel() < medicine.getLowStockLevel())
                .toList();
    }

    // Method to handle replenishment request from pharmacists
    public boolean requestReplenishment(String medicineId, int quantity) {
        List<Medicine> medicines = loadMedicines();
        for (Medicine medicine : medicines) {
            if (medicine.getMedicationId().equalsIgnoreCase(medicineId)) {
                medicine.setReplenishmentRequested(true);
                saveMedicinesToCSV(medicines);
                logger.log(Level.INFO, "Replenishment request submitted for medicine: " + medicineId);
                return true;
            }
        }
        logger.log(Level.WARNING, "Medicine not found for replenishment request: " + medicineId);
        return false;
    }

    // Method to fulfill a prescription
    public boolean fulfillPrescription(Prescription prescription) {
        String medicationId = prescription.getMedicationId();
        int quantity = prescription.getQuantity();
        boolean success = updateMedicineStock(medicationId, -quantity);
        if (success) {
            logger.log(Level.INFO, "Prescription fulfilled for Medication ID: " + medicationId);
        } else {
            logger.log(Level.WARNING, "Failed to fulfill prescription for Medication ID: " + medicationId);
        }
        return success;
    }
}
