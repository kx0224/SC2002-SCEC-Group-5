import java.util.*;
import java.io.*;
public class Appointment{
    int timeslot;
    int[] datearray=new int[2]; 
    boolean dateformatcheck=false;
    String pname;
    String outcome;
    int acceptedstatus;
    Scanner sc=new Scanner(System.in);
    Appointment(int ts, String ipdate, String pm){
        this.timeslot=ts;
        this.pname=pm;
        this.acceptedstatus=0;
        this.outcome=null;
        this.datearray=datestringtoarray(ipdate);
    }
    public int[] datestringtoarray(String dateString){
        String[] dateParts = dateString.split("/");
        int[] dateArray=new int[2];
        dateArray[0] = Integer.parseInt(dateParts[0]); // Convert the first part to an integer (month)
        dateArray[1] = Integer.parseInt(dateParts[1]); // Convert the second part to an integer (day)
        return dateArray;
    
    }

    public void showdetails(){
        System.out.println("------------------------");
        System.out.println("Name:",pname);
        System.out.printf("Date: %d/%d \n",datearray[0],datearray[1]);
        System.out.println("Timeslot:",timeslot);
        if (acceptedstatus==0) System.out.println("Pending acceptance");
        else if (acceptedstatus==1) System.out.println("Upcoming");
        else if (acceptedstatus==-1)System.out.println("Declined");
        else System.out.println("Completed");
        if (outcome==null && acceptedstatus==0)System.out.println("Outcome: Pending completion");
        else if (outcome==null && acceptedstatus==-1)System.out.println("Outcome: Appointment declined");
        else System.out.println("Outcome:",outcome);
    }

}
