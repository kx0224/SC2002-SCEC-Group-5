package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private String patientId;
    private String doctorId;
    private String doctorName;
    private String appointmentId;
    private LocalDate date;
    private LocalTime startTime;
    private AppointmentStatus status;

    // Constructor
    public Appointment(String doctorId, String doctorName, LocalDate date, LocalTime startTime) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.date = date;
        this.startTime = startTime;
        this.status = AppointmentStatus.PENDING;
        setAppointmentId();
    }

    // Generate Appointment ID
    private void setAppointmentId() {
        this.appointmentId = doctorId + "-" + date + "-" + startTime;
    }

    // Getters and setters
    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
        setAppointmentId();
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        setAppointmentId();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        setAppointmentId();
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}
