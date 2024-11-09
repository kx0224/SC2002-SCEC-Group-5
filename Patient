import java.util.ArrayList;
import java.util.List;

public static class Patient {
    private String patientID;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String bloodType;
    private String email;
    private List<String> pastDiagnosis;
    private List<String> pastTreatments;
    private List<Appointment> appointments;

    public Patient(String patientID, String name, String dateOfBirth, String gender, String bloodType, String email) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        this.email = email;
        this.pastDiagnosis = new ArrayList<>();
        this.pastTreatments = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }
}



    // Getter methods
    public String getPatientID() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getBloodType() {
        return bloodType;
    }

    public List<String> getPastDiagnosis() {
        return pastDiagnosis;
    }

    public List<String> getPastTreatments() {
        return pastTreatments;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }


    public void updateContactInfo(String newPhone, String newEmail) {
        this.phoneNumber = newPhone;
        this.email = newEmail;
    }

    public void adddiag(String diagnosis) {
        this.pastDiagnosis.add(diagnosis);
    }

    public void addTreatment(String treatment) {
        this.pastTreatments.add(treatment);
    }

    public void scheduleAppointment(String doctorName, String date, String timeSlot) {
        Appointment newAppointment = new Appointment(doctorName, date, timeSlot, "Scheduled");
        appointments.add(newAppointment);
        System.out.println("Appointment scheduled with Dr. " + doctorName + " on " + date + " at " + timeSlot);
    }

    public void rescheduleAppointment(Appointment appointment, String newDate, String newTimeSlot) {
        appointment.setDate(newDate);
        appointment.setTimeSlot(newTimeSlot);
        System.out.println("Appointment rescheduled to " + newDate + " at " + newTimeSlot);
    }

    public void cancelAppointment(Appointment appointment) {
        appointment.setStatus("Cancelled");
        System.out.println("Appointment with Dr. " + appointment.getDoctorName() + " has been cancelled.");
    }
    public static class Appointment {
        private String doctorName;
        private String date;
        private String timeSlot;
        private String status;

        public Appointment(String doctorName, String date, String timeSlot, String status) {
            this.doctorName = doctorName;
            this.date = date;
            this.timeSlot = timeSlot;
            this.status = status;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public String getDate() {
            return date;
        }

        public String getTimeSlot() {
            return timeSlot;
        }

        public String getStatus() {
            return status;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static void main(String[] args) {
        
        Patient patient = new Patient("13444", "Cassandra", "1990-1-2", "Female", "4567843212", "cassandra@gmail.com", "O+");

        patient.updateContactInfo("0945664321", "new.cassandra@gmail.com");

        patient.scheduleAppointment("Ben", "2024-11-15", "10:00 AM");

        Appointment appointment = patient.getAppointments().get(0);
        patient.rescheduleAppointment(appointment, "2024-11-15", "11:00 AM");

        patient.cancelAppointment(appointment);
    }
}
