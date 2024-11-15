package sc2002.group5.oopprojectsrc;
import java.util.*;
import java.io.*;
public class csvStaff{
    String selectedcsv;
   Scanner sc=new Scanner(System.in);
   boolean check=false;
   ArrayList<String[]> StaffList=new ArrayList<>();
   String[][] unparseddata=null;
   String[] entry=null;
    ArrayList<String> nameStrings=new ArrayList<>();
    ArrayList<String> rolelist=new ArrayList<>();
    ArrayList<Integer> Age=new ArrayList<>();
    ArrayList<String> staffID=new ArrayList<>();
    ArrayList<String> Gender=new ArrayList<>();
    String Line=null;
    int i=0;
    public csvStaff(){
        this.selectedcsv=null;
           System.out.println("Initializing Staff Database....");
           try (BufferedReader reader = new BufferedReader(new InputStreamReader(
               getClass().getResourceAsStream("/Staff_List.csv")))) {
           while ((Line = reader.readLine()) != null) {
               entry=Line.split(",");
               StaffList.add(entry);
           }
           unparseddata=StaffList.toArray(new String[0][]);
           for (i=0;i<unparseddata.length;i++){
            staffID.add(unparseddata[i][0]);
            nameStrings.add(unparseddata[i][1]);
            rolelist.add(unparseddata[i][2]);
            Gender.add(unparseddata[i][3]);
            Age.add(Integer.parseInt(unparseddata[i][4]));

           }
           
           }catch (FileNotFoundException e) {
               System.out.println("File Patient_list.csv not found. Please check the file path and working directory.");
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }