// MedicalRecordDAO.java
package dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.MedicalRecord;

public class MedicalRecordDAO {
    private static final String MEDICAL_RECORDS_FILE = "MedicalRecords.csv";

    // Retrieve all medical records from the CSV file
    public List<MedicalRecord> getAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICAL_RECORDS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("userID")) continue; // Skip header line
                String[] details = line.split(",", -1); // Split all fields, including empty ones
                if (details.length >= 8) {
                    MedicalRecord record = new MedicalRecord(
                        details[0].trim(),  // userID
                        details[1].trim(),  // bloodType
                        parseMedicalHistory(details[2]),  // medicalHistory
                        details[3].trim(),  // diagnosis
                        details[4].trim(),  // treatment
                        details[5].trim(),  // prescription
                        parseQuantity(details[6].trim()),  // quantity
                        details[7].trim()   // comments
                    );
                    medicalRecords.add(record);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading medical records: " + e.getMessage());
        }
        return medicalRecords;
    }

    // Add a new medical record to the CSV file
    public void addMedicalRecord(MedicalRecord record) {
        if (!validateRecord(record)) {
            System.err.println("Invalid medical record provided. Record not added.");
            return;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICAL_RECORDS_FILE, true))) {
            bw.write(String.join(",",
                record.getUserID(),
                record.getBloodType(),
                String.join(";", record.getMedicalHistory()),
                record.getDiagnosis(),
                record.getTreatment(),
                record.getPrescription(),
                String.valueOf(record.getQuantity()),
                record.getComments()
            ));
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error adding medical record: " + e.getMessage());
        }
    }

    // Update an existing medical record in the CSV file
    public void updateMedicalRecord(MedicalRecord updatedRecord) {
        if (!validateRecord(updatedRecord)) {
            System.err.println("Invalid updated medical record provided. Record not updated.");
            return;
        }
        List<MedicalRecord> medicalRecords = getAllMedicalRecords();
        boolean recordUpdated = false;
        File tempFile = new File("MedicalRecords_temp.csv");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            bw.write("userID,bloodType,medicalHistory,diagnosis,treatment,prescription,quantity,comments");
            bw.newLine();
            for (MedicalRecord record : medicalRecords) {
                if (record.getUserID().equals(updatedRecord.getUserID())) {
                    bw.write(String.join(",",
                        updatedRecord.getUserID(),
                        updatedRecord.getBloodType(),
                        String.join(";", updatedRecord.getMedicalHistory()),
                        updatedRecord.getDiagnosis(),
                        updatedRecord.getTreatment(),
                        updatedRecord.getPrescription(),
                        String.valueOf(updatedRecord.getQuantity()),
                        updatedRecord.getComments()
                    ));
                    recordUpdated = true;
                } else {
                    bw.write(String.join(",",
                        record.getUserID(),
                        record.getBloodType(),
                        String.join(";", record.getMedicalHistory()),
                        record.getDiagnosis(),
                        record.getTreatment(),
                        record.getPrescription(),
                        String.valueOf(record.getQuantity()),
                        record.getComments()
                    ));
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating medical record: " + e.getMessage());
            return;
        }
        if (recordUpdated) {
            if (!tempFile.renameTo(new File(MEDICAL_RECORDS_FILE))) {
                System.err.println("Error replacing old medical records file with updated version.");
            }
        } else {
            System.err.println("Medical record with user ID " + updatedRecord.getUserID() + " not found.");
        }
    }

    // Helper method to parse medical history from a semicolon-separated string
    private List<String> parseMedicalHistory(String history) {
        List<String> medicalHistory = new ArrayList<>();
        if (history != null && !history.isEmpty()) {
            String[] historyEntries = history.split(";");
            for (String entry : historyEntries) {
                medicalHistory.add(entry.trim());
            }
        }
        return medicalHistory;
    }

    // Helper method to parse quantity safely
    private int parseQuantity(String quantityStr) {
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid quantity value. Defaulting to 0.");
            return 0;
        }
    }

    // Validate medical record fields
    private boolean validateRecord(MedicalRecord record) {
        return record.getUserID() != null && !record.getUserID().isEmpty() &&
               record.getBloodType() != null && !record.getBloodType().isEmpty() &&
               record.getDiagnosis() != null && !record.getDiagnosis().isEmpty() &&
               record.getTreatment() != null && !record.getTreatment().isEmpty() &&
               record.getPrescription() != null && !record.getPrescription().isEmpty() &&
               record.getQuantity() >= 0 &&
               record.getComments() != null;
    }
}

// End of MedicalRecordDAO.java