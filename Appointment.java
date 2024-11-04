import java.util.*;
public class Appointment{
    int timeslot;
    int date;
    String pname;
    String outcome;
    int acceptedstatus;
    Appointment(int ts, int ipdate, String pm){
        this.timeslot=ts;
        this.date=ipdate;
        this.pname=pm;
        this.acceptedstatus=-1;
        this.outcome=Null;
    }
}