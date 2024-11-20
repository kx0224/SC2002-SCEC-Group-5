package model;

public class ReplenishmentRequest {
    private String replenishmentID;
    private String medicationName;
    private String medicationID;
    private int quantity;
    private String pharmacistID;
    private String adminID;
    public ReplenishmentUpdates status;





    public ReplenishmentRequest(String replenishmentID, String medicationID, Integer quantity
                                ) {
        this.replenishmentID = replenishmentID;
        this.medicationID = medicationID;
        this.quantity = quantity;

        this.status = ReplenishmentUpdates.PENDING;
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



    public ReplenishmentUpdates getStatus() {
        return status;
    }

    public void setStatus(ReplenishmentUpdates status) {
        this.status = status;
    }
}

