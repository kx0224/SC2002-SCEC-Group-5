package controllers;

import java.util.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import users.Pharmacist;
import managers.*;
import menu.PharmacistView;
import models.Inventory;
import models.Medication;
import models.PrescribedMed;
import models.prescriptionStatus;

public class PharmacistController {
    private Pharmacist pharmacist;
//    private AppointmentManager appointmentManager;
    private Inventory medicationInventory;
    private PharmacistView pharmacistView;
    private PrescriptionManager prescriptionManager;
    private ReplenishmentRequestController replenishmentRequestController;

    public PharmacistController(Inventory inventory, Pharmacist pharmacist,
                                AppointmentManager appointmentManager, PharmacistView pharmacistView,
                                PrescriptionManager prescriptionManager, ReplenishmentRequestController replenishmentRequestController) {
        this.medicationInventory = inventory;
//        this.appointmentManager = appointmentManager;
        this.pharmacist = pharmacist;
        this.pharmacistView = pharmacistView;
        this.prescriptionManager = prescriptionManager;
        this.replenishmentRequestController = replenishmentRequestController;
    }

    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            try{
            int user_choice = pharmacistView.displayMenu();
            switch (user_choice) {
                case 1:
                    pharmacistView.viewMedicationInventory(medicationInventory);
                    break;
                case 2:
                    dispenseMedication();
                    break;
                case 3:
                    submitReplenishmentRequest();
                    break;
                case 4:
                    pharmacistView.displayPrescriptionRecords(prescriptionManager);
                    break;
                case 5:
                    running = false;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (InputMismatchException e) {
                pharmacistView.showMessage("Invalid input. Please enter a valid number.");
            } catch (Exception e) {
                pharmacistView.showMessage("An unexpected error occurred: " + e.getMessage());
            }}
    }

    // Method to dispense medication
    private void dispenseMedication() {
        System.out.println("Dispensing medication...");
        String preID = pharmacistView.promptPrescriptionID().toUpperCase();
        if (preID != null) {
            PrescribedMed prescription = prescriptionManager.getPrescription(preID);
            if (prescription != null) {
                if (prescription.getStatus().equals(prescriptionStatus.DISPENSED)) {
                    System.out.println("Prescription " + preID.toUpperCase() + " has already been fulfilled.");
                    return;
                }
                Scanner sc = new Scanner(System.in);
                pharmacistView.displayPrescriptionRecord(prescription);
                System.out.print("Fulfill this prescription? (Y/N): ");
                String fulfill = sc.nextLine().trim();
                if (fulfill.equalsIgnoreCase("Y")) {
                    Medication medication = medicationInventory.getMedicationByID(prescription.getMedicineID());
                    if (medication == null) {
                        System.out.println("Medication not found in inventory.");
                        return;
                    }
                    if (prescription.getQuantity() > medication.getStock()) {
                        System.out.println("Not enough medication stock to fulfill request.");
                    } else {
                        boolean success = medicationInventory.updateStock(medication.getMed_id(), -prescription.getQuantity());
                        if (success) {
                            prescription.setStatus(prescriptionStatus.DISPENSED);
                            System.out.println("Prescription " + prescription.getPrescription_id() + " has been fulfilled.");
                            System.out.println("Updated Stock level for " + medication.getName() + ": " + medication.getStock());
                        } else {
                            System.out.println("Failed to update inventory.");
                        }
                    }
                }
            } else {
                System.out.println("Prescription not found.");
            }

        }
    }

    // Method to submit a replenishment request
    private void submitReplenishmentRequest() {
        System.out.println("Submitting replenishment request...");
        String medID = pharmacistView.promptMedID();
        Medication medication = medicationInventory.getMedicationByID(medID);
        if (medication != null) {
            int replenishQuantity = pharmacistView.promptQuantity();
            if (replenishQuantity > 0) {
                String pharmacistID = pharmacist.getHospitalID();
                replenishmentRequestController.submitReplenishmentRequest(medID, replenishQuantity, pharmacistID);
                System.out.println("Replenishment request submitted successfully for Medication ID: " + medID + " with quantity: " + replenishQuantity + ".");
            } else {
                System.out.println("Invalid quantity. Please enter a positive number.");
            }
        } else {
            System.out.println("Medication not found.");
        }
    }
}

