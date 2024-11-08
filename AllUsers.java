import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AllUsers {

    public ArrayList<User> users = new ArrayList<>();

    public AllUsers() {
        initialiseUsers();
    }

    public void initialiseUsers() {
        String line = "";
        String splitBy = ",";

        try {
            // Reading the Staff_List.csv file
            BufferedReader br = new BufferedReader(new FileReader("Staff_List.csv"));
            while ((line = br.readLine()) != null) {
                String[] newuser = line.split(splitBy);  // Use comma as separator
                User userobj = new User(newuser[0], newuser[2]);
                users.add(userobj);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        line = "";
        try {
            // Reading the Patient_List.csv file
            BufferedReader br = new BufferedReader(new FileReader("Patient_List.csv"));
            while ((line = br.readLine()) != null) {
                String[] newuser = line.split(splitBy);  // Use comma as separator
                User userobj = new User(newuser[0], "Patient");
                users.add(userobj);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
