package model;

import controller.AppointmentController;
import controller.InventoryController;
import controller.PrescriptionController;

import java.util.List;
import java.util.Scanner;

public class Pharmacist extends Staff {
    private final InventoryController inventoryController;
    private final AppointmentController appointmentController;
    private final PrescriptionController prescriptionController;

    // Constructor
    public Pharmacist(String userID, String name, String dateOfBirth, String gender, int age, String phoneNumber, String emailAddress, String department, String address, String shift, String emergencyContact, String password) {
        super(userID, name, dateOfBirth, gender, age, phoneNumber, emailAddress, "Pharmacist", department, address, shift, emergencyContact, password);
        this.inventoryController = new InventoryController();
        this.appointmentController = new AppointmentController();
        this.prescriptionController = new PrescriptionController();
    }

    // Request replenishment for a medicine
    public void requestReplenishment() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter Medicine ID to request replenishment: ");
            String medicineId = scanner.nextLine();
            System.out.print("Enter Quantity to request: ");
            int quantity = scanner.nextInt();
            if (inventoryController.requestReplenishment(medicineId, quantity)) {
                System.out.println("Replenishment request submitted successfully.");
            } else {
                System.out.println("Failed to submit replenishment request. Medicine not found.");
            }
        }
    }

    // Fulfill a prescription
    public void fulfillPrescription(Prescription prescription) {
        if (inventoryController.fulfillPrescription(prescription)) {
            System.out.println("Prescription fulfilled successfully.");
            prescriptionController.updatePrescriptionStatus(prescription.getPrescriptionId(), "dispensed");
        } else {
            System.out.println("Failed to fulfill prescription. Insufficient stock.");
        }
    }

    // View appointments with prescribed medications
    public void viewAppointmentsWithMedications() {
        List<Appointment> appointments = appointmentController.getAppointmentsWithMedications("pharmacist");
        System.out.println("\nAppointments with Prescribed Medications:");
        for (Appointment appointment : appointments) {
            Appointment.AppointmentOutcome outcome = appointment.getOutcome();
            if (outcome != null) {
                System.out.println("Service Provided: " + outcome.getServiceProvided());
                System.out.println("Prescribed Medications: " + String.join(", ", outcome.getPrescribedMedications()));
                System.out.println("Consultation Notes: " + outcome.getConsultationNotes());
                System.out.println("-----------------------------");
            }
        }
    }

    // View prescription details
    public void viewPrescriptionDetails() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter Prescription ID to view details: ");
            String prescriptionId = scanner.nextLine();
            prescriptionController.viewPrescriptionDetails(prescriptionId);
        }
    }
}
