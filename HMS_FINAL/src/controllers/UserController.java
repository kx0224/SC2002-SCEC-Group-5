package controllers;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

import users.*;
import managers.*;
import menu.*;
import models.Inventory;
import models.MedicalRecords;
import models.User;
import models.UserRoles;
import controllers.*;

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

    public void initialisePatients() {
        if (!users.get(UserRoles.PATIENT).isEmpty()) {
            return; // Prevent re-initialization if already populated
        }
        
        File patientFile = new File("data/Patient.csv");
        try (Scanner sc = new Scanner(patientFile)) {
            sc.nextLine();
            while (sc.hasNextLine()) {
                String[] patientData = sc.nextLine().split(","); // Split by comma
                
                // Ensure each line has the required number of fields
                if (patientData.length <= 8) continue;

                String patientID = patientData[0].trim();
                String name = patientData[1].trim();
                String dob = patientData[2].trim();
                String gender = patientData[3].trim();
                String bloodType = patientData[4].trim();
                String email = patientData[6].trim();
                String phoneNumber = patientData[5].trim();
                String diagnosis = patientData[7].trim();
                String treatment = patientData[8].trim();
                String defaultPassword = "password";

                users.get(UserRoles.PATIENT).add(new Patient(patientID, defaultPassword, name, dob, gender, bloodType, phoneNumber, UserRoles.PATIENT, email));
                MedicalRecords medicalRecord = new MedicalRecords(patientID, name, bloodType);
                medicalRecord.addDiagnosis(diagnosis);
                medicalRecord.addTreatment(treatment);
                medicalRecordsController.addMedicalRecord(patientID, medicalRecord, false);
            }
        } catch (FileNotFoundException e) {
            System.out.println("\n[ERROR] Patient data file not found! Please make sure the file exists at 'data/Patient.csv'.\n");
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
            default -> "U"; // Default prefix for unspecified roles
        };

        // Generate the hospital ID with the format (e.g., "D001", "P005")
        return String.format("%s%03d", prefix, count + 1);
    }

    public void initialiseStaff() {
        if (!users.get(UserRoles.ADMIN).isEmpty() || !users.get(UserRoles.DOCTOR).isEmpty() || !users.get(UserRoles.PHARMACIST).isEmpty()) {
            return; // Prevent re-initialization if already populated
        }
        
        File staffFile = new File("data/Staff_List.csv");
        try (Scanner sc = new Scanner(staffFile)) {
            sc.nextLine();
            while (sc.hasNextLine()) {
                String[] staffData = sc.nextLine().split(","); // Split by comma
                
                // Ensure each line has the required number of fields
                if (staffData.length < 1) continue;

                String staffID = staffData[0].trim();
                String name = staffData[1].trim();
                String role = staffData[2].trim().toUpperCase();
                String gender = staffData[3].trim();
                Integer age = Integer.valueOf(staffData[4].trim());
                String email = staffData[6].trim();
                String phoneNumber = staffData[5].trim();

                // Create Staff object and add to map
                switch (role) {
                    case "PHARMACIST":
                        users.get(UserRoles.PHARMACIST).add(new Pharmacist(staffID, "password", UserRoles.PHARMACIST, name, gender, age, email, phoneNumber));
                        break;
                    case "DOCTOR":
                        users.get(UserRoles.DOCTOR).add(new Doctor(staffID, "password", UserRoles.DOCTOR, name, gender, age, email, phoneNumber));
                        break;
                    case "ADMINISTRATOR":
                        users.get(UserRoles.ADMIN).add(new Administrator(staffID, "password", UserRoles.ADMIN, name, gender, age, email, phoneNumber));
                        break;
                    default:
                        System.out.println("\n[WARNING] Unknown role: " + role + ". Skipping...\n");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("\n[ERROR] Staff data file not found! Please make sure the file exists at 'data/Staff_List.csv'.\n");
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
        System.out.println("\n===============================================");
        System.out.println("    Welcome to the Hospital Management System");
        System.out.println("===============================================\n");
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your hospital ID (or type 'exit' to quit): ");
            String hospitalID = sc.nextLine();
            if (hospitalID.equalsIgnoreCase("exit")) {
                exitingProg();
                return null;
            }

            System.out.print("Enter your password: ");
            String password = sc.nextLine();

            for (Map.Entry<UserRoles, List<User>> entry : users.entrySet()) {
                List<User> userList = entry.getValue();

                for (User user : userList) {
                    if (user.getHospitalID().equalsIgnoreCase(hospitalID)) {
                        // Check if the password matches
                        if (user.getPasswordHash().equals(password)) {
                            // Skip first-time login if the user is a Doctor, Pharmacist, or Administrator
                            if (user instanceof Patient) {
                                isFirstLogin(user);
                            }
                            System.out.println("\n[INFO] Login successful...\n");

                            // Controller launching logic as needed
                            try {
                                if (user instanceof Patient) {
                                    Patient patient = (Patient) user;
                                    PatientView patientView = new PatientView(null);
                                    PatientController patientController = new PatientController(patient, appointmentManager, medicalRecordsController, patientView);
                                    patientView.setPatientController(patientController, appointmentManager, prescriptionManager);
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
                                    AdminController adminController = new AdminController(appointmentManager, replenishmentRequestController, inventory, adminView, medicalRecordsController, admin);
                                    adminView.setAdminController(adminController);
                                    adminController.showMenu();
                                }
                            } catch (Exception e) {
                                System.out.println("\n[ERROR] Error initializing controller: " + e.getMessage() + "\n");
                                e.printStackTrace();
                            }
                            System.out.println("\n[INFO] Logging out...\n");
                            return user;
                        } else {
                            System.out.println("\n[ERROR] Invalid password. Please try again.\n");
                            break;
                        }
                    }
                }
            }
            System.out.println("\n[ERROR] Invalid login credentials. Please try again.\n");
        }
    }
    

    public void isFirstLogin(User user) {
        if (user.getfirstLogin()) {
            System.out.println("\n[NOTICE] You are logging in for the first time. Please change your password.\n");
            user.changePassword();
            user.setfirstLogin(false);
        }
    }

    public void exitingProg() {
        System.out.println("\n[INFO] Exiting program...\n");
        System.exit(0);
    }
    
}