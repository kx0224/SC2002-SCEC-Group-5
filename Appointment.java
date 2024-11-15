package sc2002.group5.oopprojectsrc;
public class Appointment{
    protected int timeslot;
    int[] datearray=new int[2]; 
    String pname;
    String doctorname;
    String[] outcomemedications = null;
    Float[] medicationquantity= null;
    String treatmentype;
    String consultationnotes;
    int acceptedstatus;
    Appointment(int ts, String ipdate, String pm, String dm, int acceptedstatus){
        this.timeslot=ts;
        this.pname=pm;
        this.acceptedstatus=acceptedstatus;
        this.doctorname=dm;
        this.treatmentype=null;
        this.consultationnotes=null;
        this.datearray=datestringtoarray(ipdate);
    }
    Appointment(int ts, String ipdate, String pm, String dm, String[] meds, Float[] medamt, String trtment, String cs, int as){
        this.timeslot=ts;
        this.pname=pm;
        this.acceptedstatus=as;
        this.outcomemedications=meds;
        this.doctorname=dm;
        this.medicationquantity=medamt;
        this.treatmentype=trtment;
        this.consultationnotes=cs;
        this.datearray=datestringtoarray(ipdate);
    }
    public int[] datestringtoarray(String dateString){
        String[] dateParts = dateString.split(";");
        int[] dateArray=new int[2];
        dateArray[0] = Integer.parseInt(dateParts[0]); // Convert the first part to an integer (month)
        dateArray[1] = Integer.parseInt(dateParts[1]); // Convert the second part to an integer (day)
        return dateArray;
    
    }
    public String datearraytoString(){
        String[] datestring=new String[2];
        datestring[0]=String.valueOf(datearray[0]);
        datestring[1]=String.valueOf(datearray[1]);
        return String.join(";",datestring);
    }

    public void showdetails(){
        System.out.println("------------------------");
        System.out.println("Name:"+pname);
        System.out.printf("Date: %d/%d \n",datearray[0],datearray[1]);
        System.out.println("Timeslot:"+timeslot);
        if (acceptedstatus==0) {System.out.println("Pending acceptance"); return;}
        else if (acceptedstatus==1) System.out.println("Upcoming");
        else if (acceptedstatus==-1)System.out.println("Declined");
        else System.out.println("Completed");
        if (acceptedstatus==1)System.out.println("Outcome: Pending completion");
        else if (acceptedstatus==-1)System.out.println("Outcome: Appointment declined");
        else {
            System.out.println("Medications issued:");
            for (int i=0;i<outcomemedications.length;i++){
                System.out.printf("%s  |  %d  g \n", outcomemedications[i], medicationquantity[i]);
            }
            System.out.println("Treatment type:"+treatmentype);
            System.out.println("Consultation notes: \n"+ consultationnotes);
    }

}}
