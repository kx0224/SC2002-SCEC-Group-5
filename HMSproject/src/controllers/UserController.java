package controllers;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import models.*;
import roles.*;
import managers.*;
import menu.*;



public class UserController {
    public static Map<UserRoles, List<User>> users = new HashMap<>();
    private MedicalRecordsController medicalRecordsController;
    private static UserController instance;

    // Constructor
    public UserController(MedicalRecordsController medicalRecordsController) {
        this.medicalRecordsController = medicalRecordsController;
        for (UserRoles role : UserRoles.values()) {
            users.putIfAbsent(role, new ArrayList<>());
        }
    }

    // Singleton Pattern
    public static UserController getInstance(MedicalRecordsController medicalRecordsController) {
        if (instance == null) {
            instance = new UserController(medicalRecordsController);
        }
        return instance;
    }

    // Initialize Patients
    public void initializePatients() {
        if (!users.get(UserRoles.PATIENT).isEmpty()) {
            return; // Prevent re-initialization if already populated
        }
        loadUserData("data/Patient.csv", UserRoles.PATIENT);
    }

    // Initialize Staff
    public void initializeStaff() {
        if (!users.get(UserRoles.ADMIN).isEmpty() || !users.get(UserRoles.DOCTOR).isEmpty() || !users.get(UserRoles.PHARMACIST).isEmpty()) {
            return; // Prevent re-initialization if already populated
        }
        loadUserData("data/Staff_List.csv", UserRoles.STAFF);
    }

    // Load User Data from CSV
    private void loadUserData(String filePath, UserRoles role) {
        File file = new File(filePath);
        try (Scanner sc = new Scanner(file)) {
            sc.nextLine(); // Skip header line
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");
                if (role == UserRoles.PATIENT) {
                    if (data.length < 9) continue;
                    addPatient(data);
                } else {
                    if (data.length < 7) continue;
                    addStaff(data);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(role + " data file not found!");
        }
    }

    // Add Patient
    private void addPatient(String[] patientData) {
        String patientID = patientData[0].trim();
        String name = patientData[1].trim();
        String dob = patientData[2].trim();
        String gender = patientData[3].trim();
        String bloodType = patientData[4].trim();
        String phoneNumber = patientData[5].trim();
        String email = patientData[6].trim();
        String diagnosis = patientData[7].trim();
        String treatment = patientData[8].trim();

        users.get(UserRoles.PATIENT).add(new Patient(patientID, "password", name, dob, gender, bloodType, phoneNumber, UserRoles.PATIENT, email));
        MedicalRecords medicalRecord = new MedicalRecords(patientID, name, bloodType);
        medicalRecord.addDiagnosis(diagnosis);
        medicalRecord.addTreatment(treatment);
        medicalRecordsController.addMedicalRecord(patientID, medicalRecord, false);
    }

    // Add Staff
    private void addStaff(String[] staffData) {
        String staffID = staffData[0].trim();
        String name = staffData[1].trim();
        String role = staffData[2].trim().toUpperCase();
        String gender = staffData[3].trim();
        int age = Integer.parseInt(staffData[4].trim());
        String phoneNumber = staffData[5].trim();
        String email = staffData[6].trim();

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
                System.out.println("Unknown role: " + role + ". Skipping...");
        }
    }

    // Get Patient by ID
    public Patient getPatientById(String patientID) {
        List<User> patientList = users.get(UserRoles.PATIENT);
        if (patientList != null) {
            for (User user : patientList) {
                if (user.getHospitalID().equals(patientID) && user instanceof Patient) {
                    return (Patient) user;
                }
            }
        }
        return null;
    }

    // Get User by ID
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

    // Login Functionality
    public User login(Inventory inventory, AppointmentManager appointmentManager, ReplenishmentRequestController replenishmentRequestController,
                      MedicalRecordsController medicalRecordsController, PrescriptionManager prescriptionManager) {
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

            User user = getUserById(hospitalID);
            if (user != null && user.getPasswordHash().equals(password)) {
                isFirstLogin(user);
                System.out.println("Login successful...");
                launchUserController(user, inventory, appointmentManager, replenishmentRequestController, medicalRecordsController, prescriptionManager);
                System.out.println("Logging out...");
                return user;
            } else {
                System.out.println("Invalid login credentials. Please try again.");
            }
        }
    }

    // Launch Specific User Controller
    private void launchUserController(User user, Inventory inventory, AppointmentManager appointmentManager,
                                      ReplenishmentRequestController replenishmentRequestController, MedicalRecordsController medicalRecordsController,
                                      PrescriptionManager prescriptionManager) {
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
            System.out.println("Error initializing controller: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Handle First Login
    public void isFirstLogin(User user) {
        if (user.getFirstLogin()) {
            System.out.println("You are logging in for the first time. Please change your password.");
            user.changePassword();
            user.setFirstLogin(false);
        }
    }

    // Exit Program
    public void exitingProg() {
        System.out.println("Exiting program...");
        System.exit(0);
    }
}
