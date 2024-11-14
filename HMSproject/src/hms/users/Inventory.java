package hms.users;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Inventory {
    private static Inventory instance; // Singleton instance
    private Map<String, Float> stock; // Maps medication names to available quantities

    // Private constructor to prevent direct instantiation
    private Inventory() {
        this.stock = new HashMap<>();
        initializeInventoryFromCSV("Medicine_List.csv"); // Load inventory from CSV only once
    }

    // Public method to provide access to the singleton instance
    public static Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    // Method to initialize inventory from a CSV file
    private void initializeInventoryFromCSV(String fileName) {
        String line;
        String splitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(splitBy);
                if (data.length == 3) { // Ensure the line has medicine name and quantities
                    String medication = data[0].trim();
                    float quantity;
                    try {
                        quantity = Float.parseFloat(data[1].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid quantity for " + medication + ": " + data[1]);
                        continue;
                    }
                    stock.put(medication.toLowerCase(), quantity); // Store in lowercase for case-insensitive comparison
                } else {
                    System.out.println("Skipping malformed line in " + fileName + ": " + line);
                }
            }
            System.out.println("Inventory loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error reading inventory file: " + e.getMessage());
        }
    }

    // Method to view current inventory
    public void viewInventory() {
        System.out.println("---- Current Inventory ----");
        if (stock.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            for (Map.Entry<String, Float> entry : stock.entrySet()) {
                System.out.printf("Medication: %s, Quantity in Stock: %.2f\n", entry.getKey(), entry.getValue());
            }
        }
    }

    // Method to check if stock is available for a given list of medications and quantities
    public boolean isStockAvailable(List<String> medications, List<Float> quantities) {
        for (int i = 0; i < medications.size(); i++) {
            String med = medications.get(i).toLowerCase(); // Convert to lowercase for case-insensitive comparison
            float requiredQuantity = quantities.get(i);
            float availableQuantity = stock.getOrDefault(med, 0.0f);
            if (availableQuantity < requiredQuantity) {
                System.out.printf("Insufficient stock for %s. Available: %.2f, Required: %.2f\n",
                                  med, availableQuantity, requiredQuantity);
                return false;
            }
        }
        return true;
    }

    // Method to update stock levels based on fulfilled prescription
    public void updateStock(List<String> medications, List<Float> quantities) {
        for (int i = 0; i < medications.size(); i++) {
            String med = medications.get(i).toLowerCase(); // Convert to lowercase for case-insensitive comparison
            float quantityToDeduct = quantities.get(i);
            stock.put(med, stock.get(med) - quantityToDeduct);
        }
        System.out.println("Inventory updated based on fulfilled prescription.");
    }

    // Method to manually update inventory quantities
    public void updateInventory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the medication name to update: ");
        String medication = scanner.nextLine().toLowerCase(); // Convert to lowercase for case-insensitivity
        System.out.print("Enter the new quantity to add: ");
        float quantity = scanner.nextFloat();
        scanner.nextLine(); // Consume newline

        stock.put(medication, stock.getOrDefault(medication, 0.0f) + quantity);
        System.out.printf("Inventory updated: %s, New Quantity in Stock: %.2f\n", medication, stock.get(medication));
    }
}
