package dao;

import model.Medicine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventoryDAO {
    private static final Logger logger = Logger.getLogger(InventoryDAO.class.getName());
    private static final String FILE_PATH = "data/Medicine_List.csv";

    // Method to read inventory from CSV
    public static List<Medicine> readInventoryFromCSV() {
        List<Medicine> medicines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(InventoryDAO.class.getClassLoader().getResource(FILE_PATH).getFile())))) {
            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String id = data[0];
                Strinzg name = data[1];
                String dosage = data[2];
                int stockLevel = Integer.parseInt(data[3]);
                int lowStockLevel = Integer.parseInt(data[4]);
                boolean replenishmentRequested = Boolean.parseBoolean(data[5]);
                String manufacturer = data[6];
                medicines.add(new Medicine(id, name, dosage, stockLevel, lowStockLevel, replenishmentRequested, manufacturer));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading inventory data: " + e.getMessage());
        }
        return medicines;
    }

    // Method to update inventory in CSV
    public static void updateInventoryInCSV(List<Medicine> medicines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(InventoryDAO.class.getClassLoader().getResource(FILE_PATH).getFile())))) {
            bw.write("ID,Name,Dosage,StockLevel,LowStockLevel,ReplenishmentRequested,Manufacturer");
            bw.newLine();
            for (Medicine medicine : medicines) {
                bw.write(medicine.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error updating inventory data: " + e.getMessage());
        }
    }
}
