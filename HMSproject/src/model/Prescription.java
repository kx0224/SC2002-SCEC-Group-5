package model;

public final class Prescription {
    private String prescriptionId;
    private String appointmentId;
    private Medicine medicine; // Use Medicine object instead of medicationId
    private int quantity;
    private PrescriptionStatus status;

    public enum PrescriptionStatus {
        PENDING,
        DISPENSED,
        CANCELED
    }

    // Constructor
    public Prescription(String prescriptionId, String appointmentId, Medicine medicine, int quantity, PrescriptionStatus status) {
        this.prescriptionId = prescriptionId;
        this.appointmentId = appointmentId;
        this.medicine = medicine;
        setQuantity(quantity);
        this.status = status;
    }

    // Getters and Setters
    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    // Method to convert Prescription object to CSV format
    public String toCSV() {
        return String.format("%s,%s,%s,%d,%s", prescriptionId, appointmentId, medicine.getMedicationId(), quantity, status);
    }

    @Override
    public String toString() {
        return String.format("Prescription [ID=%s, AppointmentID=%s, Medicine=%s, Quantity=%d, Status=%s]",
                prescriptionId, appointmentId, medicine.toString(), quantity, status);
    }
}
