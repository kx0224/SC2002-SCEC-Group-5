package users;
import java.util.*;
import domain.User;
import domain.UserRoles;

public class Patient extends User implements IPatientInfo {
    String dateOfBirth;
    String bloodType;


    public Patient(String hospitalID, String password, String name, String dateOfBirth, String gender, String bloodType, String phoneNumber, UserRoles role, String email) {
        super(hospitalID, password, role, name, gender,email,phoneNumber);
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
    }
    @Override
    public void showMenu() {
    }
    @Override
    public String getBloodType() {
        return this.bloodType;
    }

    @Override
    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    @Override
    public String getPatientName() {
        return this.name;
    }

    @Override
    public String getPatientID() {
        return this.hospitalID;
    }

    @Override
  
    public String getPhoneNumber() {
        return this.phonenumber;  // Return the correct field
    }
    

    @Override
    public void setEmail(String email) {
        this.email = email;
    }
}

