package users;
import java.util.*;
import domain.User;
import domain.UserRoles;
import domain.Appointment;
import domain.AppointmentStatus;
import managers.AppointmentManager;
public class Doctor extends User {
    public Integer age;
    public Doctor(String userID, String password, UserRoles role, String name, String gender, Integer age, String email, String phonenumber) {
        super(userID, password, role, name, gender, email, phonenumber);
        this.age = age;

    }
    public void setAge(Integer age){
        this.age=age;
    }
    public Integer getAge(){return age;}
    public Set<String> getPatientsUnderCare(AppointmentManager appointmentManager) {
        Set<String> patientsUnderCare = new HashSet<>();
        for (Appointment appointment : appointmentManager.getAllAppointments().values()) {
            if (appointment.get_doctor_id().equals(this.hospitalID) &&
                    (appointment.get_status() == AppointmentStatus.CONFIRMED ||
                            appointment.get_status() == AppointmentStatus.SCHEDULED)) {
                patientsUnderCare.add(appointment.get_patient_id());
            }
        }
        return patientsUnderCare;}

    @Override
    public void showMenu() {
    }

}
