package domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private String patient_id;
    private String doctor_id;
    private String doctor_name;
    private String appointment_id;
    private LocalDate date;
    private LocalTime startTime;
    private AppointmentStatus status;

    public Appointment(String doctorID, String doctor_name, LocalDate date, LocalTime startTime) {
        this.doctor_id = doctorID;
        this.doctor_name = doctor_name;
        this.date = date;
        this.startTime = startTime;
        this.status = AppointmentStatus.PENDING;
        this.patient_id = null;  // Initialize patient_id to null
    }

    public void set_appointment_id(String doctor_id, LocalDate date, LocalTime startTime) {
        this.appointment_id = doctor_id + "-" + this.date + '-' + this.startTime;
    }

    public String get_appointment_id() {
        return appointment_id;
    }

    public void set_patient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String get_patient_id() {
        return patient_id;
    }

    public void set_doctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
        set_appointment_id(doctor_id, date, startTime);
    }

    public String get_doctor_id() {
        return doctor_id;
    }

    public void set_doctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String get_doctor_name() {
        return doctor_name;
    }

    public void set_date(LocalDate date, String patient_id) {
        this.date = date;
        set_appointment_id(doctor_id, date, startTime);
    }

    public LocalDate get_date() {
        return date;
    }

    public void set_startTime(LocalTime startTime) {
        this.startTime = startTime;
        set_appointment_id(doctor_id, date, startTime);  // Ensure appointment_id is updated when time is set
    }

    public LocalTime get_startTime() {
        return startTime;
    }

    public void set_status(AppointmentStatus status) {
        this.status = status;
    }

    public AppointmentStatus get_status() {
        return status;
    }

    public boolean has_patient_id() {
        return this.patient_id != null && !this.patient_id.isEmpty();
    }
}
