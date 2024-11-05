import java.util.*;
import java.io.*;
import Appointmnt;
public class Doctor extends User{
    public ArrayList<String> patientList = new ArrayList<>();
    public ArrayList<Appointment> completedAppointments = new ArrayList<>();
    public ArrayList<Appointment> apptRequest = new ArrayList<>();
    public ArrayList<Appointment> declined = new ArrayList<>();
    private ArrayList<Appointment> Upcoming = new ArrayList<>();
    public String[][][] schedule= new String[12][31][10]; //Correspoding to month, day and slot respectively
    boolean appuse;
    Doctor(String userm, String Password1, String hospID, String Role){
        super(userm,Password1,hospID,Role);
        boolean appuse=true;
        //import data? How would we approach this?
        
    }
    Scanner sc=new Scanner(System.in);
    void showMenu() {
        System.out.println("1. View Patient Medical Records");
        System.out.println("2. Update Patient Medical Records");
        System.out.println("3. View Personal Schedule");
        System.out.println("4. Set Availability for Appointments");
        System.out.println("5. Accept or Decline Appointment Requests");
        System.out.println("6. View Upcoming Appointments");
        System.out.println("7. Record Appointment Outcome");
        System.out.println("8. Start of new year schdule reset");
        System.out.println("9. Logout");
    }
    boolean performAction(int option) {
        switch (option) {
            case 1:
                viewPatientMedicalRecords();
                break;
            case 2:
                updatePatientMedicalRecords();
                break;
            case 3:
                viewschedule();
                break;
            case 4:
                setavailability();
                break;
            case 5:
                acceptOrDeclineAppointmentRequests();
                break;
            case 6:
                viewUpcomingAppointments();
                break;
            case 7:
                recordAppointmentOutcome();
                break;
            case 8:
                resetschedule();
                System.out.println("Reset complete");
            case 9:
                sc.close();
                System.out.println("Logging out...");
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }
    
    public void Viewschedule(){
        int count,m,d;
        boolean inputcheckformat=false;
        boolen inputcheckvalue=false;
        while (inputcheckformat==false){
        System.out.println("Enter date in format mm/dd");
        String nonformatdate=sc.nextLine();
        sc.nextLine();
        if (nonformatdate.charAt(2) != "/"){
            System.out.println("Wrong format, correct format is mm/dd");
        }
        else inputcheckformat=true;}
        while (inputcheckvalue==false){
            String[] dateParts = dateInput.split("/");
            if (dateParts.length == 2) {
                try {
                    m = Integer.parseInt(dateParts[0]); // Convert the first part to an integer (month)
                    d = Integer.parseInt(dateParts[1]); // Convert the second part to an integer (day
                    // Validate month and day
                    if (m < 1 || m > 12) {
                        System.out.println("Invalid month. Please enter a month between 1 and 12.");
                    } else if (d < 1 || d > 31) {
                        System.out.println("Invalid day. Please enter a day between 1 and 31.");
                    } else {
                        // Additional validation for specific months
                        if ((m == 2 && d > 29) || (m == 4 || m == 6 || m == 9 || m == 11) && d > 30) {
                            System.out.println("Invalid date. Please check the number of days in the month.");
                        } else {
                            // Output the extracted values if everything is valid
                            System.out.println("Month: " + m);
                            System.out.println("Day: " + d);
                            inputcheckvalue = true; // Valid input, exit the loop
                }} }catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter numbers only.");
                }
        }}
        System.out.printf("Schedule for %d / %d \n", d,m);
        for (count=0;count<9;count++){
            System.out.printf("Slot %d, %s", count,schedule[m-1][d-1][count]);
        }

    }
    private void setavailability(){
        int choice=2,m,d,startts,endts;
        boolean timeslotcheck=false;
        boolean inputcheckformat=false;
        boolen inputcheckvalue=false;
        while (choice>1 || choice<0){
        System.out.println("Enter 1 for set available, 0 for set unavailable and -1 to exit");
        choice=sc.nextInt();
        if (choice>1 || choice<-1) System.out.println("Invalid choice");
        }
        if (choice==-1){
            System.out.println("Exiting....");
            return;
        }
        while (inputcheckformat==false){
        System.out.println("Enter date in format mm/dd");
        String nonformatdate=sc.nextLine();
        sc.nextLine();
        if (nonformatdate.charAt(2) != "/"){
            System.out.println("Wrong format, correct format is mm/dd");
        }
        else inputcheck=true;}
        while (inputcheckvalue==false){
            String[] dateParts = dateInput.split("/");
            if (dateParts.length == 2) {
                try {
                    m = Integer.parseInt(dateParts[0]); // Convert the first part to an integer (month)
                    d = Integer.parseInt(dateParts[1]); // Convert the second part to an integer (day
                    // Validate month and day
                    if (m < 1 || m > 12) {
                        System.out.println("Invalid month. Please enter a month between 1 and 12.");
                    } else if (d < 1 || d > 31) {
                        System.out.println("Invalid day. Please enter a day between 1 and 31.");
                    } else {
                        // Additional validation for specific months
                        if ((m == 2 && d > 29) || (m == 4 || m == 6 || m == 9 || m == 11) && d > 30) {
                            System.out.println("Invalid date. Please check the number of days in the month.");
                        } else {
                            // Output the extracted values if everything is valid
                            System.out.println("Month: " + m);
                            System.out.println("Day: " + d);
                            inputcheckvalue = true; // Valid input, exit the loop
                }} }catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter numbers only.");
                }
        }}
        while (timeslotcheck==false){
            System.out.println("Enter start time slot");
            startts=sc.nextInt();
            System.out.println("Enter end time slot");
            if (startts>10)  System.out.println("Invalid start time slot");
            else if (endts<1) System.out.println("Invalid end time slot");
            else if (endts<startts) System.out.println("Why is your start time earlier than your end time?");
            else{
                int z=startts-1;
                if (choice ==1){
                    do {
                        schedule[m-1][d-1][z]=null;
                        z++;                    
                    } while (z<endts);
                }
                if (choice ==0){
                    do {
                        schedule[m-1][d-1][z]="Unavailable";
                        z++;   
                    } while (z<endts);
                }
            }

        }


    }
    private void acceptOrDeclineAppointmentRequests(){
        int usagechoice=0;
        int selection=0;
        int k=0;
        boolean usage=true;
        while (usage){
            System.out.println("1: Accept appointment");
            System.out.println("2: Decline appointment");
            System.out.println("3: View requests");
            System.out.println("0: Exit function");
            System.out.println("Input your choice"); 
            usagechoice=sc.nextInt();
            switch (usagechoice) {
                case 0:
                System.out.println("Exiting...");
                usage=false;
                case 1:
                    System.out.println("Select appointment by index:");
                    selection=sc.nextInt();
                    if (apptRequest.get(selection)==null || selection>apptRequest.size()|| selection<1) System.out.println("Invalid choice");
                    else {
                        acceptappointment(apptRequest.get(selection-1));
                        if (selection<apptRequest.size()){
                            for (k=selection;k<apptRequest.size();k++){
                                apptRequest.set(selection-1,apptRequest.get(k));
                            }
                        apptRequest.remove(k);
                        }
                        else apptRequest.remove(selection-1);
                    }

                    break;
                case 2:
                System.out.println("Select appointment by index:");
                selection = sc.nextInt();
                if (selection < 1 || selection > apptRequest.size() || apptRequest.get(selection - 1) == null) {
                System.out.println("Invalid choice");
            } 
                else {
                    declineappointment(apptRequest.get(selection-1));
                    if (selection<apptRequest.size()){
                        for (k=selection;k<apptRequest.size();k++){
                            apptRequest.set(selection-1,apptRequest.get(k));
                        }
                    apptRequest.remove(k);
                    }
                    else apptRequest.remove(selection-1);
            }
                break;

                case 3:
                    viewrequests();
            
                default:
                System.out.println("Invalid choice.");
                    break;
            }
        }
        

        

    }
    private void acceptappointment(Appointment appt){
        int i;
        appt.acceptedstatus=1;
        for (i=0;i<Upcoming.size();i++){
            if (Upcoming.get(i)==null){
                Upcoming.set(i, appt);
                schedule[(appt.datearray[0]-1)][(appt.datearray[1])-1][(appt.timeslot)-1]=appt.pname;
                System.out.println("Appointment accepted");
                return;
            }
        }
        Upcoming.add(appt);
        schedule[(appt.datearray[0]-1)][(appt.datearray[1])-1][(appt.timeslot)-1]=appt.pname;
        System.out.println("Appointment accepted");
        }

    }
    private void declineappointment(Appointment appt){
        int i;
        appt.acceptedstatus=-1;
        for (i=0;i<Upcoming.size();i++){
            if (Upcoming.get(i)==null){
                declined.set(i, appt);
                System.out.println("Appointment declined");
                return;
            }
        }
        Upcoming.add(appt);
        schedule[(appt.datearray[0]-1)][(appt.datearray[1])-1][(appt.timeslot)-1]=appt.pname;
        System.out.println("Appointment declined");
        }

    private void viewUpcomingAppointments(){
        for (int i=0;i<Upcoming.size();i++) {
            Upcoming.get(i).showdetails();
        }
    }
    private void viewrequests(){
        for (int i=0;i<Upcoming.size();i++) {
            apptRequest.get(i).showdetails();
            System.out.println("Index:",i+1);
        }
    }

    
