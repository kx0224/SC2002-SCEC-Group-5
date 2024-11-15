package sc2002.group5.oopprojectsrc;
import java.util.*;
public class Doctor extends User{
    private ArrayList<Appointment> AllAppointments=null;
    public ArrayList<Appointment> completedAppointments = new ArrayList<>();
    public ArrayList<Appointment> apptRequest = new ArrayList<>();
    public ArrayList<Appointment> declined = new ArrayList<>();
    private ArrayList<Appointment> Upcoming = new ArrayList<>();
    Scanner sc=new Scanner(System.in);
    csvPatient cPatient=new csvPatient();
    String doctorname=null;
    csvAppointment cAppointment=new csvAppointment();
    csvStaff cStaff=new csvStaff();
    int i=0;

    public String[][][] schedule= new String[12][31][10]; //Correspoding to month, day and slot respectively
    Doctor(String userm, String Password1, String hospID, String Role){
        super(userm,Password1,Role);
        Appointment selectionA=null;
        for (i=0;i<cStaff.staffID.size();i++){
            if (this.getHospitalID().equalsIgnoreCase(cStaff.staffID.get(i))) {doctorname=cStaff.nameStrings.get(i); break;}
        }
        AllAppointments=cAppointment.getAppointmentdoctor(doctorname);
        for (i=0;i<AllAppointments.size();i++){
            selectionA=AllAppointments.get(i);
            if (selectionA.acceptedstatus==0) apptRequest.add(selectionA);
            else if (selectionA.acceptedstatus==1) Upcoming.add(selectionA);
            else if (selectionA.acceptedstatus==-1) declined.add(selectionA);
            else completedAppointments.add(selectionA);
        }
        
        
    }
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
                break;
            case 9:
                sc.close();
                cPatient.updateCSV();
                System.out.println("Logging out...");
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }
    
    public void viewschedule(){
        int count,m,d;
        m=0;
        d=0;
        boolean inputcheckformat=false;
        boolean inputcheckvalue=false;
        String nonformatdate=null;
        while (inputcheckformat==false){
        System.out.println("Enter date in format mm/dd");
        nonformatdate=sc.nextLine();
        sc.nextLine();
        if (nonformatdate.charAt(2) != '/'){
            System.out.println("Wrong format, correct format is mm/dd");
        }
        else inputcheckformat=true;}
        while (inputcheckvalue==false){
                        String[] dateParts = nonformatdate.split("/");
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
        int choice=2,m=0,d=0,startts=0,endts=0;
        boolean timeslotcheck=false;
        boolean inputcheckformat=false;
        boolean inputcheckvalue=false;
        String nonformatdate=null;
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
         nonformatdate=sc.nextLine();
        sc.nextLine();
        if (nonformatdate.charAt(2) != '/'){
            System.out.println("Wrong format, correct format is mm/dd");
        }
        else inputcheckformat=true;}
        while (inputcheckvalue==false){
            String[] dateParts = nonformatdate.split("/");
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
        }
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


    }}
    private void acceptOrDeclineAppointmentRequests(){
        if (apptRequest.isEmpty()) {System.out.println("No appointment requests"); return;}
        int usagechoice=0;
        int selection=0;
        int k=0,m=0,d=0;
        Appointment selectedappt=null;
        boolean usage=true;
        while (usage){
            System.out.println("Declining all appointments requesting unavailable timslots.... ");
            for (int i=0;i<apptRequest.size();i++){
                selectedappt=apptRequest.get(i);
                m=selectedappt.datearray[0];
                d=selectedappt.datearray[1];
                if (schedule[m-1][d-1]!=null) declineappointment(apptRequest.get(i));
            }
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
                                apptRequest.set(k-1,apptRequest.get(k));
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
                            apptRequest.set(k-1,apptRequest.get(k));
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
        if (apptRequest.isEmpty()){System.out.println("No requests");return;}
        for (int i=0;i<Upcoming.size();i++) {
            apptRequest.get(i).showdetails();
            System.out.println("Index:"+i+1);
        }
    }
    private void resetschedule(){
        for (int i=0;i<12;i++){
            for (int j=0;j<31;j++){
                for (int k=0;k<10;k++){
                    schedule[i][j][k]=null;
                }
            }
        }
    }
    private void recordAppointmentOutcome(){
        int option,choice,i,typenumber;
        Float quantity=(float)0;
        String medtype=null;
        boolean usage=true;
        Appointment selectedAppointment=null;
        if (completedAppointments.isEmpty()){System.out.println("No completed appointments, nothing can be recorded"); return;}
        while (usage){
            System.out.println("Choose your option");
            System.out.println("1. View completed appointments");
            System.out.println("2. Record Outcome for a completed appointment");
            System.out.println("3. Remove completed appointment from list");
            option=sc.nextInt();
            switch (option){
                case 1:
                for (i=0;i<completedAppointments.size();i++){
                    completedAppointments.get(i).showdetails();
                    System.out.println("Index:"+i+1);
                }
                usage=false;
                break;
                case 2:
                for (i=0;i<completedAppointments.size();i++){
                    Upcoming.get(i).showdetails();
                    System.out.println("Index:"+i+1);
                }
                System.out.println("Choose the upcoming appointment that has been completed, by Index");
                choice=sc.nextInt();
                while (choice>Upcoming.size() || choice<1){
                    System.out.println("Invalid input");
                    System.out.println("Choose the appointment by Index");
                    choice=sc.nextInt();
                }
                Upcoming.get(choice-1).acceptedstatus=2;
                sc.nextLine();
                System.out.println("Enter Treatment type:");
                Upcoming.get(choice-1).treatmentype=sc.nextLine();
                System.out.println("Done!");
                System.out.println("Any specific remarks? Enter below:");
                Upcoming.get(choice-1).consultationnotes=sc.nextLine();
                System.out.println("Done!");
                System.out.println("Enter number of types of medications prescribed:");
                typenumber=sc.nextInt();
                while (typenumber<0){
                    System.out.println("Negative number of types? Invalid input.");
                    System.out.println("Enter number of types of medications prescribed:");
                    typenumber=sc.nextInt(); 
                }
                sc.nextLine();
                selectedAppointment=Upcoming.get(choice-1);
                if (typenumber>0) {
                    Float[] selectedquantity= new Float[typenumber];
                    String [] selectedmeds= new String[typenumber];
                    if (typenumber<5){
                        for (i=0;i<typenumber;i++){
                            System.out.println("Enter Medicine type for Medicine:"+i+1);
                            medtype=sc.nextLine();
                            sc.nextLine();
                            System.out.println("Enter quantity issued in grams:");
                            quantity=sc.nextFloat();
                            selectedmeds[i]=medtype;
                            selectedquantity[i]=quantity;
                        }
                            selectedAppointment.medicationquantity=selectedquantity;
                            selectedAppointment.outcomemedications=selectedmeds;
                            

                    }
                    else {
                        for (i=0;i<5;i++){
                            System.out.println("Enter Medicine type for Medicine:"+i+1);
                            medtype=sc.nextLine();
                            sc.nextLine();
                            System.out.println("Enter quantity issued in grams:");
                            quantity=sc.nextFloat();
                            selectedmeds[i]=medtype;
                            selectedquantity[i]=quantity;
                        }
                        for(i=5;i<typenumber;i++){
                            System.out.println("Enter Medicine type for Medicine:"+i+1);
                            medtype=sc.nextLine();
                            sc.nextLine();
                            System.out.println("Enter quantity issued in grams:");
                            quantity=sc.nextFloat();
                            selectedmeds[i]=medtype;
                            selectedquantity[i]=quantity;
                        }
                        selectedAppointment.medicationquantity=selectedquantity;
                        selectedAppointment.outcomemedications=selectedmeds;
                    }
                    
                }
                System.out.println("Done!");
                completedAppointments.add(Upcoming.get(choice-1));
                if (choice<apptRequest.size()){
                    for (i=choice;i<Upcoming.size();i++){
                        Upcoming.set(i,Upcoming.get(i));
                    }
                Upcoming.remove(i);
                }
                else apptRequest.remove(choice-1);
                System.out.println("Appointment outcome recorded and appointment status updated");
                usage=false;
                break;


               case 3:
               for (i=0;i<completedAppointments.size();i++){
                completedAppointments.get(i).showdetails();
                System.out.println("Index:"+i+1);
            }
            System.out.println("Choose the appointment by Index");
            choice=sc.nextInt();
            while (choice>completedAppointments.size() || choice<1){
                System.out.println("Invalid input");
                System.out.println("Choose the appointment by Index");
                choice=sc.nextInt();
            }
            if (choice==completedAppointments.size()) completedAppointments.remove(choice-1);
            else{
                for (i=choice;i<completedAppointments.size();i++){
                    completedAppointments.set(i-1, completedAppointments.get(i));
                }
                completedAppointments.remove(i);
            }
            System.out.println("Appointment removed from list.");
            usage=false;
            break;
            default:
            System.out.println("Invalid input");
            }
                
            }

        }
        protected void viewPatientMedicalRecords(){
            int i=0;
            Patient P=cPatient.getpatient();
            System.out.println("Patient Info:");
            System.out.println("Blood Type: "+P.getBloodType());
            System.out.println("Past Diagnoses:");
            for (i=0;i<P.pastDiagnosis.size();i++){
                System.out.println(P.pastDiagnosis.get(i));
            }
            System.out.println("Past Treatments");
            for (i=0;i<P.pastTreatments.size();i++){
                System.out.println(P.pastTreatments.get(i));
            }

        }
        protected void updatePatientMedicalRecords(){
            int i=0;
            int choice=0;
            int sel=0;
            boolean appuse=true;
            String input=null;
            Patient P=cPatient.getpatient();
            while (appuse==true){
            System.out.println("Patient Info:");
            System.out.println("Name:"+P.getName());
            System.out.println("Blood Type: "+P.getBloodType());
            System.out.println("Past Diagnoses:");
            for (i=0;i<P.pastDiagnosis.size();i++){
                System.out.printf("%d. %s \n",i+1,P.pastDiagnosis.get(i));
            }
            System.out.println("Past Treatments");
            for (i=0;i<P.pastTreatments.size();i++){
                System.out.printf("%d. %s \n", i+1,P.pastTreatments.get(i));
            }
            System.out.println("1: Add new diagnosis \n 2: Update existing diagnosis \n 3: declare a diagnosis resolved \n 4:Declare a misdiagnosis");
            System.out.println("5: Add any new treatments \n 6:Exit the function");
            choice=sc.nextInt();
            switch (choice) {
                case 1:
                System.out.println("Adding new Diagnoses \n ********************");
                System.out.println("Enter any new diagnosis for the patient, if there aren't anymore, enter 0 ");
                while (true){
                    int k=1;
                    System.out.printf("New diagnosis %d: ", k);
                    input=sc.nextLine();
                    sc.nextLine();
                    if (input=="0") break;
                    P.pastDiagnosis.add(input);
                    System.out.println("Diagnosis added");
                    k++;
                }
                    break;
                case 2:
                System.out.println("Update a diagnosis \n ************************");
                System.out.println("Select diagnosis by index");
                    sel=sc.nextInt();
                    while (sel<1 | sel>P.pastDiagnosis.size()){
                        System.out.println("Invalid input, please try again");
                        System.out.println("Select diagnosis by index");
                        sel=sc.nextInt();
                    }
                    System.out.println("Update the diagnosis, take note that it is an override");
                    input=sc.nextLine();
                    sc.nextLine();
                    P.pastDiagnosis.set(sel-1, input);
                    System.out.println("Diagnosis updated");
                    break;
                case 3:
                System.out.println("Declare a diagnosis resolved \n"+ "************************");
                System.out.println("Select diagnosis by index");
                    sel=sc.nextInt();
                    while (sel<1 | sel>P.pastDiagnosis.size()){
                        System.out.println("Invalid input, please try again");
                        System.out.println("Select diagnosis by index");
                        sel=sc.nextInt();
                    }
                    input=P.pastDiagnosis.get(sel-1)+"-resolved";
                    P.pastDiagnosis.set(sel-1, input);
                    System.out.println("Diagnosis declared as resolved");
                    break;
                case 4:
                System.out.println("Declare a misdiagnosis \n" +"***************");
                System.out.println("Select diagnosis by index");
                sel=sc.nextInt();
                while (sel<1 | sel>P.pastDiagnosis.size()){
                    System.out.println("Invalid input, please try again");
                    System.out.println("Select diagnosis by index");
                    sel=sc.nextInt();
                }
                input=P.pastDiagnosis.get(sel-1)+"-deemed as a misdiagnosis";
                P.pastDiagnosis.set(sel-1, input);
                System.out.println("Diagnosis declared as misdiagnosis");
                break;

                case 5:
                System.out.println("Add all the new treatments, enter 0 if there are none left");
                while (true){
                        int k=1;
                        System.out.printf("New diagnosis %d: ", k);
                        input=sc.nextLine();
                        sc.nextLine();
                        if (input=="0") break;
                        P.pastTreatments.add(input);
                        k++;
                    }
                break;

                case 6:
                appuse=false;
                cPatient.updatePatient(P);
                System.out.println("Exiting Function.....");
                break;
                default:
                System.out.println("##############Invalid Input##############");
                    break;
            }
            



        }

    }}






    

    
