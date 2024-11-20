package domain;
import java.util.*;

public class AppointmentOutcome {
    private String patientID;
    private String doctorID;
    private String consultation_notes;
    private List<String> appointmentMedication;
    private String appointment_id;
    private String appointmentOutcome;
    private String serviceType;
    private String outcome;


    public AppointmentOutcome(String patientID, String doctorID,String appointment_id) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.consultation_notes = "";
        this.appointmentMedication = new ArrayList<>();
        this.appointment_id = appointment_id;
        this.appointmentOutcome = "";
        this.serviceType = "";
        this.outcome = "";

    }

    public void addPrescription(String prescription) {
        appointmentMedication.add(prescription);
    }

    public List<String> getPrescriptions() {
        return appointmentMedication;
    }

    // Getters and setters for patientID, doctorID, and outcome
    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getConsultation_notes() {
        return consultation_notes;
    }

    public void setConsultation_notes(String consultation_notes) {
        this.consultation_notes = consultation_notes;
    }

    public void setAppointment_id(String appointmentId){ this.appointment_id = appointmentId;}

    public String getAppointment_id(){ return this.appointment_id;}

    public String getAppointmentOutcome() {
        return appointmentOutcome;
    }

    public void setAppointmentOutcome(String appointmentOutcome) {
        this.appointmentOutcome = appointmentOutcome;
    }
    public void setServiceType(String serviceType){ this.serviceType = serviceType;}

    public String getServiceType(){return this.serviceType;}
        public void set_outcome(String outcome){
        this.outcome = outcome;
    }
    public String get_outcome(){
        return outcome;
    }
}
