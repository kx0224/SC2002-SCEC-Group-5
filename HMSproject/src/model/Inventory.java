package.model;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Inventory {
    private Map<String, Medication> inventory;

    // Constructor
    public Inventory() {
        this.inventory = new HashMap<>();
        initializeData();
    }

    // Initialize inventory data from CSV file
    private void initializeData() {
        File inventoryFile = new File("data/inventory.csv");
        try (Scanner scanner = new Scanner(inventoryFile)) {
            scanner.nextLine(); // Skip header line
            while (scanner.hasNextLine()) {
                String[] itemData = scanner.nextLine().split(",");
                String medId = itemData[0].trim();
                String name = itemData[1].trim();
                int stock = Integer.parseInt(itemData[2].trim());
                int lowStockAlert = Integer.parseInt(itemData[3].trim());

                Medication newMedication = new Medication(name, stock, lowStockAlert);
                newMedication.setMedId(medId);
                inventory.put(medId, newMedication);
            }
            System.out.println("Inventory initialized successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Inventory data file not found!");
        }
    }

    // Get Medication ID by medication name
    public String getMedicationIdByName(String medicationName) {
        for (Medication med : inventory.values()) {
            if (med.getName().equalsIgnoreCase(medicationName)) {
                return med.getMedId();
            }
        }
        return "Medication not found";
    }

    // Update stock of a specific medication
    public boolean updateStock(String id, int newStock) {
        Medication med = inventory.get(id);
        if (med != null) {
            med.setStock(med.getStock() + newStock);
            return true;
        }
        return false;
    }

    // Get the entire inventory as a list
    public List<Medication> getInventory() {
        return new ArrayList<>(inventory.values());
    }

    // Get medication by ID
    public Medication getMedicationById(String medicationId) {
        return inventory.getOrDefault(medicationId, null);
    }

    // Get medication name by ID
    public String getMedicationNameById(String medicationId) {
        Medication med = getMedicationById(medicationId);
        return (med != null) ? med.getName() : "Medication not found";
    }

    // Get medications with low stock
    public List<Medication> getLowStock() {
        List<Medication> lowStockMedications = new ArrayList<>();
        for (Medication med : inventory.values()) {
            if (med.isLowStock()) {
                lowStockMedications.add(med);
            }
        }
        return lowStockMedications;
    }

    // Display the entire inventory
    public void displayInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Current Inventory:");
            inventory.values().forEach(System.out::println);
        }
    }

    // Generate unique medication ID
    private String generateMedId() {
        int count = inventory.size() + 1; // Increment by 1 to avoid zero-based index
        return String.format("M%03d", count);
    }
}
