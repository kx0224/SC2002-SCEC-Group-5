package sc2002.group5.oopprojectsrc;
import java.util.*;
import java.io.*;
public class csvPatient{
   String selectedcsv;
   Scanner sc=new Scanner(System.in);
   boolean check=false;
   ArrayList<String[]> PatientList=new ArrayList<>();
    ArrayList<String> treatmentlist=new ArrayList<>();
    ArrayList<String> diagnoselist=new ArrayList<>();
    String[] specificdata=null;
    String intermediate=null;
    String Line=null;
    String[] entry=null;
    ArrayList<String> IDlist=new ArrayList<>();
    ArrayList<String> Namelist=new ArrayList<>();
    ArrayList<String> datesofbirth=new ArrayList<>();
    ArrayList<String> Genderlist=new ArrayList<>();
    ArrayList<String> bloodtypelist=new ArrayList<>();
    ArrayList<String> contacinfolist=new ArrayList<>();
    ArrayList<String> treatmentStrings=new ArrayList<>();
    ArrayList<String> diagStrings=new ArrayList<>();
    int i =0;
    
    public csvPatient(){
     this.selectedcsv=null;
        System.out.println("Initializing Patient Database....");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            getClass().getResourceAsStream("/Patient_List.csv")))) {
        while ((Line = reader.readLine()) != null) {
            entry=Line.split(",");
            for (i=0;i<entry.length;i++){
                if (entry[i].isEmpty()) entry[i]="";
            }
            PatientList.add(entry);
            IDlist.add(entry[0]);
            Namelist.add(entry[1]);
            datesofbirth.add(entry[2]);
            Genderlist.add(entry[3]);
            bloodtypelist.add(entry[4]);
            contacinfolist.add(entry[5]);
            diagStrings.add(entry[6]);
            treatmentStrings.add(entry[7]);
        }
        }catch (FileNotFoundException e) {
            System.out.println("File Patient_list.csv not found. Please check the file path and working directory.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Patient getpatient() {
        while (check==false){
        System.out.println("Enter Patient name:");
        String pname=sc.nextLine();
        sc.nextLine();
        for (i=0;i<Namelist.size();i++){
            if (Namelist.get(i).equalsIgnoreCase(pname)){
                check=true;
                break;
            }
        }
        if (check==false)System.out.println("Patient not found");

    }
    System.out.println("Importing Patient Data");
    if (treatmentStrings.get(i).isEmpty()) return new Patient(IDlist.get(i),Namelist.get(i), datesofbirth.get(i), Genderlist.get(i), bloodtypelist.get(i), contacinfolist.get(i));
    specificdata=treatmentStrings.get(i).split("\\|");
    for (i=0;i<specificdata.length;i++)treatmentlist.add(specificdata[i]);
    specificdata=diagStrings.get(i).split("\\|"); 
    //VERY IMPORTANT, DUE TO PARSING ISSUES, SEPARATE ELEMENTS IN ARRAYS IN THOSE CELLS WITH "|"!!!!
    for (i=0;i<specificdata.length;i++)diagnoselist.add(specificdata[i]);
    return new Patient(IDlist.get(i), Namelist.get(i), datesofbirth.get(i), Genderlist.get(i), bloodtypelist.get(i), contacinfolist.get(i), diagnoselist, treatmentlist);

    }
    public void updatePatient(Patient p){
        String resultstrt=String.join("|", p.pastTreatments);
        String resultdiag=String.join("|", p.pastDiagnosis);
        for(i=0;i<IDlist.size();i++){

            if (Namelist.get(i).equals(p.getName())){
                contacinfolist.set(i,p.email);
                diagnoselist.set(i, resultdiag);
                treatmentStrings.set(i, resultstrt);
                break;
            }
        }
        System.out.println("Patient data updated.");
    }
    public void addPatient(Patient p){ //for new Patients
        IDlist.add(p.getPatientID());
        Namelist.add(p.getName());
        datesofbirth.add(p.getDateOfBirth());
        Genderlist.add(p.getGender());
        bloodtypelist.add(p.getBloodType());
        contacinfolist.add(p.getEmail());
    }
    public void updateCSV(){
        String celldata=null;
        for (i=0;i<IDlist.size();i++){
            String[] data={
                IDlist.get(i), Namelist.get(i), datesofbirth.get(i), Genderlist.get(i), bloodtypelist.get(i), contacinfolist.get(i), diagStrings.get(i),treatmentStrings.get(i)
            };
            if (i<PatientList.size())PatientList.set(i, data);
            else PatientList.add(data);
        }
        System.out.println("Updating CSV file.....");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Patient_List.csv"))) {
            for (i=0;i<PatientList.size();i++) {
                celldata=String.join(",", PatientList.get(i))+System.lineSeparator();
                writer.write(celldata);

            }
            System.out.println("Patient_List.csv overwritten with new data.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}