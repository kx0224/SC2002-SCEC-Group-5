package models;
public class Medication {
    private String name;
    private int stock;
    private int lowStockAlert;
    private String med_id;

    public Medication(String name, int stock, int lowStockAlert) {
        this.name = name;
        this.stock = stock;
        this.lowStockAlert = lowStockAlert;
        this.med_id = "";
    }

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

    public void setMed_id(String med_id) {
        this.med_id = med_id;
    }

    public String getMed_id() {
        return med_id;
    }

    public boolean checkLowStock() {
        return stock < lowStockAlert;
    }

    @Override
    public String toString() {
        return "Medication{" +
                "name='" + name + '\'' +
                ", stock=" + stock +
                ", lowStockAlert=" + lowStockAlert +
                ", med_id='" + med_id + '\'' +
                '}';
    }
}
