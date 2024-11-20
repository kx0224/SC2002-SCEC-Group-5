package controllers;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import domain.User;
import domain.UserRoles;
import domain.MedicalRecords;
import users.*;
import managers.*;
import views.*;
import controllers.*;
import domain.Inventory;

public class UserController {
   public static Map<UserRoles, List<User>> users = new HashMap<>();
    private medicalRecordsController medicalRecordsController;
    private static UserController instance;

public UserController(medicalRecordsController medicalRecordsController) {
    this.medicalRecordsController = medicalRecordsController;
    for (UserRoles role : UserRoles.values()) {
        users.putIfAbsent(role, new ArrayList<>());
    }
}
    public static UserController getInstance(medicalRecordsController medicalRecordsController) {
        if (instance == null) {
            instance = new UserController(medicalRecordsController);
        }
        return instance;
    }


    public void initialisePatients(){
        if (!users.get(UserRoles.PATIENT).isEmpty()) {
            return; // Prevent re-initialization if already populated
        }
    File patientfile = new File("data/Patient.csv");
    try(Scanner sc = new Scanner(patientfile)){
    sc.nextLine();
    while (sc.hasNextLine()) {
        String[] PatientData = sc.nextLine().split(","); // Split by comma

        // Ensure each line has the required number of fields
        if (PatientData.length <= 8) continue;

        String PatientID = PatientData[0].trim();
        String name = PatientData[1].trim();
        String dob = PatientData[2].trim();
        String gender = PatientData[3].trim();
        String bloodType = PatientData[4].trim();
        String email = PatientData[6].trim();
        String phoneNumber = PatientData[5].trim();
        String diagnosis = PatientData[7].trim();
        String treatment = PatientData[8].trim();
        String defaultpassword = "password";
        users.get(UserRoles.PATIENT).add(new Patient(PatientID, defaultpassword,name, dob,gender, bloodType, phoneNumber, UserRoles.PATIENT, email));
        MedicalRecords medicalRecord = new MedicalRecords(PatientID, name, bloodType);
        medicalRecord.addDiagnosis(diagnosis);
        medicalRecord.addTreatment(treatment);
        medicalRecordsController.addMedicalRecord(PatientID,medicalRecord,false);
    }
    sc.close();}
    catch (FileNotFoundException e) {
    System.out.println("Patient data file not found!");
   }
}

    public Patient getPatientById(String patientID) {
        List<User> patientList = users.get(UserRoles.PATIENT);
        if (patientList != null) {
            for (User user : patientList) {
                if (user.getHospitalID().equals(patientID) && user instanceof Patient) {
                    return (Patient) user;
                }
            }
        }
        return null; // Return null if no matching patient is found
    }

    public String generateHospitalID(UserRoles role) {
        int count = users.getOrDefault(role, new ArrayList<>()).size();
        // Assign a prefix based on the role
        String prefix = switch (role) {
            case DOCTOR -> "D";
            case PHARMACIST -> "P";
            case ADMIN -> "A";
            case PATIENT -> "PT";
            // Add cases for other roles if necessary
            default -> "U"; // Default prefix for unspecified roles
        };

        // Generate the hospital ID with the format (e.g., "D001", "P005")
        return String.format("%s%03d", prefix, count + 1);
    }
public void initialiseStaff(){
    if (!users.get(UserRoles.ADMIN).isEmpty() || !users.get(UserRoles.DOCTOR).isEmpty() || !users.get(UserRoles.PHARMACIST).isEmpty()) {
        return; // Prevent re-initialization if already populated
    }
    File stafffile = new File("data/Staff_List.csv");
    try(Scanner sc = new Scanner(stafffile)){
    sc.nextLine();
    while (sc.hasNextLine()) {
        String[] StaffData = sc.nextLine().split(","); // Split by comma

        // Ensure each line has the required number of fields
        if (StaffData.length < 1) continue;

        String staffID = StaffData[0].trim();
        String name = StaffData[1].trim();
        String role = StaffData[2].trim().toUpperCase();
        String gender = StaffData[3].trim();
        Integer age = Integer.valueOf(StaffData[4].trim());
        String email = StaffData[6].trim();
        String phonenumber = StaffData[5].trim();

        // Create Patient object and add to map
        switch (role) {
            case "PHARMACIST":
                users.get(UserRoles.PHARMACIST).add(new Pharmacist(staffID,"password", UserRoles.PHARMACIST, name, gender, age, email, phonenumber));
                break;

            case "DOCTOR":
                users.get(UserRoles.DOCTOR).add(new Doctor(staffID,"password", UserRoles.DOCTOR, name, gender, age, email, phonenumber));
                break;

            case "ADMINISTRATOR":
                users.get(UserRoles.ADMIN).add(new Administrator(staffID,"password", UserRoles.ADMIN, name, gender, age, email, phonenumber));
                break;
            default:
                System.out.println("Unknown role: " + role + ". Skipping...");
        }
    }
    sc.close();}
    catch (FileNotFoundException e) {
    System.out.println("Staff data file not found!");
   }
}
    public User getUserById(String userId) {
        for (List<User> userList : users.values()) {
            for (User user : userList) {
                if (user.getHospitalID().equals(userId)) {
                    return user;
                }
            }
        }
        return null;
    }
    public User login(Inventory inventory, AppointmentManager appointmentManager, ReplenishmentRequestController replenishmentRequestController,
                      medicalRecordsController medicalRecordsController, PrescriptionManager prescriptionManager) {
        System.out.println("\nWelcome to the Hospital Management System");
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Enter your hospital ID (or type 'exit' to quit): ");
            String hospitalID = sc.nextLine();
            if (hospitalID.equalsIgnoreCase("exit")) {
                exitingProg();
                return null;
            }

            System.out.println("Enter your password: ");
            String password = sc.nextLine();

            for (Map.Entry<UserRoles, List<User>> entry : users.entrySet()) {

                List<User> userList = entry.getValue();

                for (User user : userList) {
                    if (user.getHospitalID().equalsIgnoreCase(hospitalID)) {

                        // Check if the password matches
                        if (user.getPasswordHash().equals(password)) {

                            isFirstLogin(user);
                            System.out.println("Login successful...");

                            // Controller launching logic as needed
                            try {
                                if (user instanceof Patient) {
                                    Patient patient = (Patient) user;
                                    PatientView patientView = new PatientView(null);
                                    PatientController patientController = new PatientController(patient, appointmentManager, medicalRecordsController, patientView);
                                    patientView.setPatientController(patientController,appointmentManager,prescriptionManager);
                                    patientController.showMenu();
                                } else if (user instanceof Doctor) {
                                    Doctor doctor = (Doctor) user;
                                    DoctorView doctorView = new DoctorView(null);
                                    DoctorController doctorController = new DoctorController(doctor, doctorView, appointmentManager, medicalRecordsController, inventory, prescriptionManager);
                                    doctorView.setDoctorController(doctorController);
                                    doctorController.showMenu();
                                } else if (user instanceof Pharmacist) {
                                    Pharmacist pharmacist = (Pharmacist) user;
                                    PharmacistView pharmacistView = new PharmacistView(null);
                                    PharmacistController pharmacistController = new PharmacistController(inventory, pharmacist, appointmentManager, pharmacistView, prescriptionManager, replenishmentRequestController);
                                    pharmacistView.setPharmacistController(pharmacistController);
                                    pharmacistController.showMenu();
                                } else if (user instanceof Administrator) {
                                    Administrator admin = (Administrator) user;
                                    AdminView adminView = new AdminView(null);
                                    AdminController adminController = new AdminController(appointmentManager, replenishmentRequestController, inventory, adminView, medicalRecordsController,admin);
                                    adminView.setAdminController(adminController);
                                    adminController.showMenu();
                                }
                            } catch (Exception e) {
                                System.out.println("Error initializing controller: " + e.getMessage());
                                e.printStackTrace();
                            }
                            System.out.println("Logging out...");
                            return user;
                        } else {
                            System.out.println("Invalid password. Please try again.");
                            break;
                        }
                    }
                }
            }
            System.out.println("Invalid login credentials. Please try again.");
        }
    }



public void isFirstLogin(User user){
    if (user.getfirstLogin()){
        System.out.println("You are logging in for the first time. Please change your password.");
        user.changePassword();
        user.setfirstLogin(false);
    }
    else{
        return;
    }
}

public void exitingProg(){
    System.out.println("Exiting program...");
    System.exit(0);
}

}
