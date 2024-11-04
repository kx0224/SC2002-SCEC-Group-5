package allusers;

import java.util.Scanner;

public class usersmain {

	public static void main(String[] args) {
		
		AllUsers users = new AllUsers();
		
		do {
			System.out.println( "HospitalID:");
		    Scanner in = new Scanner(System.in);  
		    String hospitalid = in.next();
		    
		    System.out.println( "Password:");
		    Scanner in1 = new Scanner(System.in);  
		    String password = in1.next();
		    
		    for (int i=0; i<users.users.length; i++) {
		    	if(users.users[i].CheckID(hospitalid)){
		    		if(users.users[i].Checkpassword(password)) {
		    			String role = users.users[i].getRole();
		    			if (role == "Patient") {
		    				System.out.println( "● View Medical Record\r\n"
		    						+ "● Update Personal Information\r\n"
		    						+ "● View Available Appointment Slots\r\n"
		    						+ "● Schedule an Appointment\r\n"
		    						+ "● Reschedule an Appointment\r\n"
		    						+ "● Cancel an Appointment\r\n"
		    						+ "● View Scheduled Appointments\r\n"
		    						+ "● View Past Appointment Outcome Records\r\n"
		    						+ "● Logout");		    			}
		    			
		    			else if (role == "Doctor") {
		    				System.out.println("View Patient Medical Records\r\n"
		    						+ "● Update Patient Medical Records\r\n"
		    						+ "● View Personal Schedule\r\n"
		    						+ "● Set Availability for Appointments\r\n"
		    						+ "● Accept or Decline Appointment Requests\r\n"
		    						+ "● View Upcoming Appointments\r\n"
		    						+ "● Record Appointment Outcome\r\n"
		    						+ "● Logout \r\n");
		    			}
		    			
		    			else if (role == "Pharmacist") {
		    				System.out.println("● View Appointment Outcome Record\r\n"
		    						+ "● Update Prescription Status\r\n"
		    						+ "● View Medication Inventory\r\n"
		    						+ "● Submit Replenishment Request\r\n"
		    						+ "● Logout \r\n");
		    			}
		    			
		    			else if (role == "Administrator") {
		    				System.out.println(" View and Manage Hospital Staff\r\n"
		    						+ "● View Appointments details\r\n"
		    						+ "● View and Manage Medication Inventory\r\n"
		    						+ "● Approve Replenishment Requests\r\n"
		    						+ "● Logout ");
		    			}
		    			
		    		}
		    	}
		    }
		    
		}
		
		while(true);
		
		
	}

	private static String hospitalid() {
		// TODO Auto-generated method stub
		return null;
	}

}
