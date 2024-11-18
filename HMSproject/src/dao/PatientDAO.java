package dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Patient;

public class PatientDAO {
    private static final String PATIENTS_FILE = System.getProperty("user.dir") + "/src/data/Patient_List.csv";

    // Method to retrieve all patients from the CSV file
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        File patientFile = new File(PATIENTS_FILE);

        if (!patientFile.exists()) {
            System.err.println("Error: Patient data file not found at: " + patientFile.getAbsolutePath());
            return patients; // Return an empty list if the file does not exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(patientFile))) {
            String line = br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",", -1);
                if (details.length == 7) {
                    Patient patient = new Patient(
                        details[0].trim(), // userID
                        details[1].trim(), // name
                        details[2].trim(), // dateOfBirth
                        details[3].trim(), // gender
                        details[4].trim(), // phoneNumber
                        details[5].trim(), // emailAddress
                        details[6].trim()  // password
                    );
                    patients.add(patient);
                } else {
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading patient data: " + e.getMessage());
        }
        return patients;
    }

    // Method to retrieve a patient by ID
    public Patient getPatientById(String userID) {
        return getAllPatients().stream()
                .filter(patient -> patient.getUserId().equals(userID))
                .findFirst()
                .orElse(null);
    }

    // Method to update a patient's information in the CSV file
    public boolean updatePatient(Patient updatedPatient) {
        List<Patient> patients = getAllPatients();
        boolean patientUpdated = false;
        File tempFile = new File(System.getProperty("user.dir") + "/src/data/Patients_temp.csv");
        File originalFile = new File(PATIENTS_FILE);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            bw.write("userID,name,dateOfBirth,gender,phoneNumber,emailAddress,password");
            bw.newLine();
            for (Patient patient : patients) {
                if (patient.getUserId().equals(updatedPatient.getUserId())) {
                    bw.write(String.join(",",
                            updatedPatient.getUserId(),
                            updatedPatient.getName(),
                            updatedPatient.getDateOfBirth(),
                            updatedPatient.getGender(),
                            updatedPatient.getPhoneNumber(),
                            updatedPatient.getEmailAddress(),
                            updatedPatient.getPassword()
                    ));
                    patientUpdated = true;
                } else {
                    bw.write(String.join(",",
                            patient.getUserId(),
                            patient.getName(),
                            patient.getDateOfBirth(),
                            patient.getGender(),
                            patient.getPhoneNumber(),
                            patient.getEmailAddress(),
                            patient.getPassword()
                    ));
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating patient data: " + e.getMessage());
            return false;
        }

        if (patientUpdated) {
            if (!originalFile.delete() || !tempFile.renameTo(originalFile)) {
                System.err.println("Error replacing old patient data file with updated version.");
                return false;
            }
        } else {
            System.err.println("Patient with userID " + updatedPatient.getUserId() + " not found.");
            return false;
        }
        return true;
    }

    // Method to add a new patient to the CSV file
    public boolean addPatient(Patient newPatient) {
        List<Patient> patients = getAllPatients();
        if (patients.stream().anyMatch(patient -> patient.getUserId().equals(newPatient.getUserId()))) {
            System.err.println("Error: Patient with userID " + newPatient.getUserId() + " already exists.");
            return false;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATIENTS_FILE, true))) {
            bw.write(String.join(",",
                    newPatient.getUserId(),
                    newPatient.getName(),
                    newPatient.getDateOfBirth(),
                    newPatient.getGender(),
                    newPatient.getPhoneNumber(),
                    newPatient.getEmailAddress(),
                    newPatient.getPassword()
            ));
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error adding patient: " + e.getMessage());
            return false;
        }
        return true;
    }
}
