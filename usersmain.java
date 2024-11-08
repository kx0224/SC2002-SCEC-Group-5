package grp_assignment;

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
		    		String role;
					if(users.users[i].Checkpassword(password)) {
		    			String role1 = users.users[i].getRole();
		    			if (role1 == "Patient") {
		    				int choice1;
		    				do {
		    					System.out.println("Choose an option:\n"
	                    + "1. View Medical Record\n"
	                    + "2. Update Personal Information\n"
	                    + "3. View Available Appointment Slots\n"
	                    + "4. Schedule an Appointment\n"
	                    + "5. Reschedule an Appointment\n"
	                    + "6. Cancel an Appointment\n"
	                    + "7. View Scheduled Appointments\n"
	                    + "8. View Past Appointment Outcome Records\n"
	                    + "9. Logout");
		    				choice1 = in.nextInt();

		    				switch (choice1) {
		    					case 1:
		    						System.out.println("Viewing Medical Record...");
		    						break;
		    					case 2:
		    						System.out.println("Updating Personal Information...");
		    						break;
		    					case 3:
		    						System.out.println("Viewing Available Appointment Slots...");
		    						break;
		    					case 4:
		    						System.out.println("Scheduling an Appointment...");
		    						break;
		    					case 5:
		    						System.out.println("Rescheduling an Appointment...");
		    						break;
		    					case 6:
		    						System.out.println("Cancelling an Appointment...");
		    						break;
		    					case 7:
		    						System.out.println("Viewing Scheduled Appointments...");
		    						break;
		    					case 8:
		    						System.out.println("Viewing Past Appointment Outcome Records...");
		    						break;
		    					case 9:
		    						System.out.println("Logging out...");
		    						return;
		    					default:
		    						System.out.println("Invalid choice, please try again.");
	            }
	        } 			while (true);
	    }	    			}
		    			
		    			else if (role == "Doctor") {
		    			    int choice2;
		    			    do {
		    			        System.out.println("Choose an option:\n"
		    			                + "1. View Patient Medical Records\n"
		    			                + "2. Update Patient Medical Records\n"
		    			                + "3. View Personal Schedule\n"
		    			                + "4. Set Availability for Appointments\n"
		    			                + "5. Accept or Decline Appointment Requests\n"
		    			                + "6. View Upcoming Appointments\n"
		    			                + "7. Record Appointment Outcome\n"
		    			                + "8. Logout");
		    			        choice2 = in.nextInt();

		    			        switch (choice2) {
		    			            case 1:
		    			                System.out.println("Viewing Patient Medical Records...");
		    			                break;
		    			            case 2:
		    			                System.out.println("Updating Patient Medical Records...");
		    			                break;
		    			            case 3:
		    			                System.out.println("Viewing Personal Schedule...");
		    			                break;
		    			            case 4:
		    			                System.out.println("Setting Availability for Appointments...");
		    			                break;
		    			            case 5:
		    			                System.out.println("Accepting or Declining Appointment Requests...");
		    			                break;
		    			            case 6:
		    			                System.out.println("Viewing Upcoming Appointments...");
		    			                break;
		    			            case 7:
		    			                System.out.println("Recording Appointment Outcome...");
		    			                break;
		    			            case 8:
		    			                System.out.println("Logging out...");
		    			                return;  // Exit to main menu
		    			            default:
		    			                System.out.println("Invalid choice, please try again.");
		    			        }
		    			    } while (true);

		    			}
		    			
		    			else if (role == "Pharmacist") {
		    				int choice3;
		    			    do {
		    			        System.out.println("Choose an option:\n"
		    			                + "1. View Appointment Outcome Record\n"
		    			                + "2. Update Prescription Status\n"
		    			                + "3. View Medication Inventory\n"
		    			                + "4. Submit Replenishment Request\n"
		    			                + "5. Logout");
		    			        choice3 = in.nextInt();

		    			        switch (choice3) {
		    			            case 1:
		    			                System.out.println("Viewing Appointment Outcome Record...");
		    			                break;
		    			            case 2:
		    			                System.out.println("Updating Prescription Status...");
		    			                break;
		    			            case 3:
		    			                System.out.println("Viewing Medication Inventory...");
		    			                break;
		    			            case 4:
		    			                System.out.println("Submitting Replenishment Request...");
		    			                break;
		    			            case 5:
		    			                System.out.println("Logging out...");
		    			                return;  // Exit to main menu
		    			            default:
		    			                System.out.println("Invalid choice, please try again.");
		    			        }
		    			    } while (true);
		    			}
		    			
		    			else if (role == "Administrator") {
		    				int choice4;
		    			    do {
		    			        System.out.println("Choose an option:\n"
		    			                + "1. View and Manage Hospital Staff\n"
		    			                + "2. View Appointments Details\n"
		    			                + "3. View and Manage Medication Inventory\n"
		    			                + "4. Approve Replenishment Requests\n"
		    			                + "5. Logout");
		    			        choice4 = in.nextInt();

		    			        switch (choice4) {
		    			            case 1:
		    			                System.out.println("Viewing and Managing Hospital Staff...");
		    			                break;
		    			            case 2:
		    			                System.out.println("Viewing Appointments Details...");
		    			                break;
		    			            case 3:
		    			                System.out.println("Viewing and Managing Medication Inventory...");
		    			                break;
		    			            case 4:
		    			                System.out.println("Approving Replenishment Requests...");
		    			                break;
		    			            case 5:
		    			                System.out.println("Logging out...");
		    			                return;  // Exit to main menu
		    			            default:
		    			                System.out.println("Invalid choice, please try again.");
		    			        }
		    			    } while (true);
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
