package model;

public class Medication {
    private String name;
    private int stock;
    private int lowStockAlert;
    private String medId;

    // Constructor
    public Medication(String name, int stock, int lowStockAlert) {
        this.name = name;
        this.stock = stock;
        this.lowStockAlert = lowStockAlert;
        this.medId = "";
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getLowStockAlert() {
        return lowStockAlert;
    }

    public String getMedId() {
        return medId;
    }

    public void setMedId(String medId) {
        this.medId = medId;
    }

    // Check if stock is below the alert level
    public boolean isLowStock() {
        return stock < lowStockAlert;
    }

    @Override
    public String toString() {
        return String.format("Medication{name='%s', stock=%d, lowStockAlert=%d, medId='%s'}", name, stock, lowStockAlert, medId);
    }
}
