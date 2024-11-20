package roles;

import models.User;
import models.UserRoles;

public class Pharmacist extends User {
    // Fields
    private Integer age;

    // Constructor for Pharmacist
    public Pharmacist(String userID, String password, UserRoles role, String name, String gender, Integer age, String email, String phonenumber) {
        super(userID, password, role, name, gender, email, phonenumber);
        this.age = age;
    }

    // Getters and Setters
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    // Overridden Method
    @Override
    public void showMenu() {
        // Implementation to be added
    }
}
