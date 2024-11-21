
import controllers.*;
import managers.*;
import models.*;

public class Main {
    public static void main(String[] args) {
        // Initialize shared instances at startup
        Inventory inventory = new Inventory();
        AppointmentManager appointmentManager = new AppointmentManager();
        ReplenishmentRequestController replenishmentRequestController = new ReplenishmentRequestController(inventory);
        medicalRecordsController medicalRecordsController = new medicalRecordsController();
        PrescriptionManager prescriptionManager = new PrescriptionManager(inventory);

        // Initialize UserController and load data
        UserController userController = UserController.getInstance(medicalRecordsController);
        userController.initialisePatients();
        userController.initialiseStaff();

        // Keep prompting for login until the user chooses to exit
        while (true) {
            User loggedInUser = userController.login(inventory, appointmentManager, replenishmentRequestController, medicalRecordsController, prescriptionManager);
            if (loggedInUser == null) {
                break; // Exit the loop when the user decides to quit
            }
        }
    }
}
