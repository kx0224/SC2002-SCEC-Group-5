package allusers;

public class User {

	
	public User(String HospitalID, String Role) {
		this.HospitalID = HospitalID;
		this.Password = "password";
		this.Role = Role;
		this.InitialLogin = true;
	}
	
	
	private String HospitalID;
	
	private String Password;
	
	private String Role;
	
	private boolean InitialLogin;
	
	public boolean Checkpassword(String input) {
		if (input.equals(Password)){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void changePassword(String password) {
		this.Password = password;
		this.InitialLogin = false;
	}
	
	public boolean CheckID(String inputID) {
		if (inputID.equals(HospitalID)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean checkInitialLogin() {
		return this.InitialLogin;
	}
	
	public String getRole() {
		return this.Role;
	}


	}

