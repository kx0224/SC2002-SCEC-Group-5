package models;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Inventory {
    private Map<String, Medication> inventory;

    public Inventory() {
        this.inventory = new HashMap<>();
        initializeData();
    }
    private void initializeData() {
        File inventoryFile = new File("data/inventory.csv");
        try (Scanner sc = new Scanner(inventoryFile)) {
            sc.nextLine(); // Skip header line
            while (sc.hasNextLine()) {
                String[] itemData = sc.nextLine().split(",");
                String medID = itemData[0].trim();
                String name = itemData[1].trim();
                int stock = Integer.parseInt(itemData[2].trim());
                int lowstock = Integer.parseInt(itemData[3].trim());
                // Add new item to inventory
                Medication newMedication = new Medication(name, stock, lowstock);
                newMedication.setMed_id(medID);
                inventory.put(medID, newMedication);
            }
            System.out.println("Inventory initialized successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Inventory data file not found!");
        }
    }


    public String getMed_IDbyMedicationName(String medicationName) {
        for (Medication med : inventory.values()) {
            if (med.getName().equalsIgnoreCase(medicationName)) {
                return med.getMed_id();
            }
        }
        return "Medication not found"; // Return a suitable response if not found
    }



    // Method to update stock of a specific medication
    public boolean updateStock(String id, int newStock) {
        Medication med = inventory.get(id);
        if (med != null) {
            int currentStock = med.getStock();
            int updatedStock = currentStock + newStock;
            med.setStock(updatedStock);
            inventory.put(id,med);
            return true;
        } else {
            return false;
        }
    }

    // Method to get the entire inventory as a list
    public List<Medication> getInventory() {
        return new ArrayList<>(inventory.values());
    }

    // Method to get medication by name
    public Medication getMedicationByID(String medication_id) {
        for (Medication med : inventory.values()) {
            if (med.getMed_id().equalsIgnoreCase(medication_id)) {
                return med;
            }
        }
        return null;
    }
    public String getMedicationNameByID(String medicationID) {
        Medication med = getMedicationByID(medicationID);
        if (med != null) {
            return med.getName(); // Assuming the Medication class has a getName() method
        }
        return null; // Return a suitable response if not found
    }



    // Method to get medications with low stock
    public List<Medication> getLowStock() {
        List<Medication> lowStockMedications = new ArrayList<>();
        for (Medication med : inventory.values()) {
            if (med.checkLowStock()) {
                lowStockMedications.add(med);
            }
        }
        return lowStockMedications;
    }

    // Method to display the inventory
    public void displayInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Current Inventory:");
            for (Medication med : inventory.values()) {
                System.out.println(med);
            }
        }
    }

    // Private method to generate unique medication IDs
    private String generateMedId() {
        int count = inventory.size() + 1; // Increment by 1 to avoid zero-based index
        return String.format("M%03d", count);
    }
}
