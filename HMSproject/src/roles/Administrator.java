package roles;

import models.User;
import models.UserRoles;

public class Administrator extends User {
    // Fields
    public Integer age;

    // Constructor for Administrator
    public Administrator(String userID, String password, UserRoles role, String name, String gender, Integer age, String email, String phonenumber) {
        super(userID, password, role, name, gender, email, phonenumber);
        this.age = age;
    }

    // Getters and Setters
    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    // Overridden Method
    @Override
    public void showMenu() {
        // Implementation to be added
    }
}
