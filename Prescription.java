package testingpart3;

public class Prescription {
    private String medicineName;
    private String status;

    public Prescription(String medicineName) {
        this.medicineName = medicineName;
        this.status = "Pending";
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Medicine: " + medicineName + ", Status: " + status;
    }
}
