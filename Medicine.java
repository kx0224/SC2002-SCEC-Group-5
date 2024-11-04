package testingpart3;

public class Medicine {
    private String name;
    private int stock;
    private int lowStockAlert;

    public Medicine(String name, int stock, int lowStockAlert) {
        this.name = name;
        this.stock = stock;
        this.lowStockAlert = lowStockAlert;
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

    @Override
    public String toString() {
        return "Medicine: " + name + ", Stock: " + stock + ", Low Stock Alert Level: " + lowStockAlert;
    }
}
