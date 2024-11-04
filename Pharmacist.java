package testingpart3;

public class Pharmacist extends User {
    public Pharmacist(String userID, String password, String name) {
        super(userID, password, "Pharmacist", name);
    }

    @Override
    void showMenu() {
        System.out.println("1. View Appointment Outcome Record");
        System.out.println("2. Update Prescription Status");
        System.out.println("3. View Medication Inventory");
        System.out.println("4. Submit Replenishment Request");
        System.out.println("5. Logout");
    }

    @Override
    boolean performAction(int option) {
        switch (option) {
            case 1:
                viewAppointmentOutcomeRecord();
                break;
            case 2:
                updatePrescriptionStatus();
                break;
            case 3:
                viewMedicationInventory();
                break;
            case 4:
                submitReplenishmentRequest();
                break;
            case 5:
                System.out.println("Logging out...");
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }

    void viewAppointmentOutcomeRecord() {
        System.out.print("Enter Patient ID to view appointment outcomes: ");
        String patientID = HospitalManagementSystem.getScanner().nextLine().trim();

        boolean found = false;
        for (AppointmentOutcome outcome : HospitalManagementSystem.getAppointmentOutcomes()) {
            if (outcome.getPatientID().equals(patientID)) {
                found = true;
                System.out.println("Appointment Outcome for Patient ID: " + patientID);
                System.out.println("Doctor's Comment: " + outcome.getOutcome());
                System.out.println("Prescriptions:");
                for (Prescription prescription : outcome.getPrescriptions()) {
                    System.out.println("- " + prescription);
                }
            }
        }

        if (!found) {
            System.out.println("No appointment outcome found for the given Patient ID.");
        }
    }

    void updatePrescriptionStatus() {
    	System.out.print("Enter Patient ID to dispense medication: ");
        String patientID = HospitalManagementSystem.getScanner().nextLine().trim();

        // Find patient based on ID
        Patient patient = (Patient) HospitalManagementSystem.getUsers().get(patientID);
        if (patient == null) {
            System.out.println("Invalid Patient ID. Please try again.");
            return;
        }

        System.out.println("Dispensing medications for patient: " + patient.name);
        boolean dispensing = true;
        while (dispensing) {
            System.out.print("Enter medicine name (or type 'done' to finish): ");
            String medicineName = HospitalManagementSystem.getScanner().nextLine().trim();

            if (medicineName.equalsIgnoreCase("done")) {
                dispensing = false;
                break;
            }

            Medicine medicine = HospitalManagementSystem.getMedicationInventory().get(medicineName);
            if (medicine != null) {
                System.out.print("Enter quantity to dispense: ");
                int quantity = Integer.parseInt(HospitalManagementSystem.getScanner().nextLine().trim());

                if (quantity <= medicine.getStock()) {
                    medicine.setStock(medicine.getStock() - quantity);
                    System.out.println(quantity + " units of " + medicineName + " dispensed to " + patient.name);
                    System.out.println("Updated stock for " + medicineName + ": " + medicine.getStock());
                    if (medicine.getStock() <= medicine.getLowStockAlert()) {
                        System.out.println("Warning: " + medicineName + " stock is low. Current stock: " + medicine.getStock());
                    }
                } else {
                    System.out.println("Insufficient stock for " + medicineName + ". Available stock: " + medicine.getStock());
                }
            } else {
                System.out.println("Medicine not found in inventory.");
            }
        }
    }


    void viewMedicationInventory() {
        System.out.println("Viewing medication inventory...");
        for (Medicine medicine : HospitalManagementSystem.getMedicationInventory().values()) {
            System.out.print("Medicine: " + medicine.getName() + " | Stock: " + medicine.getStock());
            if (medicine.getStock() <= medicine.getLowStockAlert()) {
                System.out.println(" | Status: Low Stock Alert!");
            } else {
                System.out.println();
            }
        }
    }
    
    void submitReplenishmentRequest() {
        System.out.print("Enter medicine name to request replenishment: ");
        String medicineName = HospitalManagementSystem.getScanner().nextLine().trim();
        Medicine medicine = HospitalManagementSystem.getMedicationInventory().get(medicineName);
        if (medicine != null) {
            HospitalManagementSystem.replenishmentRequests.add(medicineName);
            System.out.println("Replenishment request for " + medicineName + " submitted successfully. Awaiting approval.");
        } else {
            System.out.println("Medicine not found.");
        }
    }
}