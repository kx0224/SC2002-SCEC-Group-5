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
		    
		    for (int i=0; i<users.users.size(); i++) {
		    	if(users.users.get(i).CheckID(hospitalid)){
		    		String role;
					if(users.users.get(i).Checkpassword(password)) {
		    			String role1 = users.users.get(i).getRole();
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
		    				
		    				Patient patient = new Patient(hospitalID, "", "", "", "", "", "");
		    				
		    				choice1 = in.nextInt();

		    				switch (choice1) {
		    					case 1:
		    						System.out.println("Viewing Medical Record...");
		    						patient.viewMedicalRecord();
		    						break;
		    					case 2:
		    						System.out.println("Updating Personal Information...");
		    						System.out.println( "Please enter new email: ");
		    						Scanner in2 = new Scanner(System.in);  
		    					    String newEmail = in2.next();
		    					    System.out.println( "Please enter new contact number: ");
		    						Scanner in3 = new Scanner(System.in);  
		    					    String newContactNumber = in3.next();
		    						patient.updatePersonalInformation(newEmail, newContactNumber);
		    						break;
		    					case 3:
		    						System.out.println("Viewing Available Appointment Slots...");
		    						patient.viewAvailableAppointmentSlots();
		    						break;
		    					case 4:
		    						System.out.println("Scheduling an Appointment...");
		    						System.out.println( "Please enter doctor ID: ");
		    						Scanner in4 = new Scanner(System.in);  
		    					    String doctorID = in4.next();
		    					    System.out.println( "Please enter date: ");
		    						Scanner in5 = new Scanner(System.in);  
		    					    String date = in5.next();
		    					    System.out.println( "Please enter time: ");
		    						Scanner in6 = new Scanner(System.in);  
		    					    String time = in6.next();
		    						patient.scheduleAppointment(doctorID, date, time);
		    						break;
		    					case 5:
		    						System.out.println("Rescheduling an Appointment...");
		    						System.out.println( "Please enter appointment ID: ");
		    						Scanner in7 = new Scanner(System.in);  
		    					    int appointmentID = in7.nextInt();
		    					    System.out.println( "Please enter new date: ");
		    						Scanner in8 = new Scanner(System.in);  
		    					    String newDate = in8.next();
		    					    System.out.println( "Please enter new time: ");
		    						Scanner in9 = new Scanner(System.in);  
		    					    String newTime = in9.next();
		    						patient.rescheduleAppointment(appointmentID, newDate,  newTime);
		    						break;
		    					case 6:
		    						System.out.println("Cancelling an Appointment...");
		    						System.out.println( "Please enter appointment ID: ");
		    						Scanner in10 = new Scanner(System.in);  
		    					    int appointmentID1 = in10.nextInt();
		    						patient.cancelAppointment(appointmentID1);
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

		    				Doctor doctor = new Doctor("", "", hospitalID, role);
		    				
		    				doctor.showMenu();
		    				Scanner in11 = new Scanner(System.in);
		    				choice2 = in11.nextInt();
		    				doctor.performAction(choice2);

		    				
//		    			    int choice2;
//		    			    do {
//		    			        System.out.println("Choose an option:\n"
//		    			                + "1. View Patient Medical Records\n"
//		    			                + "2. Update Patient Medical Records\n"
//		    			                + "3. View Personal Schedule\n"
//		    			                + "4. Set Availability for Appointments\n"
//		    			                + "5. Accept or Decline Appointment Requests\n"
//		    			                + "6. View Upcoming Appointments\n"
//		    			                + "7. Record Appointment Outcome\n"
//		    			                + "8. Logout");
//		    			        choice2 = in.nextInt();
//
//		    			        switch (choice2) {
//		    			            case 1:
//		    			                System.out.println("Viewing Patient Medical Records...");
//		    			                break;
//		    			            case 2:
//		    			                System.out.println("Updating Patient Medical Records...");
//		    			                break;
//		    			            case 3:
//		    			                System.out.println("Viewing Personal Schedule...");
//		    			                break;
//		    			            case 4:
//		    			                System.out.println("Setting Availability for Appointments...");
//		    			                break;
//		    			            case 5:
//		    			                System.out.println("Accepting or Declining Appointment Requests...");
//		    			                break;
//		    			            case 6:
//		    			                System.out.println("Viewing Upcoming Appointments...");
//		    			                break;
//		    			            case 7:
//		    			                System.out.println("Recording Appointment Outcome...");
//		    			                break;
//		    			            case 8:
//		    			                System.out.println("Logging out...");
//		    			                return;  // Exit to main menu
//		    			            default:
//		    			                System.out.println("Invalid choice, please try again.");
//		    			        }
//		    			    } while (true);

		    			}
		    			
		    			else if (role == "Pharmacist") {
		    				int choice3;
		    				Pharmacist pharmacist = new Pharmacist(hospitalID, "", "", "", "");
		    			    pharmacist.showMenu();
		    				Scanner in12 = new Scanner(System.in);
		    				choice3 = in12.nextInt();
		    				pharmacist.performAction(choice3);
		    			}
		    				
//		    				do {
//		    			        System.out.println("Choose an option:\n"
//		    			                + "1. View Appointment Outcome Record\n"
//		    			                + "2. Update Prescription Status\n"
//		    			                + "3. View Medication Inventory\n"
//		    			                + "4. Submit Replenishment Request\n"
//		    			                + "5. Logout");
//		    			        choice3 = in.nextInt();
//
//		    			        switch (choice3) {
//		    			            case 1:
//		    			                System.out.println("Viewing Appointment Outcome Record...");
//		    			                break;
//		    			            case 2:
//		    			                System.out.println("Updating Prescription Status...");
//		    			                break;
//		    			            case 3:
//		    			                System.out.println("Viewing Medication Inventory...");
//		    			                break;
//		    			            case 4:
//		    			                System.out.println("Submitting Replenishment Request...");
//		    			                break;
//		    			            case 5:
//		    			                System.out.println("Logging out...");
//		    			                return;  // Exit to main menu
//		    			            default:
//		    			                System.out.println("Invalid choice, please try again.");
//		    			        }
//		    			    } while (true);
//		    			}
		    			
		    			else if (role == "Administrator") {
		    				int choice4;
		    				Administrator admin = new Administrator(hospitalID, "", "", role);
		    			    admin.showMenu();
		    				Scanner in13 = new Scanner(System.in);
		    				choice4 = in13.nextInt();
		    				admin.performAction(choice4);
		    			}
//		    			    do {
//		    			        System.out.println("Choose an option:\n"
//		    			                + "1. View and Manage Hospital Staff\n"
//		    			                + "2. View Appointments Details\n"
//		    			                + "3. View and Manage Medication Inventory\n"
//		    			                + "4. Approve Replenishment Requests\n"
//		    			                + "5. Logout");
//		    			        choice4 = in.nextInt();
//
//		    			        switch (choice4) {
//		    			            case 1:
//		    			                System.out.println("Viewing and Managing Hospital Staff...");
//		    			                break;
//		    			            case 2:
//		    			                System.out.println("Viewing Appointments Details...");
//		    			                break;
//		    			            case 3:
//		    			                System.out.println("Viewing and Managing Medication Inventory...");
//		    			                break;
//		    			            case 4:
//		    			                System.out.println("Approving Replenishment Requests...");
//		    			                break;
//		    			            case 5:
//		    			                System.out.println("Logging out...");
//		    			                return;  // Exit to main menu
//		    			            default:
//		    			                System.out.println("Invalid choice, please try again.");
//		    			        }
//		    			    } while (true);
		    			
		    			
		    		}
		    	}
		    
		    
		}
		
		while(true);
		
		
	}

}
