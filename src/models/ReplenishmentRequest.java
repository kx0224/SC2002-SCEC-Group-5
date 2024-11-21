package models;

public class ReplenishmentRequest {
    private String replenishmentID;
    private String medicationName;
    private String medicationID;
    private int quantity;
    private String pharmacistID;
    private String adminID;
    public ReplenishmentStatus status;





    public ReplenishmentRequest(String replenishmentID, String medicationID, Integer quantity
                                ) {
        this.replenishmentID = replenishmentID;
        this.medicationID = medicationID;
        this.quantity = quantity;

        this.status = ReplenishmentStatus.PENDING;
    }

    public String getReplenishmentID() {
        return replenishmentID;
    }

    public void setReplenishmentID(String replenishmentID) {
        this.replenishmentID = replenishmentID;
    }

    public String getMedicationID() {
        return medicationID;
    }
    public void setMedicationID(String medicationID) {
        this.medicationID = medicationID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPharmacistID() {
        return pharmacistID;
    }



    public ReplenishmentStatus getStatus() {
        return status;
    }

    public void setStatus(ReplenishmentStatus status) {
        this.status = status;
    }
}

