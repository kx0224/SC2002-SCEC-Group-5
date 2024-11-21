package models;
import java.util.*;
public class PrescribedMed {
    private String medicineID;
    private prescriptionStatus status;
    private int quantity;
    private String prescription_id;
    private String appointment_id;
    private String medname;


    public PrescribedMed(String medicineID, Integer quantity) {
        this.medicineID = medicineID;
        this.status = prescriptionStatus.PENDING;
        this.quantity = quantity;
        this.prescription_id = "";
        this.medname = null;  // Set to null until explicitly provided
    }
    
    public void setmedName(String medname) {
        if (medname != null && !medname.isEmpty()) {
            this.medname = medname;
        } else {
            System.out.println("Warning: Attempt to set invalid medication name.");
        }
    }
    

    public String getMedicineID() {
        return medicineID;
    }
    public void setMedicineID(String medicineID){this.medicineID=medicineID;}

    public void setStatus(prescriptionStatus status) {
        this.status = status;
    }

    public prescriptionStatus getStatus() {
        return status;
    }

    public void setAppointment_id(String appointmentId){ this.appointment_id = appointmentId;}

    public String getAppointment_id(){ return appointment_id;}

    public String getMedname() {
        return medname;}

    public void setPrescription_id(String prescriptionId){
        this.prescription_id = prescriptionId;}
    public String getPrescription_id(){return prescription_id;}

    public void setQuantity(int quantity){ this.quantity = quantity;}
    public Integer getQuantity(){return quantity;}

    @Override
    public String toString() {
        return "Medicine ID: " + medicineID + ", Status: " + status;
    }

    public String generatePrescription_id(Map<String, PrescribedMed> prescriptionMap){
        int count =prescriptionMap.size();
        return String.format("PP%03d", count+1);

    }
}
