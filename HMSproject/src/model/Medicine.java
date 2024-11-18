package model;

public class Medicine {
    private String medicationId;
    private String name;
    private String dosage;
    private int stockLevel;
    private int lowStockLevel;
    private boolean replenishmentRequested;
    private String manufacturer;

    // Constructor
    public Medicine(String medicationId, String name, String dosage, int stockLevel, int lowStockLevel, boolean replenishmentRequested, String manufacturer) {
        this.medicationId = medicationId;
        this.name = name;
        this.dosage = dosage;
        this.stockLevel = stockLevel;
        this.lowStockLevel = lowStockLevel;
        this.replenishmentRequested = replenishmentRequested;
        this.manufacturer = manufacturer;
    }

    // Getters and Setters
    public String getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public int getLowStockLevel() {
        return lowStockLevel;
    }

    public void setLowStockLevel(int lowStockLevel) {
        this.lowStockLevel = lowStockLevel;
    }

    public boolean getReplenishmentRequested() {
        return replenishmentRequested;
    }

    public void setReplenishmentRequested(boolean replenishmentRequested) {
        this.replenishmentRequested = replenishmentRequested;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    // Method to convert Medicine object to CSV format
    public String toCSV() {
        return String.format("%s,%s,%s,%d,%d,%b,%s", medicationId, name, dosage, stockLevel, lowStockLevel, replenishmentRequested, manufacturer);
    }

    @Override
    public String toString() {
        return String.format("Medicine [ID=%s, Name=%s, Dosage=%s, Stock Level=%d, Low Stock Level=%d, Replenishment Requested=%b, Manufacturer=%s]",
                medicationId, name, dosage, stockLevel, lowStockLevel, replenishmentRequested, manufacturer);
    }
}
