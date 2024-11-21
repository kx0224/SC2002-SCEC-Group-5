package users;
import models.User;
import models.UserRoles;


public class Pharmacist extends User {

    private Integer age;

    // Constructor for Pharmacist
    public Pharmacist(String userID, String password, UserRoles role, String name, String gender, Integer age, String email, String phonenumber) {
        super(userID, password, role, name, gender, email, phonenumber);
        this.age = age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public void showMenu() {
    }
}






