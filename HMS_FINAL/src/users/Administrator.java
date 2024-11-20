package users;
import java.util.*;
import domain.User;
import domain.UserRoles;

public class Administrator extends User {
    public Integer age;
    public Administrator(String userID, String password, UserRoles role, String name, String gender, Integer age, String email, String phonenumber) {
        super(userID, password, role, name, gender,email,phonenumber);
        this.age = age;
    }
    public void setAge(Integer age){
        this.age=age;
    }
    public Integer getAge(){return age;}
    @Override
    public void showMenu() {
    }

}
