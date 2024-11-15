package sc2002.group5.oopprojectsrc;
import java.util.*;

import java.io.*;
public class csvAppointment{
    String selectedcsv;
   Scanner sc=new Scanner(System.in);
   boolean check=false;
   ArrayList<String[]> AppointmentList=new ArrayList<>();
   String[] entry=null;
   String[] dataparse=null;
   Float[] dataparseii=null;
   ArrayList<Integer> Indexes=new ArrayList<>();
    ArrayList<String> doctorname=new ArrayList<>();
    ArrayList<String>  Patienttname=new ArrayList<>();
    ArrayList<String[]> outcomemedications=new ArrayList<>();
    ArrayList<Float[]> medicationquantity=new ArrayList<>();
    ArrayList<String> date=new ArrayList<>();
    ArrayList<Integer>  timeslots=new ArrayList<>();
    ArrayList<String> treatment=new ArrayList<>();
    ArrayList<String> noteList=new ArrayList<>();
    ArrayList<Integer> accpetedstatus=new ArrayList<>();


    String Line=null;
    int i=0;
    public csvAppointment(){
        this.selectedcsv=null;
           System.out.println("Initializing Appointment Database....");
           try (BufferedReader reader = new BufferedReader(new InputStreamReader(
               getClass().getResourceAsStream("/Appointment_List.csv")))) {
           while ((Line = reader.readLine()) != null) {
               entry=Line.split(",");
               for (i = 0; i < entry.length; i++) {
                if (entry[i].isEmpty()) {
                    entry[i] = null;
                }
            }
               AppointmentList.add(entry);
               Indexes.add(Integer.valueOf(entry[0]));
               doctorname.add(entry[1]);
               Patienttname.add(entry[2]);
                date.add(entry[3]);
                timeslots.add(Integer.valueOf(entry[4]));
                outcomemedications.add(entry[5] != null ? entry[5].split("\\|") : new String[0]);
                if (entry[6]!=null){
                dataparse=entry[6].split("\\|");
                dataparseii=new Float[dataparse.length];
                for (int j=0;j<dataparse.length;j++){
                    dataparseii[j]=Float.parseFloat(dataparse[j]);
                }
                medicationquantity.add(dataparseii);
            }
                else {
                    medicationquantity.add(new Float[0]);
                }
                
                treatment.add(entry[7]);
                noteList.add(entry[8]);
                accpetedstatus.add(Integer.valueOf(entry[9]));
            
            }}catch (FileNotFoundException e) {
                System.out.println("File Appointment_List.csv not found. Please check the file path and working directory.");
                e.printStackTrace();

               
           }catch (IOException e) {
            e.printStackTrace();
           
           
           }
        
           }
           public ArrayList<Appointment> getAppointment(String Doctorname){
            ArrayList<Appointment> returnlist=new ArrayList<>();
            for (i=0;i<AppointmentList.size();i++){
                if (Doctorname.equalsIgnoreCase(doctorname.get(i))){
                    if (accpetedstatus.get(i).equals(0)){
                        returnlist.add(new Appointment(timeslots.get(i), date.get(i), Patienttname.get(i), doctorname.get(i)));
                    }
                    else{
                        returnlist.add(new Appointment(timeslots.get(i), date.get(i), Patienttname.get(i), doctorname.get(i), outcomemedications.get(i), medicationquantity.get(i), treatment.get(i), noteList.get(i),accpetedstatus.get(i)));
                    }                

                }
            }
            return returnlist;
           }
        
        public void addAppointment(Appointment A){ //For Patients to register for new appointments
            Indexes.add(Indexes.size()+1);
            doctorname.add(A.doctorname);
            Patienttname.add(A.pname);
            date.add(A.datearraytoString());
            timeslots.add(A.timeslot);
            accpetedstatus.add(0);
            outcomemedications.add(new String[0]);
            medicationquantity.add(new Float[0]);
            treatment.add(null);
            noteList.add(null);

        }
        public void updateAppointment(Appointment A){ //for completed appointments.
            for (i=0;i<Indexes.size();i++){
            if (A.doctorname.equalsIgnoreCase(doctorname.get(i))&&A.pname.equalsIgnoreCase(Patienttname.get(i))&&A.datearraytoString().equals(date.get(i))&&A.timeslot==timeslots.get(i)){
            accpetedstatus.set(i, A.acceptedstatus);
            outcomemedications.set(i, A.outcomemedications);
            medicationquantity.set(i, A.medicationquantity);
            treatment.set(i, A.treatmentype);
            noteList.set(i, A.consultationnotes);
            break;

        
        }
        }
    }
        public void updatestatus(Appointment A){//For updating a status, be it accepted for declined
            for (i=0;i<Indexes.size();i++){
                if (A.doctorname.equalsIgnoreCase(doctorname.get(i))&&A.pname.equalsIgnoreCase(Patienttname.get(i))&&A.datearraytoString().equals(date.get(i))&&A.timeslot==timeslots.get(i)){
                    accpetedstatus.set(i, A.acceptedstatus);
                    break;
                }}
        }
        public void updateCSV(){
            for (i=0;i<AppointmentList.size();i++){
            String[] data = {
                String.valueOf(Indexes.get(i)),
                doctorname.get(i),
                Patienttname.get(i),
                date.get(i),
                String.valueOf(timeslots.get(i)),
                String.join("|", outcomemedications.get(i)),
                joinFloatArray(medicationquantity.get(i)), 
                treatment.get(i) != null ? treatment.get(i) : "",
                noteList.get(i) != null ? noteList.get(i) : "",
                String.valueOf(accpetedstatus.get(i))};
            AppointmentList.set(i, data);
            }
            for (i=AppointmentList.size();i<Indexes.size();i++){
                String[] data = {
                    String.valueOf(Indexes.get(i)),
                    doctorname.get(i),
                    Patienttname.get(i),
                    date.get(i),
                    String.valueOf(timeslots.get(i)),
                    String.join("|", outcomemedications.get(i)),
                    joinFloatArray(medicationquantity.get(i)), 
                    treatment.get(i) != null ? treatment.get(i) : "",
                    noteList.get(i) != null ? noteList.get(i) : "",
                    String.valueOf(accpetedstatus.get(i))};
                AppointmentList.add(data);
            }
            String celldata=null;
            System.out.println("Updating CSV file.....");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Appointment_List.csv"))) {
                for (i=0;i<AppointmentList.size();i++) {
                    celldata=String.join(",", AppointmentList.get(i))+System.lineSeparator();
                    writer.write(celldata);
    
                }
                System.out.println("Medicine_List.csv overwritten with new data.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private String joinFloatArray(Float[] array) {
            List<String> strings = new ArrayList<>();
            for (Float f : array) {
                strings.add(f.toString());
            }
            return String.join("|", strings);
        }
        }

